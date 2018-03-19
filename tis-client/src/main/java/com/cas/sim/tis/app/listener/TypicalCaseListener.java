package com.cas.sim.tis.app.listener;

import com.cas.sim.tis.app.state.TypicalCaseState;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class TypicalCaseListener implements AnalogListener, ActionListener {

	private static final String TYPICAL_MOVE = "TYPICAL_MOVE";
	private static final String TYPICAL_LEFT_CLICKED = "TYPICAL_LEFT_CLICKED";
	private static final String TYPICAL_RIGHT_CLICKED = "TYPICAL_RIGHT_CLICKED";
	private static final String TYPICAL_LEFT_ROTATE = "TYPICAL_LEFT_ROTATE";
	private static final String TYPICAL_RIGHT_ROTATE = "TYPICAL_RIGHT_ROTATE";

	private TypicalCaseState state;
	private InputManager inputManager;

	public TypicalCaseListener(TypicalCaseState state) {
		this.state = state;
	}

	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;
		// 鼠标移动监听
		inputManager.addMapping(TYPICAL_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(MouseInput.AXIS_Y, true), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addListener(this, TYPICAL_MOVE);
		// 左右键监听
		inputManager.addMapping(TYPICAL_LEFT_CLICKED, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(TYPICAL_RIGHT_CLICKED, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(this, TYPICAL_LEFT_CLICKED, TYPICAL_RIGHT_CLICKED);
		// 鼠标滚动事件
		inputManager.addMapping(TYPICAL_LEFT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(TYPICAL_RIGHT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addListener(this, TYPICAL_LEFT_ROTATE, TYPICAL_RIGHT_ROTATE);
	}

	public void unregisterInput() {
		if (inputManager == null) {
			return;
		}
		inputManager.deleteMapping(TYPICAL_MOVE);
		inputManager.deleteMapping(TYPICAL_LEFT_CLICKED);
		inputManager.deleteMapping(TYPICAL_RIGHT_CLICKED);
		inputManager.deleteMapping(TYPICAL_LEFT_ROTATE);
		inputManager.deleteMapping(TYPICAL_RIGHT_ROTATE);
		inputManager.removeListener(this);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (TYPICAL_MOVE.equals(name)) {
			state.mouseMoved();
		} else if (TYPICAL_LEFT_ROTATE.equals(name)) {
			state.mouseWheel(true);
		} else if (TYPICAL_RIGHT_ROTATE.equals(name)) {
			state.mouseWheel(false);
		}
	}

	@Override
	public void onAction(String name, boolean pressed, float tpf) {
		if (TYPICAL_LEFT_CLICKED.equals(name)) {
			state.mouseClicked(pressed);
		} else if (TYPICAL_RIGHT_CLICKED.equals(name)) {
			state.mouseRightClicked(pressed);
		}
	}

}
