package com.cas.sim.tis.app.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.Archive;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Terminal;
import com.cas.circuit.vo.Wire;
import com.cas.circuit.vo.archive.ElecCompProxy;
import com.cas.circuit.vo.archive.WireProxy;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.collision.CollisionResult;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;

import jme3tools.optimize.GeometryBatchFactory;

public class CircuitState extends BaseState {
//	导线工连接头接出的最小长度
	private static final float minLen = 0.3f;
//	导线与电路板的最小距离
	private static final float minHeight = 0.1f;

	static enum State {
		Starting, Mid, Ending
	}

	private Node root;
	private Node wireNode;
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
	private ColorRGBA color = ColorRGBA.Black;
	private float width = 5;

	private TypicalCase typicalCase;
	private Map<String, ElecCompDef> taggedCompMap = new HashMap<>();
	private @Nullable CollisionResult collision;

	private Geometry desktop;

	public CircuitState(TypicalCase typicalCase, Node root) {
		this.typicalCase = typicalCase;
		this.root = root;

		desktop = (Geometry) root.getChild("Circuit-Desktop");
	}

	@Override
	protected void initializeLocal() {
		bindCircuitBoardEvents();
	}

	private Geometry createLineGeo(Mesh line) {
		Geometry geom = new Geometry("TempWire", line);

		Material ballMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		ballMat.setColor("Diffuse", color);
		ballMat.setFloat("Shininess", 10f);
		ballMat.setColor("Specular", ColorRGBA.White);
		ballMat.setBoolean("UseMaterialColors", true);
		ballMat.getAdditionalRenderState().setLineWidth(width);
		geom.setMaterial(ballMat);
		return geom;
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
			collision = JmeUtil.getCollisionFromCursor(root, cam, inputManager);
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
				clean();
			}

			if ("CLICKED_ON_BOARD".equals(name) && isPressed) {
				if (state == null) {
					return;
				}
				if (state == State.Starting) {
					connect_starting();
//					设置当前接线状态为接线中
					state = State.Mid;
				} else if (state == State.Mid) {
					connect_mid();
				} else if (state == State.Ending) {
//					do nothing
				}
			}
		}, "CLICKED_ON_BOARD", "CANCEL_ON_CONNECTING");
	}

