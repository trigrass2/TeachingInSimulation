package com.cas.sim.tis.app.filter;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

/**
 * 法线预处理filter
 * @author DING
 */
public class NormalPreFilter extends Filter {

	private Pass normalPass;
	private RenderManager renderManager;

	/**
	 * Creates a CartoonEdgeFilter
	 */
	public NormalPreFilter() {
		super("NormalPreFilter");
	}

	@Override
	protected boolean isRequiresDepthTexture() {
		return true;
	}

	@Override
	protected void postQueue(RenderQueue queue) {
		Renderer r = renderManager.getRenderer();
		r.setFrameBuffer(normalPass.getRenderFrameBuffer());
		renderManager.getRenderer().clearBuffers(true, true, true);
	}

	@Override
	protected void postFrame(RenderManager renderManager, ViewPort viewPort, FrameBuffer prevFilterBuffer, FrameBuffer sceneBuffer) {
		super.postFrame(renderManager, viewPort, prevFilterBuffer, sceneBuffer);

	}

	@Override
	protected Material getMaterial() {
		return material;
	}

	public Texture getNormalTexture() {
		return normalPass.getRenderedTexture();
	}

	@Override
	protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
		this.renderManager = renderManager;
		normalPass = new Pass();
		normalPass.init(renderManager.getRenderer(), w, h, Format.RGBA8, Format.Depth);
		material = new Material(manager, "Shaders/Blank.j3md");
	}

	@Override
	protected void cleanUpFilter(Renderer r) {
		normalPass.cleanup(r);
	}

}
