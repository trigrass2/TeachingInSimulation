package com.cas.sim.tis.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3x.jfx.injfx.JmeToJFXApplication;

@Component
public class JmeApplication extends JmeToJFXApplication {
	public static final Logger LOG = LoggerFactory.getLogger(JmeApplication.class);

	protected Geometry player;

	public JmeApplication() {
	}
	
	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		
		inputManager.setCursorVisible(true);
//		鼠标拖拽旋转相机
		flyCam.setDragToRotate(true);
		
		Box b = new Box(1, 1, 1);
		player = new Geometry("Player", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		player.setMaterial(mat);
		rootNode.attachChild(player);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		player.rotate(0, 0, tpf);
	}
	
	@Override
	public void update() {
		try {
			super.update();
		} catch (Exception e) {
			LOG.error("出现一个未知的异常", e);
		}
	}
}
