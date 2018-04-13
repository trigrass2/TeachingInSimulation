package com.cas.sim.tis.app.state;

import org.jetbrains.annotations.NotNull;

import com.cas.sim.tis.app.listener.WASDListener;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class SceneCameraState extends BaseState implements ActionListener, AnalogListener {
	private WASDListener camera;

	protected Quaternion rot = new Quaternion();
	protected Vector3f vector = new Vector3f(0, 0, 5);
	protected Vector3f focus = new Vector3f();

	protected boolean moved = false;
	protected boolean movedR = false;
	protected boolean buttonDownL = false;
	protected boolean buttonDownR = false;
	protected boolean buttonDownM = false;
	protected boolean checkClickL = false;
	protected boolean checkClickR = false;
	protected boolean checkClickM = false;
	protected boolean checkReleaseL = false;
	protected boolean checkReleaseR = false;
	protected boolean checkReleaseM = false;
	protected boolean checkDragged = false;
	protected boolean checkDraggedR = false;
	protected boolean checkReleaseLeft = false;
	protected boolean checkReleaseRight = false;
	protected boolean shiftModifier = false;

	private boolean zoomable = true;

	private boolean moveCamera;

	private Vector3f target;

//	在3D切换到2D时的相机位置，
	private Vector3f camLocation3D, camLocation2D;
	private Quaternion camRotation3D, camRotation2D;

	private Mode mode;

	public enum Mode {
		Ortho, Persp
	}

	public enum View {
		Front, Left, Right, Top, Bottom, Back, User
	}

	public SceneCameraState() {
	}

	@Override
	public void initializeLocal() {
		// 初始化相机
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		// 添加鼠标监听事件
		inputManager.addMapping("MouseAxisX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping("MouseAxisY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping("MouseAxisX-", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping("MouseAxisY-", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("MouseWheel", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("MouseWheel-", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping("MouseButtonLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("MouseButtonMiddle", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
		inputManager.addMapping("MouseButtonRight", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping("ShiftModifier", new KeyTrigger(KeyInput.KEY_LSHIFT));
		// 注册监听
		inputManager.addListener(this, "MouseAxisX", "MouseAxisY", "MouseAxisX-", "MouseAxisY-", "MouseWheel", "MouseWheel-", "MouseButtonLeft", "MouseButtonMiddle", "MouseButtonRight", "ShiftModifier");

		camera = new WASDListener(cam);
		camera.registerWithInput(inputManager);

		switchToView(View.Top);

		toggleOrthoPerspMode(Mode.Ortho);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (!isEnabled()) {
			return;
		}
		if (moveCamera) {
			return;
		}
		if ("MouseAxisX".equals(name)) {
			moved = true;
			movedR = true;

			if (buttonDownL || (buttonDownM && !shiftModifier)) {
				doRotateCamera(Vector3f.UNIT_Y, -value * 2.5f);
			}
			if (buttonDownR || (buttonDownM && shiftModifier)) {
				doPanCamera(value * 2.5f, 0);
			}
		} else if ("MouseAxisY".equals(name)) {
			moved = true;
			movedR = true;

			if (buttonDownL || (buttonDownM && !shiftModifier)) {
				doRotateCamera(cam.getLeft(), -value * 2.5f);
			}
			if (buttonDownR || (buttonDownM && shiftModifier)) {
				doPanCamera(0, -value * 2.5f);
			}
		} else if ("MouseAxisX-".equals(name)) {
			moved = true;
			movedR = true;

			if (buttonDownL || (buttonDownM && !shiftModifier)) {
				doRotateCamera(Vector3f.UNIT_Y, value * 2.5f);
			}
			if (buttonDownR || (buttonDownM && shiftModifier)) {
				doPanCamera(-value * 2.5f, 0);
			}
		} else if ("MouseAxisY-".equals(name)) {
			moved = true;
			movedR = true;

			if (buttonDownL || (buttonDownM && !shiftModifier)) {
				doRotateCamera(cam.getLeft(), value * 2.5f);
			}
			if (buttonDownR || (buttonDownM && shiftModifier)) {
				doPanCamera(0, value * 2.5f);
			}

		} else if ("MouseWheel".equals(name)) {
			if (zoomable && buttonDownR) {
				doZoomCamera(.05f);
			}
		} else if ("MouseWheel-".equals(name)) {
			if (zoomable && buttonDownR) {
				doZoomCamera(-.05f);
			}
		}
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (!isEnabled()) {
			return;
		}
		if ("ShiftModifier".equals(name)) {
			shiftModifier = isPressed;
		}

		if ("MouseButtonLeft".equals(name)) {
			if (isPressed) {
				if (!buttonDownL) { // mouse clicked
					checkClickL = true;
					checkReleaseL = false;
				}
			} else {
				if (buttonDownL) { // mouse released
					checkReleaseL = true;
					checkClickL = false;
				}
			}
			buttonDownL = isPressed;
		}
		if ("MouseButtonRight".equals(name)) {
			if (isPressed) {
				if (!buttonDownR) { // mouse clicked
					checkClickR = true;
					checkReleaseR = false;
				}
			} else {
				if (buttonDownR) { // mouse released
					checkReleaseR = true;
					checkClickR = false;
				}
			}
			buttonDownR = isPressed;
		}
		if ("MouseButtonMiddle".equals(name)) {

			if (isPressed) {
				if (!buttonDownM) { // mouse clicked
					checkClickM = true;
					checkReleaseM = false;
				}
			} else {
				if (buttonDownM) { // mouse released
					checkReleaseM = true;
					checkClickM = false;
				}
			}
			buttonDownM = isPressed;
		}
	}

	@Override
	public void update(float tpf) {
		if (moveCamera) {
//			moveLerpFactor = Math.min(moveLerpFactor + tpf * tpf * moveSensitivity, 1);
//			float len = target.length();
//            delta = FastMath.interpolateLinear(moveLerpFactor, delta, len);
//            System.out.println(cam.getLocation().distance(target));
//            if (len + 0.01f >= delta && len - 0.01f <= delta) {
//            	moveCamera = false;
//                moveLerpFactor = 0;
//            }
			if (cam.getLocation().distance(target) < 0.001f) {
				moveCamera = false;
			}
//			System.out.println(target);
//			cam.setLocation(target);
			cam.getLocation().interpolateLocal(target, 0.25f);
			cam.onFrameChange();
//			moveCamera = false;
		}

		if (moved) {
			// moved, check for drags
			if (checkReleaseL || checkReleaseR || checkReleaseM) {
				// drag released
				checkReleaseL = false;
				checkReleaseR = false;
				checkReleaseM = false;
			}
			moved = false;
		} else {
			// not moved, check for just clicks
			if (checkClickL) {
				checkClickL = false;
			}
			if (checkReleaseL) {
				checkReleaseL = false;
			}
			if (checkClickR) {
				checkClickR = false;
			}
			if (checkReleaseR) {
				checkReleaseR = false;
			}
			if (checkClickM) {
				checkClickM = false;
			}
			if (checkReleaseM) {
				checkReleaseM = false;
			}
		}
	}

	/**
	 * 沿轴移动相机
	 * @param axis
	 * @param amount
	 */
	protected void doRotateCamera(Vector3f axis, float amount) {
		if (mode == Mode.Ortho) {
			return;
		}

		if (axis.equals(cam.getLeft())) {
			float elevation = -FastMath.asin(cam.getDirection().y);
			amount = Math.min(Math.max(elevation + amount, FastMath.DEG_TO_RAD * 10), FastMath.DEG_TO_RAD * 60) - elevation;
		}
		rot.fromAngleAxis(amount, axis);
		cam.getLocation().subtract(focus, vector);
		rot.mult(vector, vector);
		focus.add(vector, cam.getLocation());

		Quaternion curRot = cam.getRotation().clone();
		cam.setRotation(rot.mult(curRot));
	}

	/**
	 * 摇动镜头
	 * @param left
	 * @param up
	 */
	protected void doPanCamera(float left, float up) {
		cam.getLeft().mult(left, vector);
		vector.scaleAdd(up, cam.getUp(), vector);
		vector.multLocal(cam.getLocation().distance(focus));
		cam.setLocation(cam.getLocation().add(vector));
		focus.addLocal(vector);
	}

//	protected void doMoveCamera(float forward) {
//		cam.getDirection().mult(forward, vector);
//		cam.setLocation(cam.getLocation().add(vector));
//	}

	/**
	 * 镜头放大缩小
	 * @param amount
	 */
	protected void doZoomCamera(float amount) {
		amount = cam.getLocation().distance(focus) * amount;
		float dist = cam.getLocation().distance(focus);
		
		amount = dist - Math.max(0f, dist - amount);
		Vector3f loc = cam.getLocation().clone();
		loc.scaleAdd(amount, cam.getDirection(), loc);
		cam.setLocation(loc);

		if (cam.isParallelProjection()) {
			float aspect = (float) cam.getWidth() / cam.getHeight();
//			
//			float h = FastMath.tan(45f * FastMath.DEG_TO_RAD * .5f) * dist;
//			应该用当前相机位置计算与焦点的距离。
			float h = FastMath.tan(45f * FastMath.DEG_TO_RAD * .5f) * cam.getLocation().distance(focus);
			float w = h * aspect;
			cam.setFrustum(-1000, 1000, -w, w, h, -h);
		}
	}

	/**
	 * 切换视角
	 * @param view
	 */
	public void switchToView(final View view) {
		float dist = cam.getLocation().distance(focus);
		switch (view) {
		case Front:
			cam.setLocation(new Vector3f(focus.x, focus.y, focus.z + dist));
			cam.lookAt(focus, Vector3f.UNIT_Y);
			break;
		case Left:
			cam.setLocation(new Vector3f(focus.x + dist, focus.y, focus.z));
			cam.lookAt(focus, Vector3f.UNIT_Y);
			break;
		case Right:
			cam.setLocation(new Vector3f(focus.x - dist, focus.y, focus.z));
			cam.lookAt(focus, Vector3f.UNIT_Y);
			break;
		case Back:
			cam.setLocation(new Vector3f(focus.x, focus.y, focus.z - dist));
			cam.lookAt(focus, Vector3f.UNIT_Y);
			break;
		case Top:
			cam.setLocation(new Vector3f(focus.x, focus.y + dist, focus.z));
			cam.lookAt(focus, Vector3f.UNIT_Z.mult(-1));

			break;
		case Bottom:
			cam.setLocation(new Vector3f(focus.x, focus.y - dist, focus.z));
			cam.lookAt(focus, Vector3f.UNIT_Z);
			break;
		case User:
		default:
		}
	}

	public void toggleOrthoPerspMode(Mode mode) {
		this.mode = mode;
		float aspect = (float) cam.getWidth() / cam.getHeight();
		if (mode == Mode.Ortho) {
//			记录相机的位置
			camLocation3D = cam.getLocation().clone();
			camRotation3D = cam.getRotation().clone();
//			还原相机的位置
			if (camLocation2D != null && camRotation2D != null) {
				cam.setLocation(camLocation2D);
				cam.setRotation(camRotation2D);
			}

			cam.setParallelProjection(true);
			float h = cam.getFrustumTop();
			float dist = cam.getLocation().distance(focus);
//			float fovY = FastMath.atan(h) / (FastMath.DEG_TO_RAD * .5f);
			h = FastMath.tan(45 * FastMath.DEG_TO_RAD * .5f) * dist;
			float w = h * aspect;
			cam.setFrustum(-1000, 1000, -w, w, h, -h);

		} else if (mode == Mode.Persp) {
//			记录相机的位置
			camLocation2D = cam.getLocation().clone();
			camRotation2D = cam.getRotation().clone();

//			还原相机的位置
			if (camLocation3D != null && camRotation3D != null) {
				cam.setLocation(camLocation3D);
				cam.setRotation(camRotation3D);
			}

			cam.setParallelProjection(false);
			cam.setFrustumPerspective(45f, aspect, 0.001f, 1000);
		}
	}

	public void setCamFocus(final Vector3f focus, final boolean moveCamera) {
		this.moveCamera = moveCamera;
		target = cam.getLocation().add(focus.subtract(this.focus));
		this.focus = focus;
	}

	public void setCamFocus(@NotNull Vector3f focus) {
		setCamFocus(focus, false);
	}

//	public void moveCamera(@NotNull Vector3f focus) {
//		cam.setLocation(cam.getLocation().add(focus.subtract(this.focus)));
//	}

	@Override
	public void cleanup() {
		camera.unregisterInput();
		super.cleanup();
	}

	public void setZoomEnable(boolean zoomable) {
		this.zoomable = zoomable;
	}
}
