package com.cas.circuit.control;

import java.util.function.Consumer;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class MoveCtrl extends AbstractControl {
	private Consumer<Void> end;
	
	private boolean smooth;

	private int dir;

	private float moveSpeed = 4.2f;
	private float distance;
	private float currDistance;

	private Vector3f axis;

	public MoveCtrl(float moveSpeed, boolean smooth, Vector3f axis) {
		this.moveSpeed = moveSpeed;
		this.smooth = smooth;
		this.axis = axis;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (smooth) {
			moveSmooth(tpf);
		} else {
			moveQuickly();
		}
	}

	/**
	 * 模型一次性移动到位
	 */
	private void moveQuickly() {
		move(distance);
		setEnabled(false);
		currDistance = 0;
	}

	/**
	 * 模型平滑移动直到移动到位
	 * @param tpf
	 */
	private void moveSmooth(float tpf) {
		float per = moveSpeed * tpf * dir;
		currDistance += per;
		if (FastMath.abs(currDistance) >= FastMath.abs(distance)) {
			per -= currDistance - distance;
			distance = 0;
			setEnabled(false);
		}
		move(per);
	}

	/**
	 * 模型移动转过指定距离
	 * @param per 单位距离
	 */
	private void move(float per) {
		spatial.move(axis.mult(per));
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(!enabled){
			currDistance = 0;
			if (end != null) {
				try {
					end.accept(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	public void setDistance(float distance) {
		this.distance = distance + currDistance;
		this.dir = distance > 0 ? 1 : -1;
	}
	
	public float getCurrDistance() {
		return currDistance;
	}

	/**
	 * @param call
	 */
	public void setExecutor(Consumer<Void> end) {
		this.end = end;
	}
}
