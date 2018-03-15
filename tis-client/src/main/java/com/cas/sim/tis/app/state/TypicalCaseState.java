package com.cas.sim.tis.app.state;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.asset.ModelKey;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;

import javafx.application.Platform;

public class TypicalCaseState extends BaseState {

	public static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";
	public static final String MAP_ROTATE_LEFT = "ROTATE_LEFT";
	public static final String MAP_ROTATE_RIGHT = "ROTATE_RIGHT";

	private Node root;

	private Spatial holding;
	private Spatial desktop;
	private ElecComp elecComp;

	private PointLight pointLight;

	private MyCameraState cameraState;
	private boolean moved_before_putdown;

	private CircuitState circuitState;

	@Override
	protected void initializeLocal() {
		root = new Node(TYPICAL_CASE_ROOT_NODE);
		rootNode.attachChild(root);

//		一、布置场景
		arrangementScene();

		setupLight();
//		
		cameraState = new MyCameraState();
		stateManager.attach(cameraState);

//		circuitState = new CircuitState(root);
//		stateManager.attach(circuitState);

//		三、事件
		bindEvents();

		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	@Override
	public void update(float tpf) {
		pointLight.setPosition(cam.getLocation());
//		System.out.println("TypicalCaseState.update()" + root.getWorldTranslation());
		super.update(tpf);
	}

	private void setupLight() {
		pointLight = new PointLight();
//		dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	private void bindEvents() {
//		给电路板添加监听
		addMapping("HOVER_ON_DESKTOP_AXIR_X+", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		addMapping("HOVER_ON_DESKTOP_AXIR_X-", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		addMapping("HOVER_ON_DESKTOP_AXIR_Y+", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		addMapping("HOVER_ON_DESKTOP_AXIR_Y-", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		String[] hoverMapping = new String[] { "HOVER_ON_DESKTOP_AXIR_X+", "HOVER_ON_DESKTOP_AXIR_X-", "HOVER_ON_DESKTOP_AXIR_Y+", "HOVER_ON_DESKTOP_AXIR_Y-" };
		addListener((AnalogListener) (name, value, tpf) -> {
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
		}, hoverMapping);

		addMapping("CLICKED_ON_DESKTOP", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		addListener((ActionListener) (name, isPressed, tpf) -> {
			if (elecComp == null) {
				return;
			}
			if (isPressed) {
				moved_before_putdown = false;
			} else if (!moved_before_putdown) {
				putDown();
			}
		}, "CLICKED_ON_DESKTOP");

//		鼠标滚动事件
		String[] rotateMapping = new String[] { MAP_ROTATE_LEFT, MAP_ROTATE_RIGHT };
		addMapping(MAP_ROTATE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		addMapping(MAP_ROTATE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		addListener((AnalogListener) (name, value, tpf) -> {
			if (holding == null) {
				return;
			}
			if (MAP_ROTATE_LEFT.equals(name)) {
				holding.rotate(0, FastMath.DEG_TO_RAD * 90, 0);
			} else if (MAP_ROTATE_RIGHT.equals(name)) {
				holding.rotate(0, -FastMath.DEG_TO_RAD * 90, 0);
			}
		}, rotateMapping);
	}

	private void arrangementScene() {
//		1、加载桌子
		desktop = loadAsset(new ModelKey("Model/Desktop/desktop.j3o"));
		desktop.setName("Circuit-Desktop");
		MikktspaceTangentGenerator.generate(desktop);
		root.attachChild(desktop);
	}

	public void setupCase(TypicalCase typicalCase) {
//		1、清理垃圾
		if (circuitState != null) {
			stateManager.detach(circuitState);
		}
//		创建新的circuitState
		circuitState = new CircuitState(typicalCase, root);
		stateManager.attach(circuitState);

//		尝试解析出存档对象
		Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());
		if (archive == null) {
			return;
		}
		circuitState.read(archive);
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
	}

	public void createCase(String name) {

	}

	public CircuitState getCircuitState() {
		return circuitState;
	}

}
