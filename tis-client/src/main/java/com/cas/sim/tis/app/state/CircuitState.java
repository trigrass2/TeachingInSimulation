package com.cas.sim.tis.app.state;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import com.cas.circuit.CfgConst;
import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ControlIO;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.Terminal;
import com.cas.circuit.vo.Wire;
import com.cas.circuit.vo.archive.ElecCompProxy;
import com.cas.circuit.vo.archive.WireProxy;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.app.control.HintControl;
import com.cas.sim.tis.app.control.TagNameControl;
import com.cas.sim.tis.app.control.WireNumberControl;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.xml.util.JaxbUtil;
import com.jme3.asset.ModelKey;
import com.jme3.collision.CollisionResult;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;

import javafx.application.Platform;

public class CircuitState extends BaseState {
//	导线工连接头接出的最小长度
	private static final float minLen = 0.3f;
//	导线与电路板的最小距离
	private static final float minHeight = 0.1f;

	private static final String COMP_ROOT = "comp_root_in_circuit_state";
	private static final String WIRE_ROOT = "wire_root_in_circuit_state";
//	导线颜色
	private static ColorRGBA color = ColorRGBA.Red;
//	导线半径
	private static float width = 0.01f;

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

	private TypicalCase typicalCase;
	private List<ElecCompDef> compList = new ArrayList<>();

	@Nullable
	private CollisionResult collision;

	private Geometry desktop;

	private Node rootCompNode;
	private Node rootWireNode;

	private Node guiNode;
	private BitmapFont tagFont;
	private boolean tagVisible;
	private boolean tempTagVisible;
	private boolean tageChanged;
	// Key:BASE UUID,Value: TOP UUID
	private Map<String, String> combineMap = new HashMap<String, String>();

	public CircuitState(TypicalCase typicalCase, Node root) {
		this.typicalCase = typicalCase;
		this.root = root;

		desktop = (Geometry) root.getChild("Circuit-Desktop");
	}

	public void setColor(ColorRGBA color) {
		CircuitState.color = color;
	}

	public void setWidth(float width) {
//		考虑模型的显示比例。
		CircuitState.width = width / 500;
	}

	@Override
	protected void initializeLocal() {
		rootCompNode = new Node(COMP_ROOT);
		rootWireNode = new Node(WIRE_ROOT);
		rootWireNode.setCullHint(CullHint.Never);

		root.attachChild(rootCompNode);
		root.attachChild(rootWireNode);

		guiNode = app.getGuiNode();
		tagFont = assetManager.loadFont("font/Font4TagName.fnt");

		bindCircuitBoardEvents();

		LOG.info("CircuitState完成初始化");
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (tagVisible != tempTagVisible) {
			tagVisible = tempTagVisible;
			toggleTagName();
		} else if (tageChanged) {
			toggleTagName();
			tageChanged = false;
		}
	}

