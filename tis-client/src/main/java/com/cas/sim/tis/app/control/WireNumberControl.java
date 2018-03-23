package com.cas.sim.tis.app.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.AbstractControl;

public class WireNumberControl extends AbstractControl {

	private Camera camera;
	private Map<BitmapText, Vector3f> nums = new HashMap<>();

	public WireNumberControl(Camera camera, Node guiNode, BitmapText num, List<Vector3f> points) {
		this.camera = camera;
		Vector3f prev = null;
		for (int i = 0; i < points.size(); i++) {
			Vector3f point = points.get(i);
			if (prev == null) {
				prev = point;
				continue;
			}
			if (prev.y != point.y) {
				prev = point;
				continue;
			}
			if (this.nums.size() == 0) {
				this.nums.put(num, new Vector3f().interpolateLocal(prev, point, 0.5f));
				guiNode.attachChild(num);
			} else {
				BitmapText clone = num.clone();
				this.nums.put(clone, new Vector3f().interpolateLocal(prev, point, 0.5f));
				guiNode.attachChild(clone);
			}
			prev = point;
		}
	}

	@Override
	protected void controlUpdate(float tpf) {
		for (Entry<BitmapText, Vector3f> num : nums.entrySet()) {
			Vector3f middle = num.getValue();
			Vector3f loc = camera.getScreenCoordinates(middle);
			moveMarker(num.getKey(), loc);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	@Override
	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			for (BitmapText num : nums.keySet()) {
				num.removeFromParent();
			}
		}
		super.setSpatial(spatial);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			for (BitmapText num : nums.keySet()) {
				num.setCullHint(CullHint.Always);
			}
		}
		super.setEnabled(enabled);
	}

	protected void moveMarker(BitmapText num, Vector3f tmp) {
		float x = tmp.x / camera.getWidth() - 0.5f;
		float y = tmp.y / camera.getHeight() - 0.5f;
		if (Math.abs(x) > 0.5f || Math.abs(y) > 0.5f || tmp.z >= 1) {
			// is off screen
			num.setCullHint(CullHint.Always);
		} else {
			// move it
			num.setCullHint(CullHint.Dynamic);
			num.setLocalTranslation(tmp.x - num.getLineWidth() / 2, tmp.y + num.getHeight(), tmp.z);
		}
	}

	public void setNumber(String number) {
		if (!StringUtils.isEmpty(number)) {
			number = String.format(" %s ", number);
		}
		for (BitmapText num : nums.keySet()) {
			num.setText(number);
		}
	}
}
