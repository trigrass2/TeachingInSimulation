package com.cas.sim.tis.app.state;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.listener.TypicalCaseListener;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.asset.ModelKey;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;

import javafx.application.Platform;

public class TypicalCaseState extends BaseState {
	private static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";
	private Node root;

	private Spatial holding;
	private Spatial desktop;
	private ElecComp elecComp;

	private PointLight pointLight;

	private SceneCameraState cameraState;
	private boolean moved_before_putdown;

	private CircuitState circuitState;
	private TypicalCaseListener listener;

	@Override
	protected void initializeLocal() {
		// 布置场景
		arrangementScene();

		// 添加操作模式State
		cameraState = new SceneCameraState();
		stateManager.attach(cameraState);

		// 绑定事件
		bindEvents();

		// 默认新建案例
		IContent content = SpringUtil.getBean(PageController.class).getIContent();
		if (content instanceof TypicalCase3D) {
			((TypicalCase3D) content).setupCase(new TypicalCase());
		}

		// 结束加载界面
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	@Override
	public void update(float tpf) {
		pointLight.setPosition(cam.getLocation());
		super.update(tpf);
	}

	@Override
	public void cleanup() {
		// 移除所有模型
		rootNode.detachAllChildren();
		// 移除操作模式State
		stateManager.detach(cameraState);
		// 删除监听事件
		listener.unregisterInput();
		super.cleanup();
	}

	public void setupCase(TypicalCase typicalCase) {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 创建新的circuitState
		circuitState = new CircuitState(typicalCase, root);
		stateManager.attach(circuitState);

		// 尝试解析出存档对象
		Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
		if (archive == null) {
			return;
		}
		circuitState.read(archive);
		// 结束加载界面
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	private void arrangementScene() {
		// 0、新建场景节点
		root = new Node(TYPICAL_CASE_ROOT_NODE);
		rootNode.attachChild(root);
		// 1、加载桌子
		desktop = loadAsset(new ModelKey("Model/Desktop/desktop.j3o"));
		desktop.setName("Circuit-Desktop");
		MikktspaceTangentGenerator.generate(desktop);
		root.attachChild(desktop);
		// 2、添加灯光
		setupLight();
	}

	private void setupLight() {
		pointLight = new PointLight();
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	private void bindEvents() {
		listener = new TypicalCaseListener(this);
		listener.registerWithInput(inputManager);
	}

	public void mouseMoved() {
		moved_before_putdown = true;
		if (holding == null) {
			return;
		}

		@Nullable
		Vector3f contactPoint = JmeUtil.getContactPointFromCursor(desktop, cam, inputManager);
		if (contactPoint == null) {
			return;
		}
		holding.setLocalTranslation(contactPoint);
	}

	public void mouseClicked(boolean pressed) {
		if (elecComp == null) {
			return;
		}
		if (pressed) {
			moved_before_putdown = false;
		} else if (!moved_before_putdown) {
			putDown();
		}
	}

	public void mouseRightClicked(boolean pressed) {
		if (holding == null) {
			return;
		}
		if (pressed) {
			holding.removeFromParent();
			holding = null;
			elecComp = null;
			cameraState.setZoomEnable(true);
		}
	}

	public void mouseWheel(boolean positive) {
		if (holding == null) {
			return;
		}
		if (positive) {
			holding.rotate(0, FastMath.DEG_TO_RAD * 90, 0);
		} else {
			holding.rotate(0, -FastMath.DEG_TO_RAD * 90, 0);
		}
	}

	/**
	 * 拿着一个元器件，准备放置在电路板上
	 */
	@JmeThread
	public void hold(ElecComp elecComp) {
		if (holding != null) {
			holding.removeFromParent();
		}
//		禁用相机的滚轮事件，留给旋转模型用。
		cameraState.setZoomEnable(false);
//		加载相应的模型
		try {
			holding = loadAsset(new ModelKey(elecComp.getMdlPath()));
//			设置Holding的模型对鼠标透明
			JmeUtil.setMouseVisible(holding, false);

			holding.scale(25);
			root.attachChild(holding);
			this.elecComp = elecComp;
		} catch (Exception e) {
			cameraState.setZoomEnable(true);
		}
	}

	/**
	 * 将一个元器件放置在电路板上
	 */
	@JmeThread
	public void putDown() {
//		取消Holding的模型对鼠标透明
		JmeUtil.setMouseVisible(holding, true);
		try {
//			1、获取相应元器件
			ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
//			2、将元器件模型与元器件对象一起加入电路板中
			circuitState.attachToCircuit(holding, elecCompDef);
		} catch (Exception e) {
//			删除出错的模型
			holding.removeFromParent();

			LOG.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
		} finally {
			holding = null;
			elecComp = null;
			cameraState.setZoomEnable(true);
		}
	}

	/**
	 * 将一个元器件从电路板上拿起来
	 */
	@JmeThread
	public Spatial takeUp(ElecCompDef comp) {
		holding = comp.getSpatial();
		return holding;
	}

	public Spatial getHolding() {
		return holding;
	}

	public void save() {
		if (circuitState != null) {
			circuitState.save();
		}
		// 结束加载界面
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	public CircuitState getCircuitState() {
		return circuitState;
	}

}