//	
	private void connect_starting() {
		Vector3f startPoint = JmeUtil.getLineStart(startLine3);
		Vector3f endPoint = JmeUtil.getLineEnd(startLine3);

		midLine1 = startLine3;
		midLine1.getStart().set(startPoint);
//		midLine1.getEnd().set(arr[3], arr[4], arr[5]);
		midLine2 = new Line(startPoint, endPoint);
		wireNode.attachChild(createLineGeo(midLine2));

		startLine1 = null;
		startLine2 = null;
		startLine3 = null;
	}

	private void connect_mid() {
		// midLine1 连接好，不再修改
		// 将之前的midLine2作为新的midLine1
		// 获取midLine2的起始点
		Vector3f startPoint = JmeUtil.getLineStart(midLine2);
		Vector3f endPoint = JmeUtil.getLineEnd(midLine2);

		midLine1 = midLine2;
		midLine1.getStart().set(startPoint);
		//
		midLine2 = new Line(startPoint, endPoint);
		wireNode.attachChild(createLineGeo(midLine2));

		midAxis = roll90.mult(midAxis);
	}

	private void connect_ending(ElecCompDef def, Terminal t) {
//		终点
		Vector3f dest = t.getSpatial().getWorldTranslation().clone();
//		重点线头连出的方向
		dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
//
//		##最终是在终点dest和startPoint之间创建导线##
		Vector3f startPoint = JmeUtil.getLineStart(midLine1);

//		判断最后一段线与dir的向量积，从而知道是哪一种连接方式
		Vector3f t1 = JmeUtil.getLineStart(midLine2);
		Vector3f t2 = JmeUtil.getLineEnd(midLine2);

		if (Math.round(t2.subtract(t1).dot(dir)) == 0) {
//			表明是垂直的方向连接到终点的，这种情况稍微复杂一些
//			倒数第二个点是终点在dir方向上的点，距离终点的长度是minLen
			Vector3f last2 = dest.add(dir.normalize().mult(minLen));
//			倒数第三个点是last2在电路板子上的投影点，y的值可以从取startPoint起点的y
			Vector3f last3 = last2.clone().setY(startPoint.getY());
//			倒数第四个点是last3在midLine1上的投影点
			Vector3f endPoint = JmeUtil.getLineEnd(midLine1);
			Vector3f last4 = startPoint.add(last3.subtract(startPoint).project(endPoint.subtract(startPoint)));

//			四个点全部找齐，下面把midLine1和midLine2的重新设置
			midLine1.updatePoints(midLine1.getStart(), last4);
			midLine2.updatePoints(last4, last3);
//			另外，还需要两根线
			wireNode.attachChild(createLineGeo(new Line(last3, last2)));
			wireNode.attachChild(createLineGeo(new Line(last2, dest)));
		} else {
//			倒数第二个点是midLine1上任意一点在dir上的投影点，这里取startPoint
			Vector3f last2 = dest.add(startPoint.subtract(dest).project(dir));
//			倒数第三个点是last2在电路板子上的投影点，y的值可以从取startPoint起点的y
			Vector3f last3 = last2.clone().setY(startPoint.getY());
//			三个点全部找齐，下面把midLine1和midLine2的重新设置
			midLine1.updatePoints(midLine1.getStart(), last3);
			midLine2.updatePoints(last3, last2);
//			另外，还需要一根线
			wireNode.attachChild(createLineGeo(new Line(last2, dest)));
		}

		wire.bind(t);
		wireList.add(wire);

		List<Spatial> children = wireNode.getChildren();
//		矫正坐标点
		Line line = null;
		for (int i = 0; i < children.size(); i++) {
			Geometry geo = (Geometry) children.get(i);
			Line mesh = (Line) geo.getMesh();

			if (line != null) {
				System.out.printf("%s==%s?%s\r\n", JmeUtil.getLineStart(mesh), JmeUtil.getLineEnd(line), JmeUtil.getLineStart(mesh).equals(JmeUtil.getLineEnd(line)));
			}
			line = mesh;
		}
		wire = null;
		wireNode = null;

		clean();
	}

	private void clean() {
		state = null;
		dir = null;
		startLine1 = null;
		startLine2 = null;
		startLine3 = null;

		midLine1 = null;
		midLine2 = null;

		if (wireNode != null) {
			wireNode.removeFromParent();
			wireNode = null;
		}
	}

	public void bindElecCompEvent(String tagName, ElecCompDef def) {
		taggedCompMap.put(tagName, def);

		// 1、连接头监听事件
		def.getTerminalList().forEach(t -> addListener(t.getSpatial(), new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (state == null || state == State.Ending) {
					connect_pre(def, t);
//					设置接线状态为开始
					state = State.Starting;
				} else if (state == State.Mid) {
//					
					connect_ending(def, t);

					state = State.Ending;
				} else if (state == State.Ending) {
//					do nothing
				}
			}

//			点击连接头、开始接线之前的准备工作
			protected void connect_pre(ElecCompDef def, Terminal t) {
//				创建一个节点，存放当前一根导线的模型
				wireNode = new Node();
//				将导线模型添加到root节点中以显示
				root.attachChild(wireNode);
//				创建一个导线逻辑对象
				wire = new Wire();
//				将模型与逻辑对象绑定
				wire.setSpatial(wireNode);
//				导线一头连接在当前连接头上
				wire.bind(t);

				// FIXME 导线连接的位置，多跟导线的情况下错开
				Vector3f dest = t.getSpatial().getWorldTranslation().clone();
//				获取导线连接的方向
				dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
				midAxis = roll90.mult(dir);

//				钱三段导线
				startLine1 = new Line(dest, dest);//
				startLine2 = new Line(dest, dest);
				startLine3 = new Line(dest, dest);

				wireNode.attachChild(createLineGeo(startLine1));
				wireNode.attachChild(createLineGeo(startLine2));
				wireNode.attachChild(createLineGeo(startLine3));
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
			m.getControlIOList().forEach(c -> addListener(c.getSpatial(), new MouseEventAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(c.getName());
				}
			}));
		});
		// 元器件本身的监听事件
		addListener(def.getSpatial(), new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(def.getName());
			}
		});
	}

	public void bindWires(@Nonnull List<WireProxy> wireList) {
		wireList.forEach(w -> {
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
					// 创建导线的某一段导线。
//					Line line = new Line(a[0], a[1]);
//					geoList.add(new Geometry("", line));
				});
				Mesh outMesh = new Mesh();
				GeometryBatchFactory.mergeGeometries(geoList, outMesh);
				// 创建一根导线
				Geometry lineGeo = new Geometry("Wire", outMesh);
				// TODO 给导线上色、调整粗细

				return lineGeo;
			});
			try {
				task.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void setColor(ColorRGBA color) {
		this.color = color;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void save() {
		Archive archive = new Archive();
//		保存元器件列表
		saveEleccomps(archive);
//		保存导线
//		saveWires(archive);

		SpringUtil.getBean(TypicalCaseAction.class).save(typicalCase, archive);
	}

	private void saveEleccomps(Archive archive) {
		taggedCompMap.entrySet().forEach(entry -> {
			ElecCompProxy compProxy = new ElecCompProxy();
			compProxy.setTagName(entry.getKey());
			compProxy.setModel(entry.getValue().getModel());
			compProxy.setLocation(entry.getValue().getSpatial().getLocalTranslation());
			compProxy.setRotation(entry.getValue().getSpatial().getLocalRotation());
			archive.getCompList().add(compProxy);
		});
	}

	private void saveWires(Archive archive) {
		wireList.forEach(wire -> {
			WireProxy wireProxy = new WireProxy();
			wireProxy.setWidth(wire.getWidth());
			wireProxy.setNumber(wire.getWireNum());
			wireProxy.setColor(wire.getColor());
			wireProxy.setTagName1(wire.getTerm1().getElecComp().getTagName());
			wireProxy.setTernimalId1(wire.getTerm1().getId());
			wireProxy.setTagName2(wire.getTerm2().getElecComp().getTagName());
			wireProxy.setTernimalId2(wire.getTerm2().getId());

			archive.getWireList().add(wireProxy);
		});
	}

	@Override
	public void cleanup() {
		root.detachAllChildren();
		super.cleanup();
	}
}
