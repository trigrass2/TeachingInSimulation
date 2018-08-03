package com.cas.sim.tis.app;

import java.util.prefs.Preferences;

import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.consts.SettingConsts;
import com.jme3.app.FlyCamAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXApplication;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JmeApplication extends JmeToJFXApplication {

	public JmeApplication() {
		AppSettings settings = getAppSetting();
        settings.setGammaCorrection(true);
        settings.setResizable(true);

		JmeToJFXIntegrator.prepareSettings(settings, 60);
		this.setSettings(settings);
//		不显示自带的设置界面
		this.setShowSettings(false);
//		失去焦点后暂停
		this.setPauseOnLostFocus(true);
//		不显示左下角状态栏
		this.setDisplayStatView(false);
//		不显示帧数
		this.setDisplayFps(false);
	}

	@Override
	public void start() {
		log.info("start");
		super.start();
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		stateManager.attach(new MouseEventState());
		
		stateManager.detach(stateManager.getState(FlyCamAppState.class));

		renderer.setMainFrameBufferSrgb(true);
		
//		String assetPath = SpringUtil.getBean(HTTPUtils.class).getFullPath("assets/");
//		LOG.debug("注册资源路径:{}", assetPath); // http://192.168.x.x:port/***/assests/
////		注册资源路径
//		assetManager.registerLocator(assetPath, UrlLocator.class);
		assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets", FileLocator.class);
		
		cam.setFrustumPerspective(45, (float) cam.getWidth() / cam.getHeight(), 0.01f, 100f);
		

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
			log.error("出现一个未知的异常", e);
		}
	}

	@Override
	public void stop() {
		log.info("stop");
		super.stop();
	}

	@Override
	public void destroy() {
		log.info("destroy");
		super.destroy();
	}

	public AppSettings getAppSetting() {
		Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
		AppSettings settings = new AppSettings(true);
//		分辨率
		int width = prefs.getInt(SettingConsts.RESOLUTION_WIDTH, 1366);
		int height = prefs.getInt(SettingConsts.RESOLUTION_HEIGHT, 768);
		
		settings.setResolution(width, height);
		settings.setFullscreen(prefs.getBoolean(SettingConsts.SCREEN_MODE, false));
		return settings;
	}
}
