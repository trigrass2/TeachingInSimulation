package com.cas.sim.tis.app.state;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.ElecCompProxy;
import com.cas.circuit.component.RelyOn;
import com.cas.circuit.util.JmeUtil;
import com.cas.circuit.util.Util;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.app.listener.TypicalCaseListener;
import com.cas.sim.tis.app.state.SceneCameraState.Mode;
import com.cas.sim.tis.app.state.SceneCameraState.View;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.asset.ModelKey;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;

/**
 * 典型案例模块
 * @author Administrator
 */
@Slf4j
public class TypicalCaseState extends BaseState {
//	每次打开新的案例，都会创建一个新的节点，存放与案例相关的模型
	private static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";

	private Node root;

//	当前手中的元器件对象

	ElecComp elecComp;
	ElecCompDef elecCompDef;

	private PointLight pointLight;

	private SceneCameraState cameraState;

	private CircuitState circuitState;
	private TypicalCaseListener listener;

	private TypicalCase typicalCase;
	/*
	 * 元器件所在的平面
	 */
	private Spatial compPlane;

	private TypicalCase3D ui;

	@Override
	protected void initializeLocal() {
		// 布置场景
		arrangementScene();

		// 添加操作模式State
		stateManager.attach(cameraState = new SceneCameraState());

//		cam.setLocation(new Vector3f(0.023734434f, 0.52777225f, 0.37514582f));
//		cam.setRotation(new Quaternion());
		// 绑定事件
		bindEvents();

		addMapping("toggleView", new KeyTrigger(KeyInput.KEY_CAPITAL));
		addListener((ActionListener) (name, pressed, value) -> {
			if ("toggleView".equals(name) && pressed) {
				if (cameraState.getMode() == Mode.Ortho) {
					switchTo3D();
				} else {
					switchTo2D();
				}
			}
		}, "toggleView");

		// 默认新建案例
		setupCase(new TypicalCase());
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

	public boolean isClean() {
		return circuitState.isClean();
	}

//	打开一个案例
	public void setupCase(TypicalCase typicalCase) {
		this.typicalCase = typicalCase;
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 创建新的circuitState
		circuitState = new CircuitState(this, typicalCase, root);
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			@Nullable
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
			Optional.ofNullable(archive).ifPresent(a -> circuitState.read(a));
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
	}

//	布置场景
	private void arrangementScene() {
		// 0、新建场景节点
		root = new Node(TYPICAL_CASE_ROOT_NODE);
		rootNode.attachChild(root);
		// 1、加载工作台模型
//		Spatial desktop = loadAsset(new ModelKey("Model/ShiXunZhuo/ShiXunZhuo.j3o"));
//		root.attachChild(desktop);
//		
		Node plane = (Node) loadAsset(new ModelKey("Model/Desktop/plane.j3o"));
		root.attachChild(plane);
//		1、元器件摆放的模型
		this.compPlane = plane.getChild("COMP-PLANE");

		// 2、添加灯光
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
		addListener(compPlane, new MouseEventAdapter() {
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
		Vector3f contactPoint = JmeUtil.getContactPointFromCursor(compPlane, cam, inputManager.getCursorPosition());
		if (contactPoint == null) {
			return;
		}
		elecComp.getSpatial().setLocalTranslation(new Vector3f(//
				Util.round(contactPoint.x, 3, 5), //
				contactPoint.y, //
				Util.round(contactPoint.z, 3, 5)//
		));
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
			MouseEventState.setMouseVisible(spatial, false);
//			spatial.scale(25);
			root.attachChild(spatial);

//			设置当前元器件的模型对象
			elecComp.setSpatial(spatial);
//			记录当前的元器件对象
			this.elecComp = elecComp;

			this.elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
			this.elecCompDef.setProxy(new ElecCompProxy());
			this.elecCompDef.getProxy().setId(elecComp.getId());
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

		CollisionResults results = new CollisionResults();
		circuitState.getRootCompNode().collideWith(elecComp.getSpatial().getWorldBound(), results);
		if (results.size() > 0) {
			Platform.runLater(() -> AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.collision")));
			return;
		}

		try {
			if (ElecComp.COMBINE_RELY_ON == elecComp.getCombine()) {
//				需要底座的元器件不可直接加入电路板
				Platform.runLater(() -> {
					AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.base.need"));
				});
				elecComp.getSpatial().removeFromParent();
			} else {
//				1、获取相应元器件
//				ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
//				elecCompDef.setProxy(new ElecCompProxy());
//
//				elecCompDef.getProxy().setId(elecComp.getId());
//				2、将元器件模型与元器件对象一起加入电路板中
				circuitState.attachToCircuit(elecComp.getSpatial(), elecCompDef);
			}
//			取消Holding的模型对鼠标透明
			MouseEventState.setMouseVisible(elecComp.getSpatial(), true);
		} catch (Exception e) {
//			删除出错的模型
			elecComp.getSpatial().removeFromParent();
			log.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
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
//		ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
//		elecCompDef.setProxy(new ElecCompProxy());
//
//		elecCompDef.getProxy().setId(elecComp.getId());
		if (def.getBase().isUseable(elecCompDef)) {
			return;
		}
		try {
//			取消Holding的模型对鼠标透明
			MouseEventState.setMouseVisible(elecComp.getSpatial(), true);
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
				log.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
			}
			e.printStackTrace();
		}
	}

	public void relayOnBase(ElecCompDef base) {
		if (TypicalCaseState.this.elecComp == null) {
			return;
		}
		if (base.getBase().checkMatched(elecCompDef)) {
			RelyOn relyOn = elecCompDef.getRelyOn();
			Node baseMdl = base.getSpatial();
			Vector3f loc = baseMdl.getLocalTranslation().add(relyOn.getTranslation());
			TypicalCaseState.this.elecComp.getSpatial().setLocalTranslation(loc);
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

	public TypicalCase getTypicalCase() {
		return typicalCase;
	}

	public SceneCameraState getCameraState() {
		return cameraState;
	}

	public void switchTo2D() {
		cameraState.toggleOrthoPerspMode(Mode.Ortho);
		cameraState.switchToView(View.Top);
	}

	public void switchTo3D() {
		cameraState.toggleOrthoPerspMode(Mode.Persp);
	}

	public void setUI(TypicalCase3D ui) {
		this.ui = ui;
	}

	public TypicalCase3D getUi() {
		return ui;
	}
}
