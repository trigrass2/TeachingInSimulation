package com.cas.sim.tis.app.filter;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * 纯色外描边
 * @author DING
 */
public class OutlineFilter extends Filter {

	private ViewPort outlineViewport;
	private OutlinePreFilter outlinePreFilter;
	private ColorRGBA outlineColor = new ColorRGBA(0, 1, 0, 1);
	private float outlineWidth = 1;

	public OutlineFilter(OutlinePreFilter outlinePreFilter, ViewPort viewPort) {
		super("OutlineFilter");
		this.outlinePreFilter = outlinePreFilter;
		this.outlineViewport = viewPort;
	}

	@Override
	protected void initFilter(AssetManager assetManager, RenderManager renderManager, ViewPort vp, int w, int h) {
		MaterialDef matDef = (MaterialDef) assetManager.loadAsset("Shaders/outline/Outline.j3md");
		material = new Material(matDef);
		material.setVector2("Resolution", new Vector2f(w, h));
		material.setColor("OutlineColor", outlineColor);
//		material.setColor("OutlineColor", vp.getBackgroundColor());
		material.setFloat("OutlineWidth", outlineWidth);
	}

	@Override
	protected void preFrame(float tpf) {
		super.preFrame(tpf);
		material.setTexture("OutlineDepthTexture", outlinePreFilter.getOutlineTexture());
//		System.out.println("OutlineFilter.preFrame()");
	}

	@Override
	protected Material getMaterial() {
		return material;
	}

	public ColorRGBA getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(ColorRGBA outlineColor) {
		this.outlineColor = outlineColor;
		if (material != null) {
			material.setColor("OutlineColor", outlineColor);
		}
	}

	public float getOutlineWidth() {
		return outlineWidth;
	}

	public void setOutlineWidth(float outlineWidth) {
		this.outlineWidth = outlineWidth;
		if (material != null) {
			material.setFloat("OutlineWidth", outlineWidth);
		}
	}

	public OutlinePreFilter getOutlinePreFilter() {
		return outlinePreFilter;
	}

	public ViewPort getOutlineViewport() {
		return outlineViewport;
	}

}
