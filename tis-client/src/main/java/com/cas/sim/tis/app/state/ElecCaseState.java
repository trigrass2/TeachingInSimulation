package com.cas.sim.tis.app.state;

import com.cas.circuit.component.ElecCompDef;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.SceneCameraState.Mode;
import com.cas.sim.tis.app.state.SceneCameraState.View;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.app.state.typical.HoldState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.asset.ModelKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class ElecCaseState<T>extends BaseState {
//	每次打开新的案例，都会创建一个新的节点，存放与案例相关的模型
	private static final String CASE_ROOT_NODE = "CASE_ROOT_NODE";

	protected Node root;
	protected Spatial compPlane;

	protected PointLight pointLight;

	protected SceneCameraState cameraState;

	protected ElecCase3D<T> ui;
	protected HoldState holdState;
	protected CircuitState circuitState;

	protected CaseMode mode;

	@Getter
	@AllArgsConstructor
	public enum CaseMode {
		VIEW_MODE(MsgUtil.getMessage("elec.case.mode.view"), true, false), // 查看模式（控制一步步显示元器件，导线）
		TYPICAL_TRAIN_MODE(MsgUtil.getMessage("elec.case.mode.train"), true, true), // 练习模式（教师、学生根据案例自由摆放元器件导线）
		BROKEN_TRAIN_MODE(MsgUtil.getMessage("elec.case.mode.train"), false, false), // 练习模式（教师、学生根据案例检测故障）
		BROKEN_EXAM_MODE(MsgUtil.getMessage("elec.case.mode.exam"), false, false), // 考核模式（学生根据案例检测故障）
		EDIT_MODE(MsgUtil.getMessage("elec.case.mode.edit"), false, true);// 编辑模式（教师编辑案例）

		private String name;
		private boolean hideCircuit;// 是否在初始化时隐藏电路
		private boolean holdEnable;// 模式下是否允许拿去元器件
		
		@Override
		public String toString() {
			return name;
		}
	}

	@Override
	protected void initializeLocal() {
		arrangementScene();

		bindEvents();

		// 添加操作模式State
		stateManager.attach(cameraState = new SceneCameraState());

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

		this.holdState = new HoldState(root, compPlane, cameraState);
		stateManager.attach(holdState);

		stateManager.attach(new MultimeterState());
		
		
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
		stateManager.detach(circuitState);
		stateManager.detach(holdState);
		super.cleanup();
	}

	public void switchTo2D() {
		cameraState.toggleOrthoPerspMode(Mode.Ortho);
		cameraState.switchToView(View.Top);
	}

	public void switchTo3D() {
		cameraState.toggleOrthoPerspMode(Mode.Persp);
	}

//	布置场景
	private void arrangementScene() {
		// 0、新建场景节点
		root = new Node(CASE_ROOT_NODE);
		rootNode.attachChild(root);
		// 1、加载工作台模型
		Node plane = (Node) loadAsset(new ModelKey("Model/Desktop/plane.j3o"));
		root.attachChild(plane);
		this.compPlane = plane.getChild("COMP-PLANE");
		// 2、添加灯光
		pointLight = new PointLight();
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	private void bindEvents() {
		/*
		 * 左键点击桌面，放置手中的元器件 右键点击桌面，取消手中的元器件
		 */
		addListener(compPlane, new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mode.isHoldEnable()) {
					holdState.putDown(circuitState);
				}
			}

			@Override
			public void mouseRightClicked(MouseEvent e) {
				holdState.discard();
			}
		});
	}

	public void hold(ElecComp elecComp) {
		if (mode.isHoldEnable()) {
			holdState.hold(elecComp);
		}
	}

	public void putDownOnBase(ElecCompDef def) {
		if (CaseMode.VIEW_MODE == mode || circuitState == null) {
			return;
		}
		holdState.putDownOnBase(def, circuitState);
	}

	public boolean isClean() {
		if (CaseMode.VIEW_MODE == mode || circuitState == null) {
			return true;
		} else {
			return circuitState.isClean();
		}
	}

	public abstract void setMode(CaseMode mode);

	public abstract void save();

	public abstract void newCase();

	public abstract void setupCase(T elecCase, CaseMode mode);

	public abstract T getElecCase();

	public CircuitState getCircuitState() {
		return circuitState;
	}

	public SceneCameraState getCameraState() {
		return cameraState;
	}

	public void setUI(ElecCase3D<T> ui) {
		this.ui = ui;
	}

	public ElecCase3D<T> getUI() {
		return ui;
	}

	public CaseMode getMode() {
		return mode;
	}
}
