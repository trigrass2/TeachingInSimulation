package com.cas.sim.tis.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXApplication;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

public class JmeApplication extends JmeToJFXApplication {
	public static final Logger LOG = LoggerFactory.getLogger(JmeApplication.class);

	public JmeApplication() {
		AppSettings settings = new AppSettings(true);
        settings.setGammaCorrection(true);
        settings.setResizable(true);

		JmeToJFXIntegrator.prepareSettings(settings, 60);
		setSettings(settings);
		setShowSettings(false);
	}

	@Override
	public void start() {
		LOG.info("start");
		super.start();
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();

		stateManager.attach(new MouseEventState());

//		注册资源路径
		String assetPath = SpringUtil.getBean(HTTPUtils.class).getFullPath("assets/");
		LOG.debug("注册资源路径:{}", assetPath);
		assetManager.registerLocator(assetPath, UrlLocator.class);
//		assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets", HttpZipLocator.class);

////		创建天空盒
////		Spatial sky = SkyFactory.createSky(assetManager, "Model/Sky/noon_grass_2k.hdr", SkyFactory.EnvMapType.EquirectMap);
//		Spatial sky = SkyFactory.createSky(assetManager, "Model/Sky/Path.hdr", SkyFactory.EnvMapType.EquirectMap);
//		rootNode.attachChild(sky);
//
//		滤镜
//      fpp.addFilter(new FXAAFilter());
//		postProcessor.addFilter(new ToneMapFilter(Vector3f.UNIT_XYZ.mult(4.0f)));
//      fpp.addFilter(new SSAOFilter(0.5f, 3, 0.2f, 0.2f));
	}

	@Override
	public void update() {
		try {
			super.update();
		} catch (Exception e) {
			LOG.error("出现一个未知的异常", e);
		}
	}

	@Override
	public void stop() {
		LOG.info("stop");
		super.stop();
	}

	@Override
	public void destroy() {
		LOG.info("destroy");
		super.destroy();
	}

}
