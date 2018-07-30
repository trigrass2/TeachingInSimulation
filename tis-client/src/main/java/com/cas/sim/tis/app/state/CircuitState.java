package com.cas.sim.tis.app.state;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.CirSim;
import com.cas.circuit.component.Base;
import com.cas.circuit.component.ControlIO;
import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.ElecCompProxy;
import com.cas.circuit.component.RelyOn;
import com.cas.circuit.component.Terminal;
import com.cas.circuit.component.Wire;
import com.cas.circuit.component.WireProxy;
import com.cas.circuit.element.CircuitElm;
import com.cas.circuit.util.JaxbUtil;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.control.ShowNameOnHoverControl;
import com.cas.sim.tis.app.control.TagNameControl;
import com.cas.sim.tis.app.control.WireNumberControl;
import com.cas.sim.tis.app.event.MouseEventListener;
import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.app.listener.ControlIOClickListener;
import com.cas.sim.tis.app.listener.ControlIOPressListener;
import com.cas.sim.tis.app.listener.ControlIOWheelListener;
import com.cas.sim.tis.app.listener.ElecCompBaseClickListener;
import com.cas.sim.tis.app.listener.ElecCompClickListener;
import com.cas.sim.tis.app.listener.TerminalListener;
import com.cas.sim.tis.app.listener.WireListener;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.util.StringUtil;
import com.jme3.asset.ModelKey;
import com.jme3.collision.CollisionResult;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CircuitState extends BaseState {
//	导线工连接头接出的最小长度
	private static final float minLen = 0.01f;

	private static final String COMP_ROOT = "comp_root_in_circuit_state";
	private static final String WIRE_ROOT = "wire_root_in_circuit_state";
//	导线颜色
	private static ColorRGBA color = ColorRGBA.Yellow;
//	导线半径
	private static float width = 3f * 2 / 20000;

	private final ScheduledExecutorService CIRCUIT_SERVICE = Executors.newSingleThreadScheduledExecutor();

//	接线的3中状态
	static enum State {
		Starting, Mid, Ending
	}

	private Node root;
	private Node tmpWireNode;
	private Wire wire;
	private List<Wire> wireList = new ArrayList<>();

	private State state;

	private Line startLine1;
	private Line startLine2;
	private Line startLine3;

	private Line midLine1;
	private Line midLine2;

	private Vector3f dir;
	private Vector3f midAxis;
	Quaternion roll90 = new Quaternion().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

	private TypicalCaseState caseState;
	private TypicalCase typicalCase;
	private List<ElecCompDef> compList = new ArrayList<>();

	@Nullable
	private CollisionResult collision;

	/*
	 * 导线的水平面，这是一个看不到的模型。 这个模型略高于桌面，目的是为了不让导线与桌面表面重叠
	 */
	private Spatial wirePlane;

	private Node rootCompNode;
	private Node rootWireNode;

	private Node guiNode;
	private BitmapFont tagFont;

	private boolean moved;

	private CirSim cirSim;
	// Key:BASE UUID,Value: TOP UUID
//	private Map<String, String> combineMap = new HashMap<String, String>();

//	事件监听
	private MouseEventListener wireListener, //
			terminalLitener, //
			controlIOWheelListener, //
			controlIOPressListener, //
			controlIOClickListener, //
			elecCompRightClickListener, //
			elecCompBaseClickListener; //

	private Geometry elecCompBox;

	public CircuitState(TypicalCaseState caseState, TypicalCase typicalCase, Node root) {
		this.typicalCase = typicalCase;
		this.caseState = caseState;
		this.root = root;
	}

	public static void setColor(ColorRGBA color) {
		CircuitState.color = color;
	}

	public static void setWidth(float width) {
//		考虑模型的显示比例。
		CircuitState.width = width / 20000;
	}

	@Override
	protected void initializeLocal() {
		wireListener = new WireListener();
		terminalLitener = new TerminalListener(this);
		controlIOWheelListener = new ControlIOWheelListener();
		controlIOPressListener = new ControlIOPressListener();
		controlIOClickListener = new ControlIOClickListener();
		elecCompBaseClickListener = new ElecCompBaseClickListener(caseState);

//		默认尺寸
		elecCompBox = JmeUtil.getWiringBox(assetManager, Vector3f.ZERO, Vector3f.UNIT_XYZ);
//		默认隐藏
		elecCompBox.setCullHint(CullHint.Always);
//		对鼠标不可见
		MouseEventState.setMouseVisible(elecCompBox, false);
		root.attachChild(elecCompBox);
		elecCompRightClickListener = new ElecCompClickListener(elecCompBox, caseState.getCameraState());

//		存放元器件的根节点
		rootCompNode = new Node(COMP_ROOT);
		root.attachChild(rootCompNode);
//		
		rootCompNode.addControl(new ShowNameOnHoverControl((name) -> {
			Vector2f pos = inputManager.getCursorPosition();
			Platform.runLater(() -> caseState.getUi().showName(name, pos.x, pos.y));
		}, inputManager, cam));

//		存放导线的根节点
		rootWireNode = new Node(WIRE_ROOT);
//		设置导线始终显示
		rootWireNode.setCullHint(CullHint.Never);
		root.attachChild(rootWireNode);

//		2、排布导线所在的模型
		this.wirePlane = root.getChild("WIRE-PLANE");
//		wirePlane这个模型是指用于接线过程中，本身也是不可见的模型，这里是为了避免影响其它模型的点击事件
		MouseEventState.setMouseVisible(wirePlane, false);

//		显示元器件标签名
		guiNode = app.getGuiNode();
		tagFont = assetManager.loadFont("font/Font4TagName.fnt");

		bindCircuitBoardEvents();

		log.info("CircuitState完成初始化");

		cirSim = new CirSim(app);
		CircuitElm.initClass(cirSim);
		CIRCUIT_SERVICE.scheduleAtFixedRate(cirSim, 0, (long) (1 / CirSim.TPF / 10), TimeUnit.NANOSECONDS);
	}

	private void bindCircuitBoardEvents() {
//		给电路板添加监听
		addMapping("CONNECT_ON_DESKTOP_AXIR_X+", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		addMapping("CONNECT_ON_DESKTOP_AXIR_X-", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		addMapping("CONNECT_ON_DESKTOP_AXIR_Y+", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		addMapping("CONNECT_ON_DESKTOP_AXIR_Y-", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		String[] hoverMapping = new String[] { "CONNECT_ON_DESKTOP_AXIR_X+", "CONNECT_ON_DESKTOP_AXIR_X-", "CONNECT_ON_DESKTOP_AXIR_Y+", "CONNECT_ON_DESKTOP_AXIR_Y-" };
		addListener((AnalogListener) (name, value, tpf) -> onAnalog0(name, value, tpf), hoverMapping);

		addMapping("CLICKED_ON_BOARD", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		addMapping("CANCEL_ON_CONNECTING", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		addMapping("DELETE_SELECTED_WIRE", new KeyTrigger(KeyInput.KEY_DELETE));
		addListener((ActionListener) (name, isPressed, tpf) -> onAction0(name, isPressed, tpf), "CLICKED_ON_BOARD", "CANCEL_ON_CONNECTING", "DELETE_SELECTED_WIRE");
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
//		long start = System.currentTimeMillis();
//		cirSim.updateCircuit(tpf / 2000);
//		System.out.println(System.currentTimeMillis() - start);

//		CircuitElm e = cirSim.getCircuitElm(2);
//		String[] info = new String[8];
//		e.getInfo(info);
//		System.out.println(Arrays.toString(info));
	}

	/*
	 * 鼠标在导线平面上点击
	 */
	private void onAction0(String name, boolean isPressed, float tpf) {
		if (isPressed) {
//			在鼠标按下 与 抬起的过程中，使用moved变量记录在这个过程中鼠标是否移动过。
//			当鼠标按下后，设置moved标记为false，表示没有移动过
//			如果analog0方法被执行，则会改变moved状态为true，表示鼠标移动过,则视为点击事件click无效。
			moved = false;
		}

		if ("CLICKED_ON_BOARD".equals(name) && !isPressed && !moved) {
//			走线
			if (state == null) {
				return;
			}
			if (state == State.Starting) {
				connectStarting();
//				设置当前接线状态为接线中
				state = State.Mid;
			} else if (state == State.Mid) {
				connectMid();
			} else if (state == State.Ending) {
//				do nothing
			}
		} else if ("CANCEL_ON_CONNECTING".equals(name) && isPressed) {
			if (state == State.Starting || state == State.Mid) {
				connectClean();
			}
//			取消（接线）
		} else if ("DELETE_SELECTED_WIRE".equals(name) && isPressed) {
			WireListener listener = (WireListener) wireListener;
			Spatial selectedWireToDelete = listener.getSelectedWire();
			if (selectedWireToDelete == null) {
				return;
			}
			Wire wire = selectedWireToDelete.getUserData("entity");
			detachFromCircuit(wire);
		} else {
//			do nothing
		}
	}

	/*
	 * 鼠标在导线平面上滑过
	 */
	private void onAnalog0(String name, float value, float tpf) {
		if (state == null) {
			return;
		}

		moved = true;

//		collision = JmeUtil.getCollisionFromCursor(root, cam, inputManager.getCursorPosition());
		collision = JmeUtil.getCollisionFromCursor(root, cam, inputManager.getCursorPosition());
		if (collision == null) {
			return;
		}
		Vector3f point = collision.getContactPoint();

		if (state == State.Starting) {
//			开头
			Vector3f project = point.subtract(startLine1.getStart()).project(dir);
			if (project.dot(dir) < minLen) {
				return;
			}
			Vector3f line1EndPoint = startLine1.getStart().add(project);
			startLine1.updatePoints(startLine1.getStart(), line1EndPoint);

			Vector3f line2EndPoint = line1EndPoint.add(point.subtract(line1EndPoint).project(Vector3f.UNIT_Y));
			startLine2.updatePoints(line1EndPoint, line2EndPoint);

//			Vector3f line3EndPoint = point;
			startLine3.updatePoints(line2EndPoint, point);
		} else if (state == State.Mid) {
//			计算midLine1.getStart() 与  鼠标point 向量在X与Z轴的投影。
			Vector3f tmpPoint = null;
			tmpPoint = midLine1.getStart().add(point.subtract(midLine1.getStart()).project(midAxis));
			midLine1.updatePoints(midLine1.getStart(), tmpPoint);
			midLine2.updatePoints(tmpPoint, point);
		} else if (state == State.Ending) {
//			endLine1.updatePoints(endLine1.getStart(), point);
		}
	}

	public void onTernialClick(Terminal t) {
		if (t.getWireSize() >= t.getLimit()) {
			Platform.runLater(() -> AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.wire.max")));
			return;
		}

		if (state == null || state == State.Ending) {
			connectPre(t);
//			设置接线状态为开始
			state = State.Starting;
		} else if (state == State.Mid) {
			if (t.getWires().contains(wire)) {
				Platform.runLater(() -> AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.terminal.self")));
				return;
			}
			connectEnding(t);
			state = State.Ending;
		} else if (state == State.Ending) {
//			do nothing
		}
	}

//	点击连接头、开始接线之前的准备工作
	private void connectPre(Terminal t) {
		// 创建一个节点，存放当前一根导线的模型
		tmpWireNode = new Node();
		// 将导线模型添加到root节点中以显示
		rootWireNode.attachChild(tmpWireNode);
		// 创建一个导线逻辑对象
		wire = new Wire();
		// 导线颜色和线宽
		wire.getProxy().setColor(color.clone());
		wire.getProxy().setWidth(width);

		// 导线一头连接在当前连接头上
		wire.bind(t);

//		获取导线连接的方向
		dir = t.getElecCompDef().getSpatial().getLocalRotation().mult(t.getDirection());
		midAxis = roll90.mult(dir);

//		导线连接的位置，多跟导线的情况下错开
		Vector3f dest = new Vector3f();
		if (t.getWireSize() == t.getLimit()) {
//			第二根线
			dest.set(t.getSpatial().getWorldTranslation().add(midAxis.normalize().mult(0.002f)));
		} else {
//			第一根线
			dest.set(t.getSpatial().getWorldTranslation().add(midAxis.normalize().mult(0.002f).negate()));
		}

//		前三段导线
//		
		Vector3f second = dest.add(dir.mult(minLen));

		startLine1 = new Line(dest, second);//
		startLine2 = new Line(second, second);
		startLine3 = new Line(second, second);

		tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, startLine1, color));
		tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, startLine2, color));
		tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, startLine3, color));
	}

	//
	private void connectStarting() {
		Vector3f startPoint = JmeUtil.getLineStart(startLine3);
		Vector3f endPoint = JmeUtil.getLineEnd(startLine3);

		midLine1 = startLine3;
		midLine1.getStart().set(startPoint);
//		midLine1.getEnd().set(arr[3], arr[4], arr[5]);
		midLine2 = new Line(startPoint, endPoint);
		tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, midLine2, color));

		startLine1 = null;
		startLine2 = null;
		startLine3 = null;
	}

	private void connectMid() {
		// midLine1 连接好，不再修改
		// 将之前的midLine2作为新的midLine1
		// 获取midLine2的起始点
		Vector3f startPoint = JmeUtil.getLineStart(midLine2);
		Vector3f endPoint = JmeUtil.getLineEnd(midLine2);

		midLine1 = midLine2;
		midLine1.getStart().set(startPoint);
		//
		midLine2 = new Line(startPoint, endPoint);
		tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, midLine2, color));

		midAxis = roll90.mult(midAxis);
	}

	private void connectEnding(Terminal t) {
//		重点线头连出的方向
		dir = t.getElecCompDef().getSpatial().getLocalRotation().mult(t.getDirection());
//		终点
		Vector3f dest = new Vector3f();

		Vector3f axis = roll90.mult(dir);
//		FIXME 导线数量
		if (t.getWireSize() == 1) {
//			第二根线
			dest.set(t.getSpatial().getWorldTranslation().add(axis.normalize().mult(0.002f)));
		} else {
//			第一根线
			dest.set(t.getSpatial().getWorldTranslation().add(axis.normalize().mult(0.002f).negate()));
		}

		// ##最终是在终点dest和startPoint之间创建导线##
		Vector3f startPoint = JmeUtil.getLineStart(midLine1);

		// 判断最后一段线与dir的向量积，从而知道是哪一种连接方式
		Vector3f t1 = JmeUtil.getLineStart(midLine2);
		Vector3f t2 = JmeUtil.getLineEnd(midLine2);

		if (t2.subtract(t1).normalize().dot(dir) == 0) {
			// 表明是垂直的方向连接到终点的，这种情况稍微复杂一些
			// 倒数第二个点是终点在dir方向上的点，距离终点的长度是minLen
			Vector3f last2 = dest.add(dir.normalize().mult(minLen));
			// 倒数第三个点是last2在电路板子上的投影点，y的值可以从取startPoint起点的y
			Vector3f last3 = last2.clone().setY(startPoint.getY());
			// 倒数第四个点是last3在midLine1上的投影点
			Vector3f endPoint = JmeUtil.getLineEnd(midLine1);
			Vector3f last4 = startPoint.add(last3.subtract(startPoint).project(endPoint.subtract(startPoint)));

			// 四个点全部找齐，下面把midLine1和midLine2的重新设置
			midLine1.updatePoints(midLine1.getStart(), last4);
			midLine2.updatePoints(last4, last3);
			// 另外，还需要两根线
			tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, new Line(last3, last2), color));
			tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, new Line(last2, dest), color));

			Platform.runLater(() -> AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.wire.rule")));
		} else {
			// 倒数第二个点是midLine1上任意一点在dir上的投影点，这里取startPoint
			Vector3f last2 = dest.add(startPoint.subtract(dest).project(dir));
			// 倒数第三个点是last2在电路板子上的投影点，y的值可以从取startPoint起点的y
			Vector3f last3 = last2.clone().setY(startPoint.getY());
			// 三个点全部找齐，下面把midLine1和midLine2的重新设置
//			midLine1.getStart().set(JmeUtil.getLineStart(midLine1));
//			midLine1.getEnd().set(last3);
			midLine1.updatePoints(last3, midLine1.getStart());

//			midLine2.getStart().set(last3);
//			midLine2.getEnd().set(last2);
			midLine2.updatePoints(last3, last2);

//			Geometry ball = JmeUtil.getSphere(assetManager, 6, width * 2, ColorRGBA.Red);
//			ball.setLocalTranslation(last3);
//			rootNode.attachChild(ball);
//
//			Geometry ball2 = JmeUtil.getSphere(assetManager, 6, width * 2, ColorRGBA.Green);
//			ball2.setLocalTranslation(JmeUtil.getLineStart(midLine1));
//			rootNode.attachChild(ball2);

			// 另外，还需要一根线
			tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, new Line(last2, dest), color));
		}

		wire.bind(t);
		List<Vector3f> pointList = getPointList(tmpWireNode);
		wire.getProxy().getPointList().addAll(pointList);

		Geometry realWire = JmeUtil.createCylinderLine(assetManager, pointList, width, color);
		attachToCircuit(realWire, wire);

		tmpWireNode.removeFromParent();
		wire = null;
		tmpWireNode = null;

		connectClean();
	}

	private void connectClean() {
		state = null;
		dir = null;
		startLine1 = startLine2 = startLine3 = null;
		midLine1 = midLine2 = null;

		if (tmpWireNode != null) {
			tmpWireNode.removeFromParent();
			tmpWireNode = null;
		}

		// 清理临时接线连接头
		if (wire != null) {
			wire.unbind();
			wire = null;
		}
	}

	// 获取当前导线的点坐标集合
	@Nonnull
	private List<Vector3f> getPointList(Node wireNode) {
		List<Vector3f> result = new ArrayList<>();

		List<Spatial> children = wireNode.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Geometry geo = (Geometry) children.get(i);
			Line mesh = (Line) geo.getMesh();
			Vector3f point = JmeUtil.getLineStart(mesh);
			if (!result.contains(point)) {
				result.add(point);
			}
		}
