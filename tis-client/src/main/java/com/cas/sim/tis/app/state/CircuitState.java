package com.cas.sim.tis.app.state;

import java.nio.FloatBuffer;

import org.jetbrains.annotations.Nullable;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Terminal;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.util.JmeUtil;
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
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Line;
import com.jme3.util.BufferUtils;

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

	private State state;

	private Line startLine1;
	private Line startLine2;
	private Line startLine3;

	private Line midLine1;
	private Line midLine2;

	private Line endLine1;
	private Line endLine2;

	private Vector3f dir;
	private Vector3f midAxis;
	Quaternion roll90 = new Quaternion().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
	private ColorRGBA color = ColorRGBA.Black;

	public CircuitState(Node root) {
		this.root = root;
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
		ballMat.getAdditionalRenderState().setLineWidth(5);
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
			@Nullable
			Vector3f point = JmeUtil.getContactPointFromCursor(root, cam, inputManager);
			if (point == null) {
				return;
			}
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

//	开始接线之前的准备工作
	private void connect_pre(ElecCompDef def, Terminal t) {
		wireNode = new Node();
		root.attachChild(wireNode);

		// FIXME 导线连接的位置
		Vector3f dest = t.getSpatial().getWorldTranslation().clone();
		dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
		midAxis = roll90.mult(dir);

		startLine1 = new Line(dest, dest);
		startLine2 = new Line(dest, dest);
		startLine3 = new Line(dest, dest);

		wireNode.attachChild(createLineGeo(startLine1));
		wireNode.attachChild(createLineGeo(startLine2));
		wireNode.attachChild(createLineGeo(startLine3));
	}

//	
	private void connect_starting() {
		VertexBuffer position = startLine3.getBuffer(Type.Position);
		float[] arr = BufferUtils.getFloatArray((FloatBuffer) position.getDataReadOnly());

		midLine1 = startLine3;
		midLine1.getStart().set(arr[0], arr[1], arr[2]);
//					midLine1.getEnd().set(arr[3], arr[4], arr[5]);
		midLine2 = new Line(new Vector3f(arr[3], arr[4], arr[5]), new Vector3f(arr[3], arr[4], arr[5]));
		wireNode.attachChild(createLineGeo(midLine2));

		startLine1 = null;
		startLine2 = null;
		startLine3 = null;
	}

	Vector3f getStartPoint(Line line) {
		VertexBuffer position = startLine3.getBuffer(Type.Position);
		float[] arr = BufferUtils.getFloatArray((FloatBuffer) position.getDataReadOnly());
		return new Vector3f(arr[0], arr[1], arr[2]);
	}

	private void connect_mid() {
		// midLine1 连接好，不再修改
		// 将之前的midLine2作为新的midLine1
		// 获取midLine2的起始点
		VertexBuffer position = midLine2.getBuffer(Type.Position);
		float[] arr = BufferUtils.getFloatArray((FloatBuffer) position.getDataReadOnly());

		midLine1 = midLine2;
		midLine1.getStart().set(arr[0], arr[1], arr[2]);
		//
		midLine2 = new Line(new Vector3f(arr[3], arr[4], arr[5]), new Vector3f(arr[3], arr[4], arr[5]));
		wireNode.attachChild(createLineGeo(midLine2));

		midAxis = roll90.mult(midAxis);

	}

	private void connect_ending(ElecCompDef def, Terminal t) {
//		倒数第一个点
		Vector3f dest = t.getSpatial().getWorldTranslation().clone();
		dir = def.getSpatial().getLocalRotation().mult(t.getDirection());
//
//		倒数第二个点：判断midLine1的最后一个点的坐标
		Vector3f last2 = null;
		VertexBuffer position = midLine1.getBuffer(Type.Position);
		float[] arr = BufferUtils.getFloatArray((FloatBuffer) position.getDataReadOnly());
		Vector3f midLine1StartPoint = new Vector3f(arr[0], arr[1], arr[2]);

		Vector3f v = midLine1StartPoint.subtract(dest);

		if (v.dot(dir) < minLen) {
//			需要三根线
			last2 = dest.add(dir.mult(minLen));
//			同时修改midLine1的坐标
			midLine1.updatePoints(midLine1.getStart(), last2);

		} else {
//			需要两根线
			last2 = dest.add(v.project(dir));
		}

		Vector3f last3 = last2.add(midLine1StartPoint.subtract(last2).project(Vector3f.UNIT_Y));
		midLine1.updatePoints(midLine1.getStart(), last3);

		endLine1 = new Line(last3, last2);
		endLine2 = new Line(last2, dest);
//
		wireNode.attachChild(createLineGeo(endLine1));
		wireNode.attachChild(createLineGeo(endLine2));

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

	public void bindElecCompEvent(ElecCompDef def) {
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

	public void setColor(ColorRGBA color) {
		this.color = color;
	}
}
