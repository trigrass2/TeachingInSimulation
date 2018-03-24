package com.cas.circuit.control;

import java.util.function.Consumer;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class RotateCtrl extends AbstractControl {
	private Consumer<Void> end;

	private boolean smooth;

	private int dir;

	private float rotateSpeed = 240;
	private float rad;
	private float currRad;

	private Vector3f axis;

	public RotateCtrl(float rotateSpeed, boolean smooth, Vector3f axis) {
		this.rotateSpeed = rotateSpeed;
		this.smooth = smooth;
		this.axis = axis;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (smooth) {
			rotateSmooth(tpf);
		} else {
			rotateQuickly();
		}
	}

	/**
	 * 模型一次性旋转到位
	 */
	private void rotateQuickly() {
		rotate(rad);
		setEnabled(false);
		currRad = 0;
	}

	/**
	 * 模型缓慢旋转直到旋转到位
	 * @param tpf
	 */
	private void rotateSmooth(float tpf) {
		float per = rotateSpeed * tpf * FastMath.DEG_TO_RAD * dir;
		currRad += per;
		if (FastMath.abs(currRad) >= FastMath.abs(rad)) {
			per -= currRad - rad;
			rad = 0;
			rotate(per);
			setEnabled(false);
		} else {
			rotate(per);
		}
	}

	/**
	 * 模型绕轴转过指定弧度
	 * @param per 单位弧度
	 */
	private void rotate(float per) {
		Vector3f rotate = axis.mult(per);
		spatial.rotate(rotate.x, rotate.y, rotate.z);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled) {
			currRad = 0;
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

	public void setRad(float angle) {
		this.rad = angle * FastMath.DEG_TO_RAD + currRad;
		dir = rad > 0 ? 1 : -1;
	}

	/**
	 * @param call
	 */
	public void setExecutor(Consumer<Void> end) {
		this.end = end;
	}
}
