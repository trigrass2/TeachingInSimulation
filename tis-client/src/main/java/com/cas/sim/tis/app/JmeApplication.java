package com.cas.sim.tis.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.system.AppSettings;
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
		String assetPath = SpringUtil.getBean(HTTPUtils.class).getHttpUrl("assets/");
		LOG.debug("注册资源路径:{}", assetPath);
		assetManager.registerLocator(assetPath, UrlLocator.class);

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
