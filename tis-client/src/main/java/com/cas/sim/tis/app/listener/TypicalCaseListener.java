//package com.cas.sim.tis.app.listener;
//
//import com.cas.sim.tis.app.state.typical.HoldState;
//import com.jme3.input.InputManager;
//import com.jme3.input.MouseInput;
//import com.jme3.input.controls.AnalogListener;
//import com.jme3.input.controls.MouseAxisTrigger;
//
//public class TypicalCaseListener implements AnalogListener {
//
//	private static final String TYPICAL_MOVE = "TYPICAL_MOVE";
//	private static final String TYPICAL_LEFT_ROTATE = "TYPICAL_LEFT_ROTATE";
//	private static final String TYPICAL_RIGHT_ROTATE = "TYPICAL_RIGHT_ROTATE";
//
//	private HoldState state;
//	private InputManager inputManager;
//
//	public TypicalCaseListener(HoldState state) {
//		this.state = state;
//	}
//
//	public void registerWithInput(InputManager inputManager) {
//		this.inputManager = inputManager;
//		// 鼠标移动监听
//		inputManager.addMapping(TYPICAL_MOVE, //
//				new MouseAxisTrigger(MouseInput.AXIS_X, true), //
//				new MouseAxisTrigger(MouseInput.AXIS_X, false), //
//				new MouseAxisTrigger(MouseInput.AXIS_Y, true), //
//				new MouseAxisTrigger(MouseInput.AXIS_Y, false));
//		inputManager.addListener(this, TYPICAL_MOVE);
//		// 鼠标滚动事件
//		inputManager.addMapping(TYPICAL_LEFT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
//		inputManager.addMapping(TYPICAL_RIGHT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
//		inputManager.addListener(this, TYPICAL_LEFT_ROTATE, TYPICAL_RIGHT_ROTATE);
//	}
//
//	public void unregisterInput() {
//		if (inputManager == null) {
//			return;
//		}
//		inputManager.deleteMapping(TYPICAL_MOVE);
//		inputManager.deleteMapping(TYPICAL_LEFT_ROTATE);
//		inputManager.deleteMapping(TYPICAL_RIGHT_ROTATE);
//		inputManager.removeListener(this);
//	}
//
//	@Override
//	public void onAnalog(String name, float value, float tpf) {
//		if (TYPICAL_MOVE.equals(name)) {
//			state.mouseMoved();
//		} else if (TYPICAL_LEFT_ROTATE.equals(name)) {
//			state.mouseWheel(true);
//		} else if (TYPICAL_RIGHT_ROTATE.equals(name)) {
//			state.mouseWheel(false);
//		}
//	}
//
//}
