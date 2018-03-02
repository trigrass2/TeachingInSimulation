package com.cas.sim.tis.app.state;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Terminal;
import com.cas.circuit.vo.Wire;
import com.cas.sim.tis.action.ArchiveAction;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.xml.util.JaxbUtil;
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;

import javafx.application.Platform;
import jme3tools.optimize.GeometryBatchFactory;

public class TypicalCaseState extends BaseState {

	public static final String TYPICAL_CASE_ROOT_NODE = "TYPICAL_CASE_ROOT_NODE";
	public static final String MAP_ROTATE_LEFT = "ROTATE_LEFT";
	public static final String MAP_ROTATE_RIGHT = "ROTATE_RIGHT";

	private Node root;

	private Spatial holding;
	private Spatial desktop;
	private ElecComp elecComp;

	private Map<String, ElecCompDef> taggedCompMap = new HashMap<>();
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

		circuitState = new CircuitState(root);
		stateManager.attach(circuitState);

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
		MikktspaceTangentGenerator.generate(desktop);
		root.attachChild(desktop);
	}

	public void setupCase(TypicalCase typicalCase) {
//		解析出存档对象
		Archive archive = SpringUtil.getBean(ArchiveAction.class).parse(typicalCase.getArchivePath());

		ElecCompAction action = SpringUtil.getBean(ElecCompAction.class);

//		一、开始加载元器件
		archive.getCompList().forEach(c -> {
//			1、根据元器件型号（model字段），查找数据库中的元器件实体对象
			ElecComp elecComp = action.getElecComp(c.getModel());
//			2、加载模型，同时设置好坐标与旋转
			Future<Node> task = app.enqueue((Callable<Node>) () -> {
				Spatial elecCompMdl = loadAsset(new ModelKey(elecComp.getMdlPath()));
//				设置transform信息：location、rotation
				elecCompMdl.setLocalTranslation(c.getLocation());
				elecCompMdl.setLocalRotation(c.getRotation());
				return (Node) elecCompMdl;
			});
			Node load = null;
			try {
				load = task.get();
				root.attachChild(load);
			} catch (Exception e) {
				LOG.error("无法加载元器件模型{}", elecComp.getMdlPath());
				LOG.error("错误详情", e);
				throw new RuntimeException(e);
			}
//			3、初始化元器件逻辑对象
			HTTPUtils httpUtil = SpringUtil.getBean(HTTPUtils.class);
			URL cfgPath = httpUtil.getUrl(elecComp.getCfgPath());
			ElecCompDef def = JaxbUtil.converyToJavaBean(cfgPath, ElecCompDef.class);
//			构建元器件逻辑类
//			def.build(load, c.getTagName());

			taggedCompMap.put(c.getTagName(), def);
		});
//		FIXME
////		给元器件模型加上监听
//		taggedCompMap.values().forEach(comp -> {
//			addListener(comp.getSpatial(), new TypicalCaseCompListener(comp));
////			给元器件中的连接头模型添加监听
//			comp.getTerminalList().forEach(t -> addListener(t.getSpatial(), new TypicalCaseTermianlListener(t)));
//		});

//		加载导线
		archive.getWireList().forEach(w -> {
			ElecCompDef taggedComp1 = taggedCompMap.get(w.getTagName1());
			Terminal term1 = taggedComp1.getTerminal(w.getTernimalId1());

			ElecCompDef taggedComp2 = taggedCompMap.get(w.getTagName2());
			Terminal term2 = taggedComp2.getTerminal(w.getTernimalId2());

			Wire wire = new Wire();
			wire.bind(term1);
			wire.bind(term2);

			Future<Geometry> task = app.enqueue((Callable<Geometry>) () -> {
				List<Geometry> geoList = new ArrayList<>();
				w.getPointList().forEach(a -> {
//					创建导线的某一段导线。
					Line line = new Line(a[0], a[1]);
					geoList.add(new Geometry("", line));
				});
				Mesh outMesh = new Mesh();
				GeometryBatchFactory.mergeGeometries(geoList, outMesh);
//				创建一根导线
				Geometry lineGeo = new Geometry("Wire", outMesh);
//				TODO 给导线上色、调整粗细

				return lineGeo;
			});
			try {
				task.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
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
//			2、绑定模型对象
			elecCompDef.bindModel((Node) holding);
//			3、添加事件
			circuitState.bindElecCompEvent(elecCompDef);
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

}
