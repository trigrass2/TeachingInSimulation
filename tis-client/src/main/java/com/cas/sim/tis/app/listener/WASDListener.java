package com.cas.sim.tis.app.listener;

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * A first person view camera controller. After creation, you must register the camera controller with the dispatcher using #registerWithDispatcher(). Controls: - Move the mouse to rotate the camera - Mouse wheel for zooming in or out - WASD keys for moving forward/backward and strafing - QZ keys raise or lower the camera
 */
public class WASDListener implements AnalogListener, ActionListener {
	/**
	 * WASD camera mapping to move left. Default assigned to KeyInput.KEY_A
	 */
	public final static String WASD_STRAFELEFT = "WASD_StrafeLeft";
	/**
	 * WASD camera mapping to move right. Default assigned to KeyInput.KEY_D
	 */
	public final static String WASD_STRAFERIGHT = "WASD_StrafeRight";
	/**
	 * WASD camera mapping to move left. Default assigned to KeyInput.KEY_I
	 */
	public final static String WASD_STRAFEFORWARD = "WASD_StrafeForward";
	/**
	 * WASD camera mapping to move right. Default assigned to KeyInput.KEY_K
	 */
	public final static String WASD_STRAFEBACKWARD = "WASD_StrafeBackward";
	/**
	 * WASD camera mapping to move forward. Default assigned to KeyInput.KEY_W
	 */
	public final static String WASD_FORWARD = "WASD_Forward";
	/**
	 * WASD camera mapping to move backward. Default assigned to KeyInput.KEY_S
	 */
	public final static String WASD_BACKWARD = "WASD_Backward";
	/**
	 * WASD camera mapping to move up. Default assigned to KeyInput.KEY_Q
	 */
	public final static String WASD_RISE = "WASD_Rise";
	/**
	 * WASD camera mapping to move down. Default assigned to KeyInput.KEY_W
	 */
	public final static String WASD_LOWER = "WASD_Lower";
	/**
	 * WASD camera mapping to move down. Default assigned to KeyInput.KEY_LeftShift
	 */
	public final static String WASD_SPEEDUP = "WASD_SpeedUp";
	private static String[] mappings = new String[] {

			WASD_STRAFELEFT, WASD_STRAFERIGHT,

			WASD_STRAFEFORWARD, WASD_STRAFEBACKWARD,

			WASD_FORWARD, WASD_BACKWARD,

			WASD_RISE, WASD_LOWER,

			WASD_SPEEDUP };

	protected Camera cam;
	protected Vector3f initialUpVec;
	protected static float DEFAULT_SPEED = .5f;
	protected float moveSpeed = DEFAULT_SPEED;
	protected MotionAllowedListener motionAllowed = null;
	protected boolean enabled = true;
	protected InputManager inputManager;

	/**
	 * Creates a new FlyByCamera to control the given Camera object.
	 * @param cam
	 */
	public WASDListener(Camera cam) {
		this.cam = cam;
		initialUpVec = cam.getUp().clone();
	}

	/**
	 * Sets the up vector that should be used for the camera.
	 * @param upVec
	 */
	public void setUpVector(Vector3f upVec) {
		initialUpVec.set(upVec);
	}

	public void setMotionAllowedListener(MotionAllowedListener listener) {
		this.motionAllowed = listener;
	}

	/**
	 * Sets the move speed. The speed is given in world units per second.
	 * @param moveSpeed
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * Gets the move speed. The speed is given in world units per second.
	 * @return moveSpeed
	 */
	public float getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * @param enable If false, the camera will ignore input.
	 */
	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	/**
	 * @return If enabled
	 * @see WASDListener#setEnabled(boolean)
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Registers the FlyByCamera to receive input events from the provided Dispatcher.
	 * @param inputManager
	 */
	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;
		// keyboard only WASD for movement and QZ for rise/lower height
		inputManager.addMapping(WASD_STRAFELEFT, new KeyTrigger(KeyInput.KEY_A), new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping(WASD_STRAFERIGHT, new KeyTrigger(KeyInput.KEY_D), new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping(WASD_STRAFEFORWARD, new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping(WASD_STRAFEBACKWARD, new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping(WASD_FORWARD, new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping(WASD_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping(WASD_RISE, new KeyTrigger(KeyInput.KEY_Q), new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping(WASD_LOWER, new KeyTrigger(KeyInput.KEY_Z), new KeyTrigger(KeyInput.KEY_N));
		inputManager.addMapping(WASD_SPEEDUP, new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addListener(this, mappings);

	}

	/**
	 * Unregisters the FlyByCamera from the event Dispatcher.
	 */
	public void unregisterInput() {

		if (inputManager == null) {
			return;
		}

		for (String s : mappings) {
			if (inputManager.hasMapping(s)) {
				inputManager.deleteMapping(s);
			}
		}

		inputManager.removeListener(this);
	}

	protected void riseCamera(float value) {
		Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
		Vector3f pos = cam.getLocation().clone();

		if (motionAllowed != null) {
			motionAllowed.checkMotionAllowed(pos, vel);
		} else {
			pos.addLocal(vel);
		}

		cam.setLocation(pos);
	}

	protected void moveCamera(float value, boolean sideways) {
		Vector3f vel = new Vector3f();
		Vector3f pos = cam.getLocation().clone();

		if (sideways) {
			cam.getLeft(vel);
		} else {
			cam.getDirection(vel);
		}
		vel.multLocal(value * moveSpeed);

		if (motionAllowed != null) {
			motionAllowed.checkMotionAllowed(pos, vel);
		} else {
			pos.addLocal(vel);
		}

		cam.setLocation(pos);
	}

	protected void strafeMoveCamera(float value, boolean b) {
		Vector3f loc = cam.getLocation();
		Vector3f vect = cam.getDirection().mult(value).clone();
		cam.setLocation(loc.add(vect.x, 0, vect.z));
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (!enabled) return;
		if (name.equals(WASD_FORWARD)) {
			moveCamera(value, false);
		} else if (name.equals(WASD_BACKWARD)) {
			moveCamera(-value, false);
		} else if (name.equals(WASD_STRAFELEFT)) {
			moveCamera(value, true);
		} else if (name.equals(WASD_STRAFERIGHT)) {
			moveCamera(-value, true);
		} else if (name.equals(WASD_RISE)) {
			riseCamera(value);
		} else if (name.equals(WASD_LOWER)) {
			riseCamera(-value);
		} else if (name.equals(WASD_STRAFEFORWARD)) {
			strafeMoveCamera(value, false);
		} else if (name.equals(WASD_STRAFEBACKWARD)) {
			strafeMoveCamera(-value, false);
		}
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (WASD_SPEEDUP.equals(name)) {
			setMoveSpeed(isPressed ? DEFAULT_SPEED * 2 : DEFAULT_SPEED);
		}
	}
}
