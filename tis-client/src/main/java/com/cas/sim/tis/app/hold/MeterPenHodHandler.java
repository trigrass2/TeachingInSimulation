package com.cas.sim.tis.app.hold;

import com.cas.circuit.util.JmeUtil;
import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MeterPenHodHandler implements HoldHandler {

	private Node parentNode;

	private Spatial spatial;

	private ElecCaseState<?> caseState;

	private Vector3f[] dir = new Vector3f[] { //
			Vector3f.UNIT_X.mult(FastMath.HALF_PI).negate(), //
			Vector3f.UNIT_X.mult(FastMath.PI), //
			Vector3f.UNIT_Y.mult(FastMath.HALF_PI), //
			Vector3f.ZERO, //
			Vector3f.UNIT_Y.mult(FastMath.HALF_PI).negate(), //
	};
	private int index = 0;

	public MeterPenHodHandler(ElecCaseState<?> caseState, Node parentNode) {
		this.parentNode = parentNode;
		this.caseState = caseState;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void initialize() {
//		设置Holding的模型对鼠标不可见
		MouseEventState.setMouseVisible(spatial, false);

		spatial.setUserData("ORIG_LOC", spatial.getLocalTranslation().clone());

//		默认垂直摆放
		spatial.setLocalRotation(new Quaternion().fromAngles(dir[0].x, dir[0].y, dir[0].z));
	}

	@Override
	public void rotate(boolean up) {
		if (up) {
			index++;
		} else {
			index--;
		}
		if (index == -1) {
			index = dir.length - 1;
		} else if (index == dir.length) {
			index = 0;
		}
		spatial.setLocalRotation(new Quaternion().fromAngles(dir[index].x, dir[index].y, dir[index].z));
	}

	@Override
	public void move() {
		Vector3f contactPoint = JmeUtil.getContactPointFromCursor(//
				caseState.getRootNode(), //
				caseState.getCam(), //
				caseState.getInputManager().getCursorPosition());
		if (contactPoint == null) {
			return;
		}
		/*
		 * 在x-z平面上，每次移动一个单位
		 */
		spatial.setLocalTranslation(contactPoint);
	}

	@Override
	public boolean putDown() {
		return true;
	}

	@Override
	public boolean putDownOn(Spatial base) {
		return false;
	}

	@Override
	public void discard() {
//		最终将模型从手中丢掉
		spatial.removeFromParent();

		parentNode.attachChild(spatial);
		spatial.setLocalTranslation(spatial.getUserData("ORIG_LOC"));
		spatial.setLocalRotation(new Quaternion());
	}
	
	@Override
	public <T> T getData() {
//		FIXME
		return null;
	}
}
