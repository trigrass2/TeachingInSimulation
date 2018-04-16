package com.cas.sim.tis.app.filter;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

public class BorderFilter extends Filter {

	private Float borderPx;
	private ColorRGBA borderColor;
	private Vector2f borderResolution;
	private Vector2f marginRightAndBottom;

	public BorderFilter(Float borderPx, ColorRGBA borderColor, Vector2f borderResolution, Vector2f marginRightAndBottom) {
		super("BorderFilter");
		this.borderPx = borderPx;
		this.borderColor = borderColor;
		this.borderResolution = borderResolution;
		this.marginRightAndBottom = marginRightAndBottom;
	}

	@Override
	protected void initFilter(AssetManager assetManager, RenderManager renderManager, ViewPort vp, int w, int h) {
		MaterialDef matDef = (MaterialDef) assetManager.loadAsset("Shaders/border/border.j3md");
		material = new Material(matDef);
		material.setColor("BorderColor", borderColor);
		material.setFloat("BorderPx", borderPx);
		material.setVector2("Resolution", new Vector2f(w, h));
		material.setVector2("BorderResolution", borderResolution);
		material.setVector2("MarginRightAndBottom", marginRightAndBottom);
	}

	@Override
	protected Material getMaterial() {
		return material;
	}

//	@Override
//	protected boolean isRequiresDepthTexture() {
//		return true;
//	}

}
