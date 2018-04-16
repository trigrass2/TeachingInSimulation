package com.cas.sim.tis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.app.filter.BorderFilter;
import com.cas.sim.tis.app.filter.LineRenderFilter;
import com.cas.sim.tis.app.filter.OutlineFilter;
import com.cas.sim.tis.app.filter.OutlinePreFilter;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class FilterUtil {

	private static Logger logger = LoggerFactory.getLogger(FilterUtil.class);

	public static enum FilterType {

		OUTLINE, BLOOM, BORDER, LINERENDER;

	}

	private static SimpleApplication application;
	private static FilterPostProcessor fpp;
	private static AssetManager assetManager;
	private static RenderManager renderManager;
	private static ViewPort viewPort;

	private static ViewPort outlineViewport;
	private static OutlinePreFilter outlinePreFilter;
	private static OutlineFilter outlineFilter;
	private static boolean isLocked;

	private static LineRenderFilter lineRenderFilter;
	private static BorderFilter borderFilter;
	private static BloomFilter bloomFilter;

	public static void init(SimpleApplication app) {
		application = app;
		assetManager = app.getAssetManager();
		renderManager = app.getRenderManager();

		fpp = new FilterPostProcessor(assetManager);

		viewPort = app.getViewPort();
		viewPort.addProcessor(fpp);
	}

	/**
	 * 初始化外边框效果Filter
	 */
	public static void initOutLineFilter() {
		if (fpp == null) {
			return;
		}
		FilterPostProcessor outlinefpp = new FilterPostProcessor(assetManager);
		outlinePreFilter = new OutlinePreFilter();
		outlinefpp.addFilter(outlinePreFilter);

		outlineViewport = renderManager.createPreView("outlineViewport", application.getCamera());
		outlineViewport.setBackgroundColor(ColorRGBA.BlackNoAlpha);
		outlineViewport.setClearFlags(true, true, true);
		outlineViewport.addProcessor(outlinefpp);

		outlineFilter = new OutlineFilter(outlinePreFilter, outlineViewport);

		fpp.addFilter(outlineFilter);
	}

	/**
	 * 初始化高亮效果Filter
	 */
	public static void initBloomFilter() {
		if (fpp == null) {
			return;
		}
		bloomFilter = new BloomFilter(BloomFilter.GlowMode.Objects);
		bloomFilter.setBloomIntensity(0.8f);
		bloomFilter.setExposurePower(1.3f);
		fpp.addFilter(bloomFilter);
	}

	/**
	 * 初始化连接线Filter
	 */
	public static void initLineRenderFilter() {
		if (fpp == null) {
			return;
		}
		// 连接头之间连线
		lineRenderFilter = new LineRenderFilter(3f, application.getInputManager());
		lineRenderFilter.setColor(ColorRGBA.Black);
		fpp.addFilter(lineRenderFilter);
	}

	public static void initBorderFilter(Float borderPx, ColorRGBA borderColor, Vector2f borderResolution, Vector2f marginRightAndBottom) {
		if (fpp == null) {
			return;
		}
		borderFilter = new BorderFilter(borderPx, borderColor, borderResolution, marginRightAndBottom);
		borderFilter.setEnabled(false);
		fpp.addFilter(borderFilter);
	}

	public static void showOutlineEffect(Spatial model, int width, ColorRGBA color) {
		if (application == null) {
			logger.warn("ShaderEffect还没有初始化");
			return;
		}
		if (isLocked) {
			return;
		}
		outlineViewport.clearScenes();
		outlineViewport.attachScene(model);
		outlineFilter.setOutlineColor(color);
		outlineFilter.setOutlineWidth(width);
	}

	public static void showOutlineEffect(Spatial model) {
		showOutlineEffect(model, 3, ColorRGBA.Green);
	}

	public static void hideOutlineEffect(Spatial model) {
		if (application == null) {
			logger.warn("ShaderEffect还没有初始化");
			return;
		}
		outlineViewport.detachScene(model);
	}

	public static void setSpatialElectrical(Spatial spatial, boolean enabled) {
		Geometry electricity = (Geometry) ((Node) spatial).getChild("electrified");
		if (electricity == null) {
			throw new RuntimeException("找不到节点名为“electrified”的节点");
		}
		Material mat = electricity.getMaterial();
		if (enabled) {
			mat.setColor("color", new ColorRGBA(0.1f, 0.8f, 1.0f, 0.5f));
		} else {
			mat.setColor("color", new ColorRGBA(0.1f, 0.8f, 1.0f, 0f));
		}
	}

	public static LineRenderFilter getLineRenderFilter() {
		return lineRenderFilter;
	}

	public static BorderFilter getBorderFilter() {
		return borderFilter;
	}

	public static void removeFilter(FilterType type) {
		switch (type) {
		case OUTLINE:
			fpp.removeFilter(outlineFilter);
			break;
		case BLOOM:
			fpp.removeFilter(bloomFilter);
			break;
		case BORDER:
			fpp.removeFilter(borderFilter);
			break;
		case LINERENDER:
			fpp.removeFilter(lineRenderFilter);
			break;
		default:
			break;
		}
	}

	public static void destroy() {
		if (application == null) {
			logger.warn("ShaderEffect还没有初始化");
			return;
		}
		fpp.removeAllFilters();
		fpp.cleanup();
		viewPort.removeProcessor(fpp);
	}

	public static void setLocked(boolean isLocked) {
		FilterUtil.isLocked = isLocked;
	}

	public static boolean isLocked() {
		return isLocked;
	}
}
