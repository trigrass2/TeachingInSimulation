package com.cas.sim.tis.test.app;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext.Type;

public class FtpAssetAccess extends SimpleApplication {

	private Spatial player;

	public static void main(String[] args) {
		new FtpAssetAccess().start(Type.Display);
	}

	@Override
	public void simpleInitApp() {
		inputManager.setCursorVisible(true);
//		鼠标拖拽旋转相机
		flyCam.setDragToRotate(true);

		assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets", FileLocator.class);
//		assetManager.registerLocator("http://192.168.1.19:8082/assets/", UrlLocator.class);
		player = assetManager.loadModel("Model\\Desktop\\desktop.j3o");

		System.out.println("JmeApplication.simpleInitApp()" + player);
		rootNode.attachChild(player);

		rootNode.addLight(new DirectionalLight());
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
//		player.rotate(0, 0, tpf);
	}

}
