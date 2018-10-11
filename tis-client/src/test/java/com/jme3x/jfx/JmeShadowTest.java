/*
 * Copyright (c) 2009-2012 jMonkeyEngine All rights reserved. Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme3x.jfx;

import com.cas.sim.tis.app.JmeApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowFilter;

/**
 * 渐变的外描边(更耗性能)
 * @author DING
 */
public class JmeShadowTest extends JmeApplication {

	public static void main(String[] args) {
		JmeShadowTest app = new JmeShadowTest();
		app.start();
	}

	private FilterPostProcessor fpp;
	private Node sceneModel;
	private PointLight pointLight;

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(5f);
		// light
//		AmbientLight al = new AmbientLight();
//		rootNode.addLight(al);
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-1, -4, -2).normalizeLocal());
		sun.setColor(ColorRGBA.Yellow);
		rootNode.addLight(sun);
		// model
			sceneModel = new Node();

		Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		material.setColor("Diffuse", ColorRGBA.Green);
		material.setColor("Specular", ColorRGBA.White);

		Geometry cylinder = new Geometry("Cylinder", new Cylinder(32, 16, 2, 20));
		cylinder.setMaterial(material.clone());
		cylinder.setLocalTranslation(5, 0, 0);
//		cylinder.setShadowMode(ShadowMode.CastAndReceive);
		Geometry cube = new Geometry("Cube", new Box(20, 1, 20));
		cube.setMaterial(material.clone());
		cube.setLocalTranslation(0, -5, 0);
//		cube.setShadowMode(ShadowMode.CastAndReceive);
		Geometry sphere = new Geometry("Sphere", new Sphere(32, 16, 2));
		sphere.setMaterial(material.clone());
		sphere.setLocalTranslation(-5, 0, 0);
		sceneModel.setShadowMode(ShadowMode.CastAndReceive);

		sceneModel.attachChild(cylinder);
		sceneModel.attachChild(cube);
		sceneModel.attachChild(sphere);

		assetManager.registerLocator("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\java\\com\\jme3x\\jfx", FileLocator.class);
		Spatial spatial = assetManager.loadModel("DaoJia_chaixie.j3o");
		spatial.setLocalTranslation(0, -4, 0);
		sceneModel.attachChild(spatial);

		rootNode.attachChild(sceneModel);

//		rootNode.setShadowMode(ShadowMode.Off);

		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 1024, 3);
		dlsf.setLight(sun);
		dlsf.setLambda(0.55f);
		dlsf.setShadowIntensity(0.6f);
		dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);

		// shader
		fpp = new FilterPostProcessor(assetManager);
		fpp.addFilter(dlsf);

//		viewPort.setBackgroundColor(ColorRGBA.White);

		pointLight = new PointLight();
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);

		PointLightShadowFilter filter = new PointLightShadowFilter(assetManager, 512);
		filter.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
		filter.setLight(pointLight);

		fpp.addFilter(filter);
		viewPort.addProcessor(fpp);
	}

	@Override
	public void update() {
		super.update();
		if (pointLight != null) {
			pointLight.setPosition(cam.getLocation());
		}
	}
}
