package com.cas.sim.tis.app.state;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.cas.circuit.vo.ControlIO;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.LightIO;
import com.cas.circuit.vo.Terminal;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.AnimUtil;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.xml.util.JaxbUtil;
import com.cas.util.StringUtil;
import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
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

import javafx.application.Platform;

public class ElecCompState extends BaseState {

	private static final String ROOT_NAME = "ELEC_COMP_ROOT";
	private Node root;
	private ChaseCamera chaser;
	private boolean pickEnable;
	private boolean transparent;
	private boolean explode;
	private List<Spatial> shells = new ArrayList<>();
	private boolean autoRotate;
	private float scale = 1;
	private PointLight pointLight;
	private boolean moveable;
	private Recongnize3D ui;

	@Override
	protected void initializeLocal() {
//		认知模块的根节点
		root = new Node(ROOT_NAME);
		LOG.debug("创建元器件状态机的根节点{}", root.getName());
		rootNode.attachChild(root);

//		设定相机：
		setupCamera();

//		光源：
		setupLight();

////		PBR indirect lighting
//		final EnvironmentCamera envCam = new EnvironmentCamera(256, new Vector3f(0, 3f, 0));
//		stateManager.attach(envCam);
//
//		stateManager.attach(new BreakPointState((a) -> {
//			LightProbe probe = LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode, new JobProgressAdapter<LightProbe>() {
//				@Override
//				public void done(LightProbe result) {
////					加载结束
//					Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
//				}
//			});
//			((BoundingSphere) probe.getBounds()).setRadius(100);
//			rootNode.addLight(probe);
//		}));

//		加载结束
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	private void setupLight() {
		pointLight = new PointLight();
//		dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	private void setupCamera() {
//		1、禁用飞行视角
		app.getFlyByCamera().setEnabled(false);
//		2、启动跟随视角
		chaser = new ChaseCamera(cam, root, inputManager);
		chaser.setHideCursorOnRotate(false);
//		3、设置垂直翻转
		chaser.setInvertVerticalAxis(true);
//		4、设置最大和最小仰角
		chaser.setMaxVerticalRotation(FastMath.DEG_TO_RAD * 80);
		chaser.setMinVerticalRotation(-FastMath.DEG_TO_RAD * 80);
//		5、设置缩放与旋转的灵敏度
		chaser.setZoomSensitivity(1);
		chaser.setRotationSpeed(5);
//		6、移除用于旋转相机的鼠标右键触发器
		inputManager.deleteTrigger(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
//		7、模拟一个拖拽事件，通过修改相机观测点的偏移量实现的。
		dragEvent();
	}

	private void dragEvent() {
//		鼠标右键按下才能拖拽
		addMapping("BTN_MOVE", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		addListener((ActionListener) (name, isPressed, tpf) -> {
			moveable = isPressed;
			if (isPressed) {
//				@Nullable
//				Geometry picked = JmeUtil.getGeometryFromCursor(root, cam, inputManager);
//				moveable = picked != null;
//
////				XXX for test
//				@Nullable
				Vector3f point = JmeUtil.getContactPointFromCursor(root, cam, inputManager);
				if (point != null) {
					Spatial ball = JmeUtil.getSphere(assetManager, 32, 0.1f, ColorRGBA.Red);
					root.attachChild(ball);
					ball.setLocalTranslation(point);
				}
//			} else {
//				moveable = false;
			}
		}, "BTN_MOVE");

//		鼠标拖拽的四个方向
		addMapping("AXIS_X_RIGHT", new MouseAxisTrigger(MouseInput.AXIS_X, true));// 右
		addMapping("AXIS_X_LEFT", new MouseAxisTrigger(MouseInput.AXIS_X, false));// 左
		addMapping("AXIS_Y_UP", new MouseAxisTrigger(MouseInput.AXIS_Y, true));// 上
		addMapping("AXIS_Y_DOWN", new MouseAxisTrigger(MouseInput.AXIS_Y, false));// 下
		addListener((AnalogListener) (name, value, tpf) -> {
			if (!moveable) {
				return;
			}
			value *= 10;

			if ("AXIS_X_LEFT".equals(name)) {
				chaser.setLookAtOffset(chaser.getLookAtOffset().add(cam.getLeft().normalize().mult(value)));
			}
			if ("AXIS_X_RIGHT".equals(name)) {
				chaser.setLookAtOffset(chaser.getLookAtOffset().add(cam.getLeft().normalize().mult(value).negate()));
			}
			if ("AXIS_Y_UP".equals(name)) {
				chaser.setLookAtOffset(chaser.getLookAtOffset().add(cam.getUp().normalize().mult(value)));
			}
			if ("AXIS_Y_DOWN".equals(name)) {
				chaser.setLookAtOffset(chaser.getLookAtOffset().add(cam.getUp().normalize().mult(value).negate()));
			}
		}, "AXIS_X_LEFT", "AXIS_X_RIGHT", "AXIS_Y_UP", "AXIS_Y_DOWN");
	}

	@JmeThread
	public void setElecComp(ElecComp elecComp) {
//		加载模型
		loadModel(elecComp.getMdlPath());

//		获取相应元器件
		ElecCompDef elecCompDef = JaxbUtil.converyToJavaBean(SpringUtil.getBean(HTTPUtils.class).getUrl(elecComp.getCfgPath()), ElecCompDef.class);

//		找出元器件外壳模型名称
		loadShell(elecCompDef.getParam(ElecCompDef.PARAM_KEY_SHELL));

//		FIXME 这里应更为精准地设置为元器件模型。
		elecCompDef.bindModel(root);

//		添加事件
		Map<Spatial, String> nameMap = collectName(elecCompDef);

		nameMap.entrySet().forEach(e -> addListener(e.getKey(), new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (!pickEnable) {
					return;
				}
				System.out.println("ElecCompState.setElecComp(...).new MouseEventAdapter() {...}.mouseClicked()");
				Vector3f point = cam.getScreenCoordinates(evt.getContactPoint());
				Platform.runLater(() -> ui.showName(e.getValue(), point.getX(), point.getY()));
			}
		}));
	}

	private Map<Spatial, String> collectName(ElecCompDef elecCompDef) {
//		绑定连接头
		Map<Spatial, String> nameMap = new HashMap<>();

//		连接头
		Map<Spatial, String> terminalMap = elecCompDef.getTerminalList().stream().collect(Collectors.toMap(Terminal::getSpatial, Terminal::getName));
		nameMap.putAll(terminalMap);

//		线缆插孔
		Map<Spatial, String> jackMap = elecCompDef.getJackList().stream().collect(Collectors.toMap(Jack::getSpatial, Jack::getName));
		nameMap.putAll(jackMap);

		elecCompDef.getMagnetismList().forEach(m -> {
//			按钮
			Map<Spatial, String> controlMap = m.getControlIOList().stream().collect(Collectors.toMap(ControlIO::getSpatial, ControlIO::getName));
			nameMap.putAll(controlMap);
//			指示灯
			Map<Spatial, String> lightMap = m.getLightIOList().stream().collect(Collectors.toMap(LightIO::getSpatial, LightIO::getName));
			nameMap.putAll(lightMap);
		});
//		指示灯
		Map<Spatial, String> lightMap = elecCompDef.getLightIOList().stream().collect(Collectors.toMap(LightIO::getSpatial, LightIO::getName));
		nameMap.putAll(lightMap);

		return nameMap;
	}

//	@param path 模型路径
	private void loadModel(String path) {
//		clean up
		shells.clear();
		LOG.debug("移除{}中所有模型", root.getName());
		root.detachAllChildren();

//		检查参数
		if (path == null) {
			return;
		}

//		加载模型
		Spatial model = null;
		try {
			model = assetManager.loadModel(path);
			LOG.debug("加载模型{}", path);
		} catch (Exception e) {
			LOG.warn(MessageFormat.format("加载模型失败{0}", path), e);
			throw e;
		}

//		FIXME 调用这一句,
//		MikktspaceTangentGenerator.generate(model);

//		将模型放大100倍
		model.scale(100);
		root.attachChild(model);
//		
		explode0();
	}

//	@param shell 元器件的外壳（由1个或多个节点组成）
	private void loadShell(String shell) {
		if (shell == null) {
			return;
		}
		StringUtil.split(shell, ',').forEach(s -> {
//			找出元器件外壳模型
			Spatial shellNode = root.getChild(s);
			if (shellNode != null) {
				shells.add(shellNode);
			} else {
				LOG.error("模型{}中没有名为{}的节点", root, s);
			}
		});

		transparentShell0();
	}

	@Override
	public void cleanup() {
		boolean result = root.removeFromParent();
		if (result) {
			LOG.debug("删除根节点{}", root.getName());
		} else {
			LOG.warn("删除根节点{}失败", root.getName());
		}

		shells.clear();
		chaser.cleanupWithInput(inputManager);
		super.cleanup();
	}

	@Override
	public void update(float tpf) {
		if (autoRotate) {
			root.rotate(0, tpf / 2, 0);
		}
		pointLight.setPosition(cam.getLocation());
		root.setLocalScale(scale);
		super.update(tpf);
	}

	public void explode(@NotNull Boolean n) {
		this.explode = n.booleanValue();
		explode0();
	}

	@JmeThread
	private void explode0() {
		if (explode) {
			AnimUtil.simplePlay(root);
		} else {
			AnimUtil.animReset(root);
		}
	}

	public void transparent(@NotNull Boolean n) {
		this.transparent = n.booleanValue();
		app.enqueue(() -> {
			transparentShell0();
		});
	}

	@JmeThread
	private void transparentShell0() {
		if (transparent) {
			shells.forEach(shell -> JmeUtil.transparent(shell, .7f));
		} else {
			shells.forEach(shell -> JmeUtil.untransparent(shell));
		}
	}

	public void setNameVisible(@NotNull Boolean n) {
		this.pickEnable = n.booleanValue();
	}

	public void autoRotate(Boolean n) {
		this.autoRotate = n.booleanValue();
	}

	public void reset() {
		chaser.setLookAtOffset(Vector3f.ZERO);
		scale = 1;
	}

	public void zoomIn() {
		scale *= 1.1;
	}

	public void zoomOut() {
		scale /= 1.1;
	}

	public void setUI(Recongnize3D ui) {
		this.ui = ui;
	}
}
