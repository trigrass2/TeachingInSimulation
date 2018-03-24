package com.cas.sim.tis.app.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.cas.sim.tis.app.state.BaseState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 注意：在使用ChaseCamera时候，要调用chaser.setHideCursorOnRotate(false);否则会影响本类的运行
 * @author Administrator
 */
public class MouseEventState extends BaseState {
	public static final String TO_MOUSE_VISIBLE = "ToMouseVisible";

	/**
	 * 能看成一次点击事件(click)的最大划过距离
	 */
	protected static final float CLICK_MAX_AXIS_DISTANCE = 0.003f;
	protected static final String MOUSE_PRIMARY_BUTTON = "MOUSE_PRIMARY_BUTTON";
	protected static final String MOUSE_SECOND_BUTTON = "MOUSE_SECOND_BUTTON";
	protected static final String MOUSE_WHEEL_UP = "MOUSE_WHEEL_UP";
	protected static final String MOUSE_WHEEL_DOWN = "MOUSE_WHEEL_DOWN";
	protected static final String MOUSE_AXIS_LEFT = "MOUSE_AXIS_LEFT";
	protected static final String MOUSE_AXIS_RIGHT = "MOUSE_AXIS_RIGHT";
	protected static final String MOUSE_AXIS_UP = "MOUSE_AXIS_UP";
	protected static final String MOUSE_AXIS_DOWN = "MOUSE_AXIS_DOWN";

//	private static final String[] MapNames = { MOUSE_BUTTON, MOUSE_AXIS_LEFT, MOUSE_AXIS_RIGHT, MOUSE_AXIS_UP, MOUSE_AXIS_DOWN };

	private List<Spatial> candidateList = new ArrayList<>();

	private Map<Spatial, List<MouseEventListener>> eventMap = new HashMap<>();

	protected CollisionResults results = new CollisionResults();
	protected MouseEvent e = null;
	protected Spatial picked = null;
	protected float axis_distance = 0f;

	protected boolean mouseButtonPressed; // 标记鼠标状态_鼠标是否按下
//	private ArrayList<Spatial> ignorModelList = new ArrayList<Spatial>(); // 要忽略的模型

	protected Spatial pressed;

