package com.cas.sim.tis.app.state;

import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;

public class MyCameraState extends BaseState {

	private ChaseCamera chaser;
	private boolean moveable;

	@Override
	protected void initializeLocal() {
		float aspect = (float) cam.getWidth() / cam.getHeight();
		cam.setParallelProjection(false);
		cam.setFrustumPerspective(45f, aspect, 0.01f, 1000);

//		1、禁用飞行视角
		app.getFlyByCamera().setEnabled(false);

//		2、启动跟随视角
		chaser = new ChaseCamera(cam, rootNode, inputManager);
		
		chaser.setHideCursorOnRotate(false);
//		3、设置垂直翻转
		chaser.setInvertVerticalAxis(true);
//		4、设置最大和最小仰角
		chaser.setMaxVerticalRotation(FastMath.DEG_TO_RAD * 60);
		chaser.setMinVerticalRotation(FastMath.DEG_TO_RAD * 10);
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
//				Vector3f point = JmeUtil.getContactPointFromCursor(rootNode, cam, inputManager);
//				if (point != null) {
//					Spatial ball = JmeUtil.getSphere(assetManager, 32, 0.1f, ColorRGBA.Red);
//					rootNode.attachChild(ball);
//					ball.setLocalTranslation(point);
//				}
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

	public void setZoomEnable(boolean zoomEnable) {
		if (zoomEnable) {
			if (!inputManager.hasMapping(CameraInput.CHASECAM_ZOOMIN)) {
				inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
				inputManager.addListener(chaser, CameraInput.CHASECAM_ZOOMIN);
			}
			if (!inputManager.hasMapping(CameraInput.CHASECAM_ZOOMOUT)) {
				inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
				inputManager.addListener(chaser, CameraInput.CHASECAM_ZOOMOUT);
			}
		} else {
			inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMIN);
			inputManager.deleteMapping(CameraInput.CHASECAM_ZOOMOUT);
		}
	}

	public ChaseCamera getChaser() {
		return chaser;
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
	}
}
