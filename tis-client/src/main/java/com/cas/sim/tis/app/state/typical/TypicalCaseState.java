package com.cas.sim.tis.app.state.typical;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.BaseState;
import com.cas.sim.tis.app.state.SceneCameraState;
import com.cas.sim.tis.app.state.SceneCameraState.Mode;
import com.cas.sim.tis.app.state.SceneCameraState.View;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
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

/**
 * 典型案例模块
 * @author Administrator
 */
public class TypicalCaseState extends BaseState {
//	每次打开新的案例，都会创建一个新的节点，存放与案例相关的模型
	private static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";

	private Node root;
	private Spatial compPlane;

	private PointLight pointLight;

	private SceneCameraState cameraState;

	private HoldState holdState;

	private TypicalCase typicalCase;

	private TypicalCase3D ui;

	public enum CaseMode {
		VIEW_MODE(MsgUtil.getMessage("typical.case.mode.view")), // 查看模式（控制一步步显示元器件，导线）
		TRAIN_MODE(MsgUtil.getMessage("typical.case.mode.train")), // 练习模式（教师、学生根据案例自由摆放元器件导线）
		EDIT_MODE(MsgUtil.getMessage("typical.case.mode.edit"));// 编辑模式（教师编辑案例）

		private String name;

		private CaseMode(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private CaseMode mode;

	private CircuitState circuitState;

	@Override
	protected void initializeLocal() {
		// 布置场景
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

		// 结束加载界面
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	private void bindEvents() {
		/*
		 * 左键点击桌面，放置手中的元器件 右键点击桌面，取消手中的元器件
		 */
		addListener(compPlane, new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (CaseMode.TRAIN_MODE != mode) {
					holdState.putDown(circuitState);
				}
			}

			@Override
			public void mouseRightClicked(MouseEvent e) {
				holdState.discard();
			}
		});
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
		super.cleanup();
	}

	public void newCase() {
		TypicalCase typicalCase = new TypicalCase();
		typicalCase.setName("新建案例 *");
		setupCase(typicalCase, CaseMode.EDIT_MODE);
	}

//	打开一个案例（管理员、教师、学生）
	public void setupCase(TypicalCase typicalCase, CaseMode mode) {
		this.typicalCase = typicalCase;
		setMode(mode);
		Platform.runLater(() -> {
			ui.setTitle(typicalCase.getName());
			// 结束加载界面
			SpringUtil.getBean(PageController.class).hideLoading();
		});
	}

//	布置场景
	private void arrangementScene() {
		// 0、新建场景节点
		root = new Node(TYPICAL_CASE_ROOT_NODE);
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

	public TypicalCase getTypicalCase() {
		return typicalCase;
	}

	public SceneCameraState getCameraState() {
		return cameraState;
	}

	public CircuitState getCircuitState() {
		return circuitState;
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

	public TypicalCase3D getUI() {
		return ui;
	}

	public void setMode(CaseMode mode) {
		// 1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
		// 查看模式时，不可拿去元器件
		holdState.setEnabled(CaseMode.VIEW_MODE != mode);
		// 创建新的circuitState
		circuitState = new CircuitState(this, holdState, typicalCase, root);
		circuitState.setOnInitialized((n) -> {
			// 尝试解析出存档对象
			@Nullable
			Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
			Optional.ofNullable(archive).ifPresent(a -> {
				circuitState.read(a, mode);
				if (CaseMode.TRAIN_MODE == mode) {
					TrainState trainState = new TrainState(ui, circuitState.getSteps(), circuitState.getRootCompNode());
					stateManager.attach(trainState);
					Platform.runLater(() -> ui.loadSteps(circuitState.getSteps()));
				} else if (CaseMode.VIEW_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
					Platform.runLater(() -> ui.loadSteps(circuitState.getSteps()));
				} else if (CaseMode.EDIT_MODE == mode) {
					stateManager.detach(stateManager.getState(TrainState.class));
				}
			});
			// 结束加载界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		});
		stateManager.attach(circuitState);
		this.mode = mode;
	}

	public void hold(ElecComp elecComp) {
		if (CaseMode.VIEW_MODE != mode) {
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

	public void save() {
		if (circuitState == null) {
			return;
		}
		circuitState.save();
	}

	public void autoComps(boolean layout) {
		if (circuitState == null) {
			return;
		}
		circuitState.autoComps(layout);
	}

	public void autoWires(boolean routing) {
		if (circuitState == null) {
			return;
		}
		circuitState.autoWires(routing);
	}
}
