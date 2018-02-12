package com.cas.sim.tis.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;
import com.jme3x.jfx.injfx.JmeToJFXApplication;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

public class JmeApplication extends JmeToJFXApplication {
	public static final Logger LOG = LoggerFactory.getLogger(JmeApplication.class);

	public JmeApplication() {
		final AppSettings settings = JmeToJFXIntegrator.prepareSettings(new AppSettings(true), 60);
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
//		注册资源路径
//		String assetPath = SpringUtil.getBean(HTTPUtils.class).getHttpUrl("assets/");
//		LOG.debug("注册资源路径:{}", assetPath);
//		assetManager.registerLocator(assetPath, UrlLocator.class);
		assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets", FileLocator.class);

//		Spatial sky = SkyFactory.createSky(assetManager, "Model/Sky/noon_grass_2k.hdr", SkyFactory.EnvMapType.EquirectMap);
		Spatial sky = SkyFactory.createSky(assetManager, "Model/Sky/Path.hdr", SkyFactory.EnvMapType.EquirectMap);
		rootNode.attachChild(sky);

		final EnvironmentCamera envCam = new EnvironmentCamera(256, new Vector3f(0, 3f, 0));
		stateManager.attach(envCam);

		Spatial model = assetManager.loadModel("Model\\Desktop\\desktop.j3o");
		MikktspaceTangentGenerator.generate(model);
		rootNode.attachChild(model);

		super.simpleInitApp();
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
