package com.cas.sim.tis.test.app;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.scene.Spatial;

public class FtpAssetAccess extends SimpleApplication {

	private Spatial player;

	public static void main(String[] args) {
		new FtpAssetAccess().start();
	}
	
	@Override
	public void simpleInitApp() {
		inputManager.setCursorVisible(true);
//		鼠标拖拽旋转相机
		flyCam.setDragToRotate(true);

		assetManager.registerLocator("ftp://192.168.1.19/assets/", UrlLocator.class);
		player = assetManager.loadModel("Model/11.j3o");

		System.out.println("JmeApplication.simpleInitApp()" + player);
		rootNode.attachChild(player);
		
		rootNode.addLight(new DirectionalLight());
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		System.out.println("FtpAssetAccess.simpleUpdate()");
		player.rotate(0, 0, tpf);
	}

}
