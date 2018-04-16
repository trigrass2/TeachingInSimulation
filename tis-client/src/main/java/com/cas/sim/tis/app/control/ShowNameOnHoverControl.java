package com.cas.sim.tis.app.control;

import org.jetbrains.annotations.NotNull;

import com.cas.sim.tis.app.state.CircuitState;
import com.cas.util.Util;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class ShowNameOnHoverControl extends AbstractControl {

	private InputManager inputManager;
	private Camera cam;
	private CircuitState circuitState;

	public ShowNameOnHoverControl(CircuitState circuitState, InputManager inputManager, Camera cam) {
		this.circuitState = circuitState;
		this.inputManager = inputManager;
		this.cam = cam;
	}

	@Override
	protected void controlUpdate(float tpf) {
		Ray ray = getRay();

		CollisionResults results = new CollisionResults();
		spatial.collideWith(ray, results);

		if (results.size() > 0) {
			CollisionResult closest = results.getClosestCollision();
			Geometry geo = closest.getGeometry();
//			geo 或者是其parent节点，但不是spatial的模型，userdata中必有entity属性
			Object entity = findEntity(geo);
			@NotNull
			String name = (@NotNull String) Util.getFieldValue(entity, "name");

			circuitState.showName(name);
		}else {
			circuitState.showName(null);
		}
	}

//	从实体中获取entity
	@NotNull
	private Object findEntity(Spatial geo) {
		if (geo == spatial) {
			throw new RuntimeException("没有找到Entity");
		}

		Object entity = geo.getUserData("entity");
		if (entity != null) {
			return entity;
		}
		return findEntity(geo.getParent());
	}

	@NotNull
	private Ray getRay() {
		Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
		Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
		direction.subtractLocal(origin).normalizeLocal();
		return new Ray(origin, direction);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