//		最后一个点
		Geometry geo = (Geometry) children.get(children.size() - 1);
		Line mesh = (Line) geo.getMesh();

		Vector3f point = JmeUtil.getLineEnd(mesh);
		if (!result.contains(point)) {
			result.add(point);
		}

		if (result.size() == 1) {
			result.clear();
		}
		return result;
	}

	private void bindElecCompEvent(ElecCompDef def) {
		// 1、连接头监听事件
		def.getTerminalMap().values().forEach(t -> addListener(t.getSpatial(), terminalLitener));
//		// 2、插孔监听事件
//		def.getJackList().forEach(j -> addListener(j.getSpatial(), new MouseEventAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				System.out.println(j.getName());
//			}
//		}));
		// 3、按钮监听事件
//		def.getMagnetismList().forEach(m -> {
		for (ControlIO c : def.getControlIOList()) {
//				if (c.getType().indexOf(CfgConst.SWITCH_CTRL_INPUT) == -1) {
//					continue;
//				}
			if (ControlIO.INTERACT_ROTATE.equals(c.getInteract())) {
				addListener(c.getSpatial(), controlIOWheelListener);
			} else if (ControlIO.INTERACT_PRESS.equals(c.getInteract())) {
				addListener(c.getSpatial(), controlIOPressListener);
			} else {
				addListener(c.getSpatial(), controlIOClickListener);
			}
		}
//		});
//		4、元器件本身的监听事件
		addListener(def.getSpatial(), elecCompRightClickListener);
//		5、若当前为底座添加监听事件
		if (def.getBase() != null) {
			addListener(def.getSpatial(), elecCompBaseClickListener);
		}
	}

	private void unbindElecCompEvent(ElecCompDef def) {
		def.getTerminalMap().values().forEach(t -> mouseEventState.removeCandidate(t.getSpatial()));

//		elecCompDef.getJackList().forEach(j -> mouseEventState.removeCandidate(j.getSpatial()));

//		elecCompDef.getMagnetismList().forEach(m -> {
		def.getControlIOList().forEach(c -> mouseEventState.removeCandidate(c.getSpatial()));
//		});

		mouseEventState.removeCandidate(def.getSpatial());
	}

	private void unbindWireEvent(Wire wire) {
		mouseEventState.removeCandidate(wire.getSpatial());
	}

	public void read(Archive archive) {
		// 等待初始化完成
		while (!initialized) {
			log.debug("正在等待初始化完成。。。");
		}
		readEleccomps(archive.getCompList());
		readWires(archive.getWireList());

	}

	private void readEleccomps(@Nonnull List<ElecCompProxy> compProxyList) {
		ElecCompAction action = SpringUtil.getBean(ElecCompAction.class);

//		一、开始加载元器件
		compProxyList.forEach(proxyComp -> {
//			1、根据元器件型号（model字段），查找数据库中的元器件实体对象
			ElecComp elecComp = action.findElecCompById(proxyComp.getId());
			if (elecComp == null) {
				String errMsg = String.format("没有找到ID号为%s的元器件", proxyComp.getId());
				log.warn(errMsg);
				throw new RuntimeException(errMsg);
			}
//			2、加载模型，同时设置好坐标与旋转
			Node compMdl = (Node) loadAsset(new ModelKey(elecComp.getMdlPath()));
//			设置transform信息：location、rotation
			compMdl.setLocalTranslation(proxyComp.getLocation());
			compMdl.setLocalRotation(proxyComp.getRotation());
//			compMdl.scale(25);

//			3、初始化元器件逻辑对象
			URL cfgUrl = HTTPUtils.getUrl(elecComp.getCfgPath());
			ElecCompDef def = JaxbUtil.converyToJavaBean(cfgUrl, ElecCompDef.class);
			def.setProxy(proxyComp);
			if (def.getRelyOn() == null) {
				// 加入电路板中
				attachToCircuit(compMdl, def);
			} else {
				// 获得底座，底座的排序一定在元器件之前
				Map<String, ElecCompDef> compMap = compList.stream().collect(Collectors.toMap(x -> x.getProxy().getUuid(), x -> x));
				ElecCompDef baseDef = compMap.get(proxyComp.getBaseUuid());
				attachToBase(compMdl, def, baseDef);
			}
		});
	}

