package com.cas.sim.tis.app.control;

import com.cas.circuit.util.JmeUtil;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class HintControl extends AbstractControl {

	/**
	 * 颜色提示
	 */
	private static final float COLOR_SPEED = 1f;
	private ColorRGBA begin = ColorRGBA.Red;
	private static final ColorRGBA COLOR_END = ColorRGBA.White;
	private float changeAmnt;
	private float operator = 1;

	public HintControl(ColorRGBA color) {
		this.begin = color;
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		if (changeAmnt < 0) {
			operator = 1;
		} else if (changeAmnt > 1) {
			operator = -1;
		}
		changeAmnt += tpf * operator * COLOR_SPEED;
		JmeUtil.color(spatial, begin.clone().interpolateLocal(COLOR_END, changeAmnt), false);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	@Override
	public void setEnabled(boolean enabled) {
		if (spatial == null) {
			throw new NullPointerException("请将control加入到spatial中，再调用该方法");
		}
		if (enabled) {
			JmeUtil.color(spatial, begin, true);
		} else {
			JmeUtil.uncolor(this.spatial);
		}

		super.setEnabled(enabled);
	}

}
