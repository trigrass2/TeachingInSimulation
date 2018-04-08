package com.cas.sim.tis.app.state;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.listener.TypicalCaseListener;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.MsgUtil;
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
import javafx.scene.control.Alert.AlertType;

public class TypicalCaseState extends BaseState {
//	每次打开新的案例，都会创建一个新的节点，存放与案例相关的模型
	private static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";

	private Node root;

//	当前手中的元器件对象
	private ElecComp elecComp;

	private Spatial desktop;

	private PointLight pointLight;

	private SceneCameraState cameraState;

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
		circuitState = new CircuitState(this, typicalCase, root);
		stateManager.attach(circuitState);

		// 尝试解析出存档对象
		Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
		if (archive == null) {
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
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

		/*
		 * 左键点击桌面，放置手中的元器件 右键点击桌面，取消手中的元器件
		 */
		addListener(desktop, new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				putDown();
			}

			@Override
			public void mouseRightClicked(MouseEvent e) {
				discard();
			}
		});
	}

	public void mouseMoved() {
//		moved_before_putdown = true;
		if (elecComp == null) {
			return;
		}

		@Nullable
		Vector3f contactPoint = JmeUtil.getContactPointFromCursor(desktop, cam, inputManager);
		if (contactPoint == null) {
			return;
		}
		elecComp.getSpatial().setLocalTranslation(contactPoint);
	}

	public void mouseWheel(boolean positive) {
		if (elecComp == null) {
			return;
		}
		if (positive) {
			elecComp.getSpatial().rotate(0, FastMath.DEG_TO_RAD * 90, 0);
		} else {
			elecComp.getSpatial().rotate(0, -FastMath.DEG_TO_RAD * 90, 0);
		}
	}

	/**
	 * 拿着一个元器件，准备放置在电路板上
	 */
	@JmeThread
	public void hold(ElecComp elecComp) {
//		检查当前手中时候已经有一个元器件了
		if (this.elecComp != null) {
//			如果有，就将模型删除掉，准备换成新元器件模型
			this.elecComp.getSpatial().removeFromParent();
		}
//		禁用相机的滚轮事件，留给旋转模型用。
		cameraState.setZoomEnable(false);
//		加载相应的模型
		try {
			Spatial spatial = loadAsset(new ModelKey(elecComp.getMdlPath()));
//			设置Holding的模型对鼠标透明
			JmeUtil.setMouseVisible(spatial, false);
			spatial.scale(25);
			root.attachChild(spatial);

//			设置当前元器件的模型对象
			elecComp.setSpatial(spatial);
//			记录当前的元器件对象
			this.elecComp = elecComp;
		} catch (Exception e) {
			cameraState.setZoomEnable(true);
		}
	}

	/**
	 * 放置手中的元器件
	 */
	@JmeThread
	public void putDown() {
		if (elecComp == null) {
			return;
		}
//		取消Holding的模型对鼠标透明
		JmeUtil.setMouseVisible(elecComp.getSpatial(), true);
		try {
			if (ElecComp.COMBINE_RELY_ON == elecComp.getCombine()) {
//				需要底座的元器件不可直接加入电路板
				Platform.runLater(() -> {
					AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.base.need"));
				});
				elecComp.getSpatial().removeFromParent();
			} else {
//				1、获取相应元器件
				ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
				elecCompDef.getProxy().setId(elecComp.getId());
//				2、将元器件模型与元器件对象一起加入电路板中
				circuitState.attachToCircuit(elecComp.getSpatial(), elecCompDef);
			}
		} catch (Exception e) {
//			删除出错的模型
			elecComp.getSpatial().removeFromParent();
			LOG.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
			e.printStackTrace();
		} finally {
			elecComp = null;
			cameraState.setZoomEnable(true);
		}
	}

	/**
	 * 丢弃手中的元器件
	 */
	public void discard() {
		if (elecComp == null) {
			return;
		}
		elecComp.getSpatial().removeFromParent();
		elecComp = null;
		cameraState.setZoomEnable(true);
	}

	/**
	 * 将元器件放置在指定底座上
	 * @param baseDef 指定底座
	 */
	public void putDownOnBase(ElecCompDef def) {
		if (elecComp == null || ElecComp.COMBINE_RELY_ON != elecComp.getCombine()) {
			return;
		}
//		1、获取相应元器件
		ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
		elecCompDef.getProxy().setId(elecComp.getId());
		if (def.getBase().isUseable(elecCompDef)) {
			return;
		}
		try {
//			取消Holding的模型对鼠标透明
			JmeUtil.setMouseVisible(elecComp.getSpatial(), true);
//			2、判断元器件与底座是否匹配
			if (def.getBase().checkMatched(elecCompDef)) {
//				3、将元器件模型与元器件对象一起加入电路板中
				circuitState.attachToBase(elecComp.getSpatial(), elecCompDef, def);
			} else {
				Platform.runLater(() -> {
					AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.base.unmatch"));
				});
				elecComp.getSpatial().removeFromParent();
			}
			elecComp = null;
			cameraState.setZoomEnable(true);
		} catch (Exception e) {
//			删除出错的模型
			if (elecComp != null) {
				elecComp.getSpatial().removeFromParent();
				LOG.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
			}
			e.printStackTrace();
		}
	}

	public void save() {
		if (circuitState != null) {
			circuitState.save();
		}
	}

	public CircuitState getCircuitState() {
		return circuitState;
	}

}
