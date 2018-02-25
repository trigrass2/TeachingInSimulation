package com.cas.sim.tis.test.app;

import com.cas.sim.tis.util.AnimUtil;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class TestAnimation extends SimpleApplication {

	@Override
	public void simpleInitApp() {

		viewPort.setBackgroundColor(ColorRGBA.White);

		assetManager.registerLocator("E:\\JME_SDKPROJ_HOME\\ESimulation3D\\assets", FileLocator.class);
		Node spatial = (Node) assetManager.loadModel("Model/CJX2-12/CJX2-12.j3o");
		spatial.scale(50);
		rootNode.attachChild(spatial);

		AnimUtil.simplePlay(spatial);
	}

	public static void main(String[] args) {
		new TestAnimation().start();
	}
}
