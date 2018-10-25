package com.cas.sim.tis.app.hold;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 其实并不是一个AppState，只是用HoldState类修改过来的，为了保持类名的一致性
 * @author zzy
 */
@Slf4j
public enum HoldStatePro {
	ins;

	private static final String UDK_HANDLER = "HoldHandler";

	private static final String TYPICAL_MOVE = "TYPICAL_MOVE";
	private static final String TYPICAL_LEFT_ROTATE = "TYPICAL_LEFT_ROTATE";
	private static final String TYPICAL_RIGHT_ROTATE = "TYPICAL_RIGHT_ROTATE";

//	手持节点模型
	private @Setter Node root;

	private @Getter Spatial holdingSpatial; // 当前拿取的物体

	private @Setter boolean enabled;

	private InputManager inputManager;

	private AnalogListener analogListener = (name, value, tpf) -> {
		if (holdingSpatial == null) {
			return;
		}
		if (TYPICAL_MOVE.equals(name)) {
			getHoldHandler().move();
		} else if (TYPICAL_LEFT_ROTATE.equals(name)) {
			getHoldHandler().rotate(true);
		} else if (TYPICAL_RIGHT_ROTATE.equals(name)) {
			getHoldHandler().rotate(false);
		}
	};

//	private HoldStatePro(Node root, InputManager inputManager) {
//		this.root = root;
//		this.inputManager = inputManager;
//
//		registerWithInput(inputManager);
//	}

	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;
		// 鼠标移动监听
		inputManager.addMapping(TYPICAL_MOVE, //
				new MouseAxisTrigger(MouseInput.AXIS_X, true), //
				new MouseAxisTrigger(MouseInput.AXIS_X, false), //
				new MouseAxisTrigger(MouseInput.AXIS_Y, true), //
				new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		// 鼠标滚动事件
		inputManager.addMapping(TYPICAL_LEFT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(TYPICAL_RIGHT_ROTATE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addListener(analogListener, //
				TYPICAL_LEFT_ROTATE, //
				TYPICAL_RIGHT_ROTATE, //
				TYPICAL_MOVE);
	}

	public void unregisterInput() {
		if (inputManager == null) {
			return;
		}
		inputManager.deleteMapping(TYPICAL_MOVE);
		inputManager.deleteMapping(TYPICAL_LEFT_ROTATE);
		inputManager.deleteMapping(TYPICAL_RIGHT_ROTATE);
		inputManager.removeListener(analogListener);
	}

	/**
	 * 用于判断当前手中是否空闲。
	 * @return true 表示空闲的，可以拿取东西， false 则无法拿取
	 */
	public boolean isIdle() {
		return holdingSpatial == null;
	}

	/**
	 * 拿在手中
	 * @param spatial
	 * @param handler
	 * @return true 拿取成功， false
	 */
	public boolean pickUp(Spatial spatial, HoldHandler handler) {
		if (!enabled) {
			return false;
		}
		if (!isIdle()) {
			String errMsg = String.format("当前手中拿着{}，无法拿取新物品{}，请先丢弃手中物品", holdingSpatial, spatial);
			log.warn(errMsg);
			throw new IllegalStateException(errMsg);
		}
//
		handler.setSpatial(spatial);
		spatial.setUserData(UDK_HANDLER, handler);
//
//		将拿到的模型加入到根节点(root)中
		root.attachChild(spatial);
//
//		拿到物品时候的一些业务处理
		handler.initialize();
		this.holdingSpatial = spatial;
		return true;
	}

	/**
	 * 放置
	 */
	public void putDown() {
		if (holdingSpatial == null) {
			return;
		}
//		放置物品时候的一些业务处理
		boolean result = getHoldHandler().putDown();
		if (result) {
//			holdingSpatial.removeFromParent();
			holdingSpatial = null;
		}
	}

	/**
	 * 放置
	 */
	public void putDownOn(Spatial spatial) {
		if (holdingSpatial == null) {
			return;
		}
//		放置物品时候的一些业务处理
		boolean result = getHoldHandler().putDownOn(spatial);
		if (result) {
//			holdingSpatial.removeFromParent();
			holdingSpatial = null;
		}
	}

	/**
	 * 丢弃
	 */
	public void discard() {
		if (holdingSpatial == null) {
			return;
		}
//		丢弃物品时候的一些业务处理
		getHoldHandler().discard();
//		最终将模型从手中丢掉
		holdingSpatial.removeFromParent();
		holdingSpatial = null;
	}

	private HoldHandler getHoldHandler() {
		HoldHandler handler = holdingSpatial.getUserData(UDK_HANDLER);
		if (handler == null) {
			throw new NullPointerException("模型没有注册");
		}
		return handler;
	}

}