	@Override
	protected void initializeLocal() {
		addMapping(MOUSE_PRIMARY_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		addListener(new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				mouseButtonPressed = isPressed;

				if (isPressed) {
					pickModel();
					axis_distance = 0;
//					记录当前鼠标按下时，所选中模型
					pressed = picked;
					if (pressed != null) {
//						触发这个模型的鼠标按下的事件
						e.setAction(MouseAction.MOUSE_PRESSED);
						notifyEventTrigged(e);
					}
				} else {
					pickModel();
//					鼠标松开
					Spatial oldPressed = pressed;
					pressed = null;
//					如果两次选择的不一样，则选择无效
					if (picked == null || oldPressed != picked) {
						return;
					}
					if (Math.abs(axis_distance) < CLICK_MAX_AXIS_DISTANCE) {
						e.setAction(MouseAction.MOUSE_CLICK);
						notifyEventTrigged(e);
					}
					e.setAction(MouseAction.MOUSE_RELEASED);
					notifyEventTrigged(e);
				}
			}
		}, MOUSE_PRIMARY_BUTTON);
		addMapping(MOUSE_SECOND_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		addListener(new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				mouseButtonPressed = isPressed;

				if (isPressed) {
					pickModel();
					axis_distance = 0;
//				记录当前鼠标按下时，所选中模型
					pressed = picked;
				} else {
					pickModel();
//				鼠标松开
					Spatial oldPressed = pressed;
					pressed = null;
//				如果两次选择的不一样，则选择无效
					if (picked == null || oldPressed != picked) {
						return;
					}
					if (Math.abs(axis_distance) < CLICK_MAX_AXIS_DISTANCE) {
						e.setAction(MouseAction.MOUSE_RIGHT_CLICK);
						notifyEventTrigged(e);
					}
				}
			}
		}, MOUSE_SECOND_BUTTON);
		addMapping(MOUSE_WHEEL_UP, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		addMapping(MOUSE_WHEEL_DOWN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		addListener(new AnalogListener() {

			@Override
			public void onAnalog(String name, float value, float tpf) {
				pickModel();
				if (MOUSE_WHEEL_UP.equals(name)) {
					e.setWheel(MouseEvent.WHEEL_UP);
					e.setAction(MouseAction.MOUSE_WHEEL);
					notifyEventTrigged(e);
				} else if (MOUSE_WHEEL_DOWN.equals(name)) {
					e.setWheel(MouseEvent.WHEEL_DOWN);
					e.setAction(MouseAction.MOUSE_WHEEL);
					notifyEventTrigged(e);
				}
			}
		}, MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN);
		addMapping(MOUSE_AXIS_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		addMapping(MOUSE_AXIS_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
		addMapping(MOUSE_AXIS_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		addMapping(MOUSE_AXIS_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		addListener(new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (mouseButtonPressed) {
					if (MOUSE_AXIS_LEFT.equals(name)) {
						axis_distance += value;
					} else if (MOUSE_AXIS_RIGHT.equals(name)) {
						axis_distance -= value;
					} else if (MOUSE_AXIS_UP.equals(name)) {
						axis_distance += value;
					} else if (MOUSE_AXIS_DOWN.equals(name)) {
						axis_distance -= value;
					}
				}
			}
		}, MOUSE_AXIS_LEFT, MOUSE_AXIS_RIGHT, MOUSE_AXIS_UP, MOUSE_AXIS_DOWN);
	}

	@Override
	public void cleanup() {
		candidateList.clear();

		super.cleanup();
	}

	public void addCandidate(Spatial spatial, MouseEventListener listener) {
		if (spatial == null) {
			throw new NullPointerException();
		}
		synchronized (candidateList) {
//			获取节点的鼠鼠标监听
			List<MouseEventListener> listeners = getListenerList(spatial);
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}

			if (candidateList.indexOf(spatial) == -1) {
				Spatial candidate = null;
//				默认添加到集合末尾
				int insertIndex = candidateList.size();
				for (int i = 0; i < candidateList.size(); i++) {
					candidate = candidateList.get(i);
//					查找集合中是否有node的父节点,如果有,则插入第一个父节点的前面
					if (candidate instanceof Node) {
						if (((Node) candidate).hasChild(spatial)) {
							insertIndex = i;
							break;
						}
					}
				}
				candidateList.add(insertIndex, spatial);
			}
		}
	}

	public void removeCandidate(Spatial node) {
		eventMap.remove(node);
		candidateList.remove(node);
	}

	public void removeListener(Spatial node, MouseEventListener eventListener) {
		List<MouseEventListener> listeners = getListenerList(node);
		boolean result = listeners.remove(eventListener);
		if (result) {
			LOG.info("删除指定的鼠标监听 成功！");
			mouseButtonPressed = false;
		} else {
			LOG.warn("");
		}
		if (listeners.size() == 0) {
			removeCandidate(node);
		}
	}

	// 选择几何体
	protected void pickModel() {
		Spatial tmpPicked = null;
		Vector3f contactPoint = null;
		Vector3f contactNormal = null;

		this.picked = null;
		this.e = null;

		rootNode.collideWith(getRay(), results);
		int resultsize = results.size();
		Geometry geometry = null;
		CollisionResult collision = null;
		for (int i = 0; i < resultsize; i++) {
//			从近到远依次判断被选中的对象
			collision = results.getCollision(i);
			geometry = collision.getGeometry();
//			验证被选中模型的有效性
			if (valiedate(geometry)) {
				break;
			}
		}
		if (geometry == null) {
//			记录本次选中的模型
			return;
		}
		List<Spatial> candicates = getCandidates();
		for (Spatial node : candicates) {
			if (node == geometry) {
				tmpPicked = node;
//			} else if (node instanceof Node && ((Node) node).hasChild(geometry)) {
//				tmpPicked = node;
			} else if (node instanceof Node && geometry.hasAncestor((Node) node)) {
				tmpPicked = node;
			}
			if (tmpPicked != null) {
				contactPoint = collision.getContactPoint(); // 绝对坐标
				contactNormal = collision.getContactNormal(); // 相对坐标
				break;
			}
		}
//		记录本次选中的模型
		this.picked = tmpPicked;
		this.e = new MouseEvent(picked, contactPoint, contactNormal);
	}

	protected boolean valiedate(Geometry geometry) {
//		模型被剔除，视为不可选中
//		if (geometry.getCullHint() == CullHint.Always) {
//			return false;
//		}
////		模型虽然显示，但已经加入黑名单中，视为不可选中。
//		if (this.ignorModelList.contains(geometry)) {
//			return false;
//		}
//		漏网之鱼，对鼠标不可见，但是不在黑名单中的，也视为不可选中。
		Boolean toMouseVisible = geometry.getUserData(TO_MOUSE_VISIBLE);
//		有这个属性，并且值是false，表示对鼠标不可见
		if (toMouseVisible != null && !toMouseVisible.booleanValue()) {
//			模型对鼠标不可见
			return false;
		}
//		
		return true;
	}

	protected Ray getRay() {
		Vector2f click2d = null;
		if (inputManager.isCursorVisible()) {
			click2d = inputManager.getCursorPosition();
		} else {
			click2d = new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2);
		}
		Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtract(click3d).normalizeLocal();

		results.clear();
		return new Ray(click3d, dir);
	}

	protected void notifyEventTrigged(MouseEvent event) {
		if (!isEnabled() || event == null) {
			return;
		}
		// 事件被消耗了,就没有此事件,就不会在有响应了
		if (event.isSuspend()) {
			return;
		}
		MouseAction action = event.getAction();
		if (action == null) {
			return;
		}

		try {
			Spatial sp = event.getSpatial();
			if (!getCandidates().contains(sp)) {
				return;
			}
			List<MouseEventListener> listeners = getListenerList(sp);
			for (MouseEventListener listener : listeners) {
				switch (action) {
				case MOUSE_PRESSED:
					listener.mousePressed(event);
					break;
				case MOUSE_RELEASED:
					listener.mouseReleased(event);
					break;
				case MOUSE_CLICK:
					listener.mouseClicked(event);
					break;
				case MOUSE_RIGHT_CLICK:
					listener.mouseRightClicked(event);
					break;
				case MOUSE_WHEEL:
					listener.mouseWheel(event);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			LOG.warn("事件异常", e);
			throw new RuntimeException(e);
		}
	}

	public List<Spatial> getCandidates() {
		synchronized (candidateList) {
			return candidateList;
		}
	}

//	public void setToMouseTransprent(Spatial... ignor) {
//		if (ignor == null) {
//			return;
//		}
//		for (Spatial spatial : ignor) {
//			transparent(spatial);
//		}
//
////		if (Util.isEmpty(ignor)) {
////			return;
////		}
////		for (int i = 0; i < ignor.length; i++) {
////			if (this.ignorModelList.contains(ignor[i])) {
////				continue;
////			}
////			this.ignorModelList.add(ignor[i]);
////		}
//	}
//
//	private void transparent(Spatial spatial) {
//		if (spatial instanceof Node) {
//			((Node) spatial).getChildren().forEach(child -> transparent(child));
//		} else {
//			spatial.setUserData(TO_MOUSE_VISIBLE, false);
//		}
//	}

	@NotNull
	private List<MouseEventListener> getListenerList(Spatial key) {
		List<MouseEventListener> list = eventMap.get(key);
		if (list == null) {
			list = new ArrayList<>();
			eventMap.put(key, list);
		}
		return list;
	}
}