	private void bindCircuitBoardEvents() {
//		给电路板添加监听
		addMapping("CONNECT_ON_DESKTOP_AXIR_X+", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		addMapping("CONNECT_ON_DESKTOP_AXIR_X-", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		addMapping("CONNECT_ON_DESKTOP_AXIR_Y+", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		addMapping("CONNECT_ON_DESKTOP_AXIR_Y-", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		String[] hoverMapping = new String[] { "CONNECT_ON_DESKTOP_AXIR_X+", "CONNECT_ON_DESKTOP_AXIR_X-", "CONNECT_ON_DESKTOP_AXIR_Y+", "CONNECT_ON_DESKTOP_AXIR_Y-" };
		addListener((AnalogListener) (name, value, tpf) -> {
			if (state == null) {
				return;
			}
//			先判断鼠标是否在元器件上
			collision = JmeUtil.getCollisionFromCursor(rootCompNode, cam, inputManager);
//			如果没有宣导元器件
			if (collision == null) {
//				再尝试和桌面碰撞
				collision = JmeUtil.getCollisionFromCursor(desktop, cam, inputManager);
			}
			if (collision == null) {
				return;
			}
			Vector3f point = collision.getContactPoint();

//			离电路板略高一些。
			point.addLocal(0, minHeight, 0);

			if (state == State.Starting) {
//				开头
				Vector3f project = point.subtract(startLine1.getStart()).project(dir);
				if (project.dot(dir) < minLen) {
					return;
				}
				Vector3f line1EndPoint = startLine1.getStart().add(project);
				startLine1.updatePoints(startLine1.getStart(), line1EndPoint);

				Vector3f line2EndPoint = line1EndPoint.add(point.subtract(line1EndPoint).project(Vector3f.UNIT_Y));
				startLine2.updatePoints(line1EndPoint, line2EndPoint);

//				Vector3f line3EndPoint = point;
				startLine3.updatePoints(line2EndPoint, point);
			} else if (state == State.Mid) {
//				计算midLine1.getStart() 与  鼠标point 向量在X与Z轴的投影。
				Vector3f tmpPoint = null;
				tmpPoint = midLine1.getStart().add(point.subtract(midLine1.getStart()).project(midAxis));
				midLine1.updatePoints(midLine1.getStart(), tmpPoint);
				midLine2.updatePoints(tmpPoint, point);
			} else if (state == State.Ending) {
//				endLine1.updatePoints(endLine1.getStart(), point);
			}

		}, hoverMapping);

		addMapping("CLICKED_ON_BOARD", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		addMapping("CANCEL_ON_CONNECTING", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		addListener((ActionListener) (name, isPressed, tpf) -> {
			if (collision == null) {
				return;
			}
			if (desktop != collision.getGeometry()) {
				return;
			}

			if ("CANCEL_ON_CONNECTING".equals(name) && isPressed) {
				if (state == State.Starting || state == State.Mid) {
					connectClean();
				}
			}

			if ("CLICKED_ON_BOARD".equals(name) && isPressed) {
				if (state == null) {
					return;
				}
				if (state == State.Starting) {
					connectStarting();
//					设置当前接线状态为接线中
					state = State.Mid;
				} else if (state == State.Mid) {
					connectMid();
				} else if (state == State.Ending) {
//					do nothing
				}
			}
		}, "CLICKED_ON_BOARD", "CANCEL_ON_CONNECTING");
	}

//			点击连接头、开始接线之前的准备工作
	private void connectPre(ElecCompDef def, Terminal t) {
		// 创建一个节点，存放当前一根导线的模型
		tmpWireNode = new Node();
		// 将导线模型添加到root节点中以显示
		rootWireNode.attachChild(tmpWireNode);
		// 创建一个导线逻辑对象
		wire = new Wire();
		// 导线颜色和线宽
		wire.getProxy().setColor(color);
		wire.getProxy().setWidth(width);

		// 导线一头连接在当前连接头上
		wire.bind(t);

		// FIXME 导线连接的位置，多跟导线的情况下错开
		Vector3f dest = t.getSpatial().getWorldTranslation().clone();
		// 获取导线连接的方向
		dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
		midAxis = roll90.mult(dir);

		// 钱三段导线
		startLine1 = new Line(dest, dest);//
		startLine2 = new Line(dest, dest);
		startLine3 = new Line(dest, dest);

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

	private void connectEnding(ElecCompDef def, Terminal t) {
		// 终点
		Vector3f dest = t.getSpatial().getWorldTranslation().clone();
		// 重点线头连出的方向
		dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
		//
		// ##最终是在终点dest和startPoint之间创建导线##
		Vector3f startPoint = JmeUtil.getLineStart(midLine1);

		// 判断最后一段线与dir的向量积，从而知道是哪一种连接方式
		Vector3f t1 = JmeUtil.getLineStart(midLine2);
		Vector3f t2 = JmeUtil.getLineEnd(midLine2);

		if (Math.round(t2.subtract(t1).dot(dir)) == 0) {
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
		} else {
			// 倒数第二个点是midLine1上任意一点在dir上的投影点，这里取startPoint
			Vector3f last2 = dest.add(startPoint.subtract(dest).project(dir));
			// 倒数第三个点是last2在电路板子上的投影点，y的值可以从取startPoint起点的y
			Vector3f last3 = last2.clone().setY(startPoint.getY());
			// 三个点全部找齐，下面把midLine1和midLine2的重新设置
			midLine1.updatePoints(midLine1.getStart(), last3);
			midLine2.updatePoints(last3, last2);
			// 另外，还需要一根线
			tmpWireNode.attachChild(JmeUtil.createLineGeo(assetManager, new Line(last2, dest), color));
		}

		wire.bind(t);
		List<Vector3f> pointList = getPointList();
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
		startLine1 = null;
		startLine2 = null;
		startLine3 = null;

		midLine1 = null;
		midLine2 = null;

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
	private List<Vector3f> getPointList() {
		List<Vector3f> result = new ArrayList<>();

		List<Spatial> children = tmpWireNode.getChildren();
		Set<Vector3f> set = new HashSet<>();
		for (int i = 0; i < children.size() - 1; i++) {
			Spatial spatial = (Spatial) children.get(i);
			Geometry geo = (Geometry) spatial;
			Line mesh = (Line) geo.getMesh();
			Vector3f point = JmeUtil.getLineStart(mesh);
			if (!set.contains(point)) {
				result.add(point);
			}
		}
//		最后一段线
		Spatial spatial = children.get(children.size() - 1);
		Geometry geo = (Geometry) spatial;
		Line mesh = (Line) geo.getMesh();

		Vector3f point = JmeUtil.getLineStart(mesh);
		if (!set.contains(point)) {
			result.add(point);
		}

		point = JmeUtil.getLineEnd(mesh);
		if (!set.contains(point)) {
			result.add(point);
		}

		if (result.size() == 1) {
			result.clear();
		}
		return result;
	}

	private void bindElecCompEvent(ElecCompDef def) {
		// 1、连接头监听事件
		def.getTerminalList().forEach(t -> addListener(t.getSpatial(), new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (state == null || state == State.Ending) {
					connectPre(def, t);
//					设置接线状态为开始
					state = State.Starting;
				} else if (state == State.Mid) {
//					
					connectEnding(def, t);

					state = State.Ending;
				} else if (state == State.Ending) {
//					do nothing
				}
			}
		}));
		// 2、插孔监听事件
		def.getJackList().forEach(j -> addListener(j.getSpatial(), new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(j.getName());
			}
		}));
		// 3、按钮监听事件
		def.getMagnetismList().forEach(m -> {
			for (ControlIO c : m.getControlIOList()) {
				if (c.getType().indexOf(CfgConst.SWITCH_CTRL_INPUT) == -1) {
					continue;
				}
				if (ControlIO.INTERACT_ROTATE.equals(c.getInteract())) {
					addListener(c.getSpatial(), new MouseEventAdapter() {
						@Override
						public void mouseWheel(MouseEvent e) {
							c.switchStateChanged(e.getWheel());
							c.playMotion();
						}
					});
				} else if (ControlIO.INTERACT_PRESS.equals(c.getInteract())) {
					addListener(c.getSpatial(), new MouseEventAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							c.switchStateChanged(null);
							c.playMotion();
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							c.switchStateChanged(null);
							c.playMotion();
						}
					});
				} else {
					addListener(c.getSpatial(), new MouseEventAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							c.switchStateChanged(null);
							c.playMotion();
						}
					});
				}
			}
		});
		// 4、元器件本身的监听事件
		addListener(def.getSpatial(), new MouseEventAdapter() {
			@Override
			public void mouseRightClicked(MouseEvent e) {
				super.mouseRightClicked(e);
				IContent content = SpringUtil.getBean(PageController.class).getIContent();
				if (content instanceof TypicalCase3D) {
					Platform.runLater(() -> {
						((TypicalCase3D) content).showPopupMenu(def);
					});
				}
			}
		});
//		5、若当前为底座添加监听事件
		if (ElecComp.COMBINE_BUTTOM == def.getElecComp().getCombine()) {
			addListener(def.getSpatial(), new MouseEventAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 判断当前底座是否已经被占用
					if (StringUtils.isEmpty(combineMap.get(def.getProxy().getUuid()))) {
						TypicalCaseState caseState = stateManager.getState(TypicalCaseState.class);
						caseState.putDownOnBase(def);
						super.mouseClicked(e);
					}
				}
			});
		}
	}

	private void unbindElecCompEvent(ElecCompDef elecCompDef) {
		elecCompDef.getTerminalList().forEach(t -> mouseEventState.removeCandidate(t.getSpatial()));

		elecCompDef.getJackList().forEach(j -> mouseEventState.removeCandidate(j.getSpatial()));

		elecCompDef.getMagnetismList().forEach(m -> {
			m.getControlIOList().forEach(c -> mouseEventState.removeCandidate(c.getSpatial()));
		});

		mouseEventState.removeCandidate(elecCompDef.getSpatial());
	}

	private void bindWireEvent(Geometry wireMdl, final Wire wire) {
		addListener(wireMdl, new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 选中穿透高亮

				for (Wire w : wireList) {
					Spatial mdl = w.getSpatial();
					HintControl control = mdl.getControl(HintControl.class);
					boolean enable = w == wire;
					if (control != null) {
						mdl.removeControl(control);
					} else if (enable) {
						control = new HintControl();
						mdl.addControl(control);
					}
				}
				super.mouseClicked(e);
			}

			@Override
			public void mouseRightClicked(MouseEvent e) {
				// 选中显示导线菜单
				IContent content = SpringUtil.getBean(PageController.class).getIContent();
				if (content instanceof TypicalCase3D) {
					Platform.runLater(() -> {
						((TypicalCase3D) content).showPopupMenu(wire);
					});
				}
				super.mouseRightClicked(e);
			}
		});
	}

	private void unbindWireEvent(Wire wire) {
		mouseEventState.removeCandidate(wire.getSpatial());
	}

	public void read(Archive archive) {
		// 等待初始化完成
		while (!initialized) {
			LOG.debug("正在等待初始化完成。。。");
		}
		readEleccomps(archive.getCompList());
		readWires(archive.getWireList());
	}

	private void readEleccomps(@Nonnull List<ElecCompProxy> compProxyList) {
		ElecCompAction action = SpringUtil.getBean(ElecCompAction.class);

//		一、开始加载元器件
		compProxyList.forEach(proxyComp -> {
//			1、根据元器件型号（model字段），查找数据库中的元器件实体对象
			ElecComp elecComp = action.getElecComp(proxyComp.getModel());
			if (elecComp == null) {
				LOG.warn("没有找到型号为{}的元器件", proxyComp.getModel());
			}

//			2、加载模型，同时设置好坐标与旋转
			Future<Node> task = app.enqueue((Callable<Node>) () -> {
				Node node = (Node) loadAsset(new ModelKey(elecComp.getMdlPath()));
//				设置transform信息：location、rotation
				node.setLocalTranslation(proxyComp.getLocation());
				node.setLocalRotation(proxyComp.getRotation());
				node.scale(25);
				return node;
			});
			Node compMdl = null;
			try {
				compMdl = task.get();
			} catch (Exception e) {
				LOG.error("无法加载元器件模型{}:{}", elecComp.getMdlPath(), e);
				throw new RuntimeException(e);
			}

//			3、初始化元器件逻辑对象
			URL cfgUrl = SpringUtil.getBean(HTTPUtils.class).getUrl(elecComp.getCfgPath());
			ElecCompDef def = JaxbUtil.converyToJavaBean(cfgUrl, ElecCompDef.class);
			def.setProxy(proxyComp);
			def.setElecComp(elecComp);
			if (ElecComp.COMBINE_TOP == elecComp.getCombine()) {
				// 获得底座，底座的排序一定在元器件之前
				Map<String, ElecCompDef> compMap = compList.stream().collect(Collectors.toMap(x -> x.getProxy().getUuid(), x -> x));
				ElecCompDef baseDef = compMap.get(proxyComp.getBaseUuid());
				attachToBase(compMdl, def, baseDef);
			} else {
				// 加入电路板中
				attachToCircuit(compMdl, def);
			}
		});
	}

	private void readWires(@Nonnull List<WireProxy> wireProxyList) {
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

			Future<Geometry> task = app.enqueue((Callable<Geometry>) () -> {
				Geometry wireMdl = JmeUtil.createCylinderLine(assetManager, proxy.getPointList(), proxy.getWidth(), proxy.getColor());
				return wireMdl;
			});
			Geometry wireMdl = null;
			try {
				wireMdl = task.get();
			} catch (Exception e) {
				LOG.error("无法加载导线模型:{}", e);
				throw new RuntimeException(e);
			}
			attachToCircuit(wireMdl, wire);
		});
	}

	public void save() {
		Archive archive = new Archive();
//		保存元器件列表
		saveEleccomps(archive);
//		保存导线
		saveWires(archive);

		SpringUtil.getBean(TypicalCaseAction.class).save(typicalCase, archive);
	}

	private void saveEleccomps(Archive archive) {
		compList.forEach(comp -> {
			ElecCompProxy compProxy = comp.getProxy();
			compProxy.setModel(comp.getModel());
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
			wireProxy.setComp1Uuid(wire.getTerm1().getElecComp().getProxy().getUuid());
			wireProxy.setTernimal1Id(wire.getTerm1().getId());
			wireProxy.setComp2Uuid(wire.getTerm2().getElecComp().getProxy().getUuid());
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
		combineMap.clear();
//		除了desktop，其余模型全部清除
		root.detachAllChildren();
		if (desktop != null) {
			root.attachChild(desktop);
		}
		super.cleanup();
	}

	public void attachToCircuit(Spatial holding, ElecCompDef elecCompDef) {
//		将holding的模型加入“电路板”的环境中
//		holding会先自动从原parent中剔除，成为孤儿节点，然后加入新的parent中
		rootCompNode.attachChild(holding);

//		2、绑定模型对象
		elecCompDef.bindModel((Node) holding);

//		3、添加事件
		bindElecCompEvent(elecCompDef);

//		4、最后将元器件加入列表中
		compList.add(elecCompDef);
	}

	public boolean detachFromCircuit(ElecCompDef elecCompDef) {
//		0、判断元器件连接头是否接线
		for (Terminal terminal : elecCompDef.getTerminalList()) {
			if (terminal.getWires().size() > 0) {
				return false;
			}
		}
		for (Jack jack : elecCompDef.getJackList()) {
			if (jack.getCable() != null) {
				return false;
			}
		}
		if (ElecComp.COMBINE_BUTTOM == elecCompDef.getElecComp().getCombine()) {
			if (!StringUtils.isEmpty(combineMap.get(elecCompDef.getProxy().getUuid()))) {
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
		if (ElecComp.COMBINE_TOP == elecCompDef.getElecComp().getCombine()) {
			elecCompDef.getStitchList().forEach(s -> {
				s.getContacted().setContacted(null);
				s.setContacted(null);
			});
			combineMap.remove(elecCompDef.getProxy().getBaseUuid());
			elecCompDef.getProxy().setBaseUuid(null);
		}
	}

	public void attachToBase(Spatial holding, ElecCompDef elecCompDef, ElecCompDef baseDef) {
//		FIXME 加入底座指定位置
		Node parent = baseDef.getSpatial();
		holding.scale(0.04f);
		holding.setLocalTranslation(0, 0, 0);
		parent.attachChild(holding);

//		2、绑定模型对象
		elecCompDef.bindModel((Node) holding);

//		3、添加事件
		bindElecCompEvent(elecCompDef);

//		4、连接元器件与底座对应连接头
		Map<String, Terminal> bases = baseDef.getTerminalMap();
		elecCompDef.getStitchList().forEach(s -> {
			Terminal terminal = bases.get(s.getId());
			s.setContacted(terminal);
			terminal.setContacted(s);
		});
//		5、绑定底座对象UUID
		String baseUuid = baseDef.getProxy().getUuid();
		elecCompDef.getProxy().setBaseUuid(baseUuid);
		combineMap.put(baseUuid, elecCompDef.getProxy().getUuid());
//		6、最后将元器件加入列表中
		compList.add(elecCompDef);
	}

	private void attachToCircuit(Geometry wireMdl, Wire wire) {
//		1、将导线加入场景
// 		其实：wireNode.getChildren()是SafeArrayList类型，自带同步功能。不会出现java.util.ConcurrentModificationException
		rootWireNode.attachChild(wireMdl);
// 		2、将模型与逻辑对象绑定
		wire.setSpatial(wireMdl);
//		3、绑定监听事件
		bindWireEvent(wireMdl, wire);
//		4、将导线加入列表中
		wireList.add(wire);
	}

	public boolean detachFromCircuit(Wire wire) {
		// 判断当前是否通电
		if (wire.getTerm1().hasInputVoltageIO() || wire.getTerm1().hasOutputVoltageIO()) {
			return false;
		} else if (wire.getTerm2().hasInputVoltageIO() || wire.getTerm2().hasOutputVoltageIO()) {
			return false;
		}
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
	}

	public void setTagNameVisible(boolean tagVisible) {
		this.tempTagVisible = tagVisible;
	}

	public void setTagNameChanged(boolean tagChanged) {
		this.tageChanged = tagChanged;
	}

	private void toggleTagName() {
		for (ElecCompDef elecCompDef : compList) {
			Spatial elecCompMdl = elecCompDef.getSpatial();
			TagNameControl control = elecCompMdl.getControl(TagNameControl.class);
			if (control == null) {
				BitmapText tag = new BitmapText(tagFont);
				tag.setName(elecCompDef.getProxy().getUuid());
				guiNode.attachChild(tag);

				control = new TagNameControl(cam, tag);
				elecCompMdl.addControl(control);
			}
			control.setTagName(elecCompDef.getProxy().getTagName());
			control.setEnabled(tagVisible);
		}
		for (Wire wire : wireList) {
			Spatial wireMdl = wire.getSpatial();
			WireNumberControl control = wireMdl.getControl(WireNumberControl.class);
			if (control == null) {
				BitmapText tag = new BitmapText(tagFont);
				tag.setLocalScale(0.75f);
				tag.setName(String.format("%s-%s", wire.getProxy().getComp1Uuid(), wire.getProxy().getComp2Uuid()));

				control = new WireNumberControl(cam, guiNode, tag, wire.getProxy().getPointList());
				wireMdl.addControl(control);
			}
			control.setNumber(wire.getProxy().getNumber());
			control.setEnabled(tagVisible);
		}
	}
}