//	从存档中读取导线信息
	private void readWires(@Nonnull List<WireProxy> wireProxyList) {
//		KEY : 元器件UUID
		Map<String, ElecCompDef> compMap = compList.stream().collect(Collectors.toMap(x -> x.getProxy().getUuid(), x -> x));

		wireProxyList.forEach(proxy -> {
			ElecCompDef taggedComp1 = compMap.get(proxy.getComp1Uuid());
			Terminal term1 = taggedComp1.getTerminal(proxy.getTernimal1Id());

			ElecCompDef taggedComp2 = compMap.get(proxy.getComp2Uuid());
			Terminal term2 = taggedComp2.getTerminal(proxy.getTernimal2Id());

			Wire wire = new Wire();
			wire.setProxy(proxy);

			wire.bind(term1);
			wire.bind(term2);
//			
			Geometry wireMdl = JmeUtil.createCylinderLine(assetManager, proxy.getPointList(), proxy.getWidth(), proxy.getColor());
			attachToCircuit(wireMdl, wire);
		});
	}

	public void save() {
		Archive archive = new Archive();
//		
		archive.setName(typicalCase.getName());

//		保存元器件列表
		saveEleccomps(archive);
//		保存导线
		saveWires(archive);

		SpringUtil.getBean(TypicalCaseAction.class).save(typicalCase, archive);
	}

	private void saveEleccomps(Archive archive) {
		compList.forEach(comp -> {
			ElecCompProxy compProxy = comp.getProxy();
			compProxy.setLocation(comp.getSpatial().getLocalTranslation());
			System.out.println(comp.getSpatial().getLocalRotation());
			compProxy.setRotation(comp.getSpatial().getLocalRotation());
			archive.getCompList().add(compProxy);
		});
	}

	private void saveWires(Archive archive) {
		wireList.forEach(wire -> {
			WireProxy wireProxy = wire.getProxy();

			wireProxy.setNumber(wire.getWireNum());
			wireProxy.setComp1Uuid(wire.getTerm1().getElecCompDef().getProxy().getUuid());
			wireProxy.setTernimal1Id(wire.getTerm1().getId());
			wireProxy.setComp2Uuid(wire.getTerm2().getElecCompDef().getProxy().getUuid());
			wireProxy.setTernimal2Id(wire.getTerm2().getId());
//			
			archive.getWireList().add(wireProxy);
		});
	}

	@Override
	public void cleanup() {
		for (ElecCompDef elecCompDef : compList) {
			detachElecComp(elecCompDef);
		}
		compList.clear();
		for (Wire wire : wireList) {
			detachWire(wire);
		}
		wireList.clear();
//		combineMap.clear();
//		除了desktop，其余模型全部清除

		if (rootCompNode != null) {
			rootCompNode.removeFromParent();
		}
		if (rootWireNode != null) {
			rootWireNode.removeFromParent();
		}
		if (elecCompBox != null) {
			elecCompBox.removeFromParent();
		}

		CIRCUIT_SERVICE.shutdown();

		super.cleanup();
	}

	public boolean isClean() {
		return compList.size() == 0 && wireList.size() == 0;
	}

	@JmeThread
	void attachToCircuit(Spatial compModel, ElecCompDef elecCompDef) {
//		1、加入元器件标签
		BitmapText tag = new BitmapText(tagFont);
//		标签对象的名称和元器件的UUID一致
		tag.setName(elecCompDef.getProxy().getUuid());
		tag.setText(elecCompDef.getProxy().getTagName());
		guiNode.attachChild(tag);
		TagNameControl control = new TagNameControl(cam, tag);
//		默认状态下不启动
		control.setEnabled(false);
		compModel.addControl(control);
		elecCompDef.getProxy().setTagNode(tag);

//		将holding的模型加入“电路板”的环境中
//		holding会先自动从原parent中剔除，成为孤儿节点，然后加入新的parent中
		rootCompNode.attachChild(compModel);
//		2、绑定模型对象
		elecCompDef.bindModel((Node) compModel);

//		3、添加接线事件
		bindElecCompEvent(elecCompDef);
//		4、将元器件加入列表中
		compList.add(elecCompDef);
//		5、添加到电路逻辑中
		Collection<CircuitElm> elms = elecCompDef.getCircuitElmMap().values();
		elms.forEach(e -> cirSim.addCircuitElm(e));
		cirSim.needAnalyze();
	}

	@JmeThread
	public boolean detachFromCircuit(ElecCompDef elecCompDef) {
//		0、判断元器件连接头是否接线
		for (Terminal terminal : elecCompDef.getTerminalMap().values()) {
			if (terminal.getWireSize() > 0) {
				return false;
			}
		}
//		for (Jack jack : elecCompDef.getJackList()) {
//			if (jack.getCable() != null) {
//				return false;
//			}
//		}

		Base base = elecCompDef.getBase();
		if (base != null) {
			if (base.getRelyOnPlug() != null) {
				return false;
			} else if (base.getRelyOnResis() != null) {
				return false;
			}
		}
//		1、移除元器件模型
		detachElecComp(elecCompDef);
//		2、从元器件列表中移除
		compList.remove(elecCompDef);
		return true;
	}

	private void detachElecComp(ElecCompDef elecCompDef) {
		Spatial elecCompMdl = elecCompDef.getSpatial();
//		1、移除Control
		elecCompMdl.removeControl(TagNameControl.class);
// 		2、删除模型
		elecCompMdl.removeFromParent();
//		3、解绑监听事件
		unbindElecCompEvent(elecCompDef);
//		4、解除组合绑定
		RelyOn relyOn = elecCompDef.getRelyOn();
		if (relyOn != null) {
			ElecCompDef baseDef = relyOn.getBaseDef();
			if (baseDef == null) {
				log.error("组合使用元器件不可能没有底座！");
				return;
			}
			for (String relyId : relyOn.getRelyIds()) {
				Terminal t1 = elecCompDef.getTerminal(relyId);
				Terminal t2 = baseDef.getTerminal(relyId);
				t1.getWires().clear();
				t2.getWires().clear();
			}
			baseDef.getBase().setRelyOnPlug(null);
			elecCompDef.getRelyOn().setBaseDef(null);
		}

		elecCompDef.getCircuitExchangeList().forEach(ex -> cirSim.removeCircuitElm(ex.getCircuitElm()));
		cirSim.needAnalyze();
	}

	public void attachToBase(Spatial holding, ElecCompDef relyOnDef, ElecCompDef baseDef) {
		RelyOn relyOn = relyOnDef.getRelyOn();
		relyOn.setBaseDef(baseDef);

		Node baseMdl = baseDef.getSpatial();
		Vector3f loc = baseMdl.getLocalTranslation().add(relyOn.getTranslation());
		holding.setLocalTranslation(loc);
//		holding.setLocalRotation(relyOn.getRotation());
		rootCompNode.attachChild(holding);
//		2、绑定模型对象
		relyOnDef.bindModel((Node) holding);

//		3、添加事件
		bindElecCompEvent(relyOnDef);

//		4-1、连接元器件与底座对应连接头
		if (relyOn.getType() == RelyOn.RELY_ON_TYPE_PLUG) {
			for (String relyId : relyOn.getRelyIds()) {
				Terminal t1 = relyOnDef.getTerminal(relyId);
				Terminal t2 = baseDef.getTerminal(relyId);

				Wire wire = new Wire();
				wire.markInternal();

				wire.bind(t1);
				wire.bind(t2);
			}
			baseDef.getBase().setRelyOnPlug(relyOnDef);
		}
//		4-2、连接元器件与底座对应连杆
		if (relyOn.getType() == RelyOn.RELY_ON_TYPE_RESIS) {
			for (String relyId : relyOn.getRelyIds()) {
				Terminal t1 = relyOnDef.getTerminal(relyId);
				Terminal t2 = baseDef.getTerminal(relyId);

				Wire wire = new Wire();
				wire.markInternal();

				wire.bind(t1);
				wire.bind(t2);
			}
			baseDef.getBase().setRelyOnPlug(relyOnDef);
		}

//		5、绑定底座对象UUID
		String baseUuid = baseDef.getProxy().getUuid();
		relyOnDef.getProxy().setBaseUuid(baseUuid);
//		combineMap.put(baseUuid, elecCompDef.getProxy().getUuid());
//		6、最后将元器件加入列表中
		compList.add(relyOnDef);

//		7、添加到电路逻辑中
		Collection<CircuitElm> elms = relyOnDef.getCircuitElmMap().values();
		elms.forEach(e -> cirSim.addCircuitElm(e));
		cirSim.needAnalyze();
	}

	void attachToCircuit(Geometry wireMdl, Wire wire) {
//		1、将导线加入场景
// 		其实：wireNode.getChildren()是SafeArrayList类型，自带同步功能。不会出现java.util.ConcurrentModificationException
		rootWireNode.attachChild(wireMdl);

//		加入tagName
		BitmapText tag = new BitmapText(tagFont);
		tag.setLocalScale(0.75f);
		tag.setName(String.format("%s-%s", wire.getProxy().getComp1Uuid(), wire.getProxy().getComp2Uuid()));

		WireProxy proxy = wire.getProxy();
		tag.setText(proxy.getNumber());
		WireNumberControl control = new WireNumberControl(cam, guiNode, tag, wire.getProxy().getPointList());
//		默认状态下不启动
		wireMdl.addControl(control);
		proxy.setTagNode(tag);

// 		2、将模型与逻辑对象绑定
		wire.setSpatial(wireMdl);
//		3、绑定监听事件
		addListener(wireMdl, wireListener);
//		4、将导线加入列表中
		wireList.add(wire);

		cirSim.needAnalyze();
	}

	public boolean detachFromCircuit(Wire wire) {
		// TODO 判断当前是否通电
		detachWire(wire);
		// 移除导线
		wireList.remove(wire);

		return true;
	}

	private void detachWire(Wire wire) {
		Spatial wireMdl = wire.getSpatial();
//		1、移除Control
		wireMdl.removeControl(WireNumberControl.class);
//		2、删除模型
		rootWireNode.detachChild(wireMdl);
//		3、解绑监听事件
		unbindWireEvent(wire);
//		4、解除连接头绑定
		wire.unbind();

		cirSim.needAnalyze();
	}

	public void setTagNameVisible(boolean tagVisible) {
		app.enqueue(() -> this.toggleTagName(tagVisible));
	}

	private void toggleTagName(boolean tagVisible) {
		for (ElecCompDef elecCompDef : compList) {
			Spatial elecCompMdl = elecCompDef.getSpatial();
			@Nonnull
			TagNameControl control = elecCompMdl.getControl(TagNameControl.class);
			if (control != null) {
				control.setEnabled(tagVisible);
			}
		}
		for (Wire wire : wireList) {
			Spatial wireMdl = wire.getSpatial();
			@Nonnull
			WireNumberControl control = wireMdl.getControl(WireNumberControl.class);
			control.setEnabled(tagVisible);
		}
	}

	public void setElecCompTransparent(boolean transparent) {
		app.enqueue(() -> this.toggelTransparent(transparent));
	}

	private void toggelTransparent(boolean transparent) {
		compList.forEach(def -> {
			String shell = def.getParam(ElecCompDef.PARAM_KEY_SHELL);
			Node elecCompMdl = def.getSpatial();
			StringUtil.split(shell, ',').forEach(s -> {
//				找出元器件外壳模型
				Spatial shellNode = elecCompMdl.getChild(s);
				if (shellNode != null) {
//					JmeUtil.transparent(shellNode, .7f);
					if (transparent) {
						shellNode.setCullHint(CullHint.Always);
					} else {
						shellNode.setCullHint(CullHint.Dynamic);
					}
//					MouseEventState.setMouseVisible(shellNode, false);
				} else {
					log.error("模型{}中没有名为{}的节点", elecCompMdl, s);
				}
			});
		});
	}

	public Node getRootCompNode() {
		return rootCompNode;
	}
}
