package com.cas.sim.tis.app.control;

import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.AbstractControl;

public class TagNameControl extends AbstractControl {

	private Camera camera;
	private BitmapText tag;

	public TagNameControl(Camera camera, BitmapText tag) {
		this.camera = camera;
		this.tag = tag;
	}

	@Override
	protected void controlUpdate(float tpf) {
		BoundingBox bound = ((BoundingBox) spatial.getWorldBound());
		Vector3f center = bound.getCenter().add(0, bound.getYExtent() + 0.75f, 0);
		Vector3f loc = camera.getScreenCoordinates(center);
		moveMarker(loc);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	@Override
	public void setSpatial(Spatial spatial) {
		if (spatial == null) {
			tag.removeFromParent();
		}
		super.setSpatial(spatial);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			tag.setCullHint(CullHint.Always);
		}
		super.setEnabled(enabled);
	}

	protected void moveMarker(Vector3f tmp) {
		float x = tmp.x / camera.getWidth() - 0.5f;
		float y = tmp.y / camera.getHeight() - 0.5f;
		if (Math.abs(x) > 0.5f || Math.abs(y) > 0.5f || tmp.z >= 1) {
			// is off screen
			tag.setCullHint(CullHint.Always);
		} else {
			// move it
			tag.setCullHint(CullHint.Dynamic);
			tag.setLocalTranslation(tmp.x - tag.getLineWidth(), tmp.y - tag.getHeight(), tmp.z);
		}
	}

	public void setTagName(String tagName) {
		this.tag.setText(tagName);
	}
}
