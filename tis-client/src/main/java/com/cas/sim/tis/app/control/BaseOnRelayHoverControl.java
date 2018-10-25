//package com.cas.sim.tis.app.control;
//
//import java.util.function.Consumer;
//
//import org.jetbrains.annotations.NotNull;
//
//import com.cas.sim.tis.app.event.MouseEventState;
//import com.jme3.collision.CollisionResult;
//import com.jme3.collision.CollisionResults;
//import com.jme3.input.InputManager;
//import com.jme3.math.Ray;
//import com.jme3.math.Vector3f;
//import com.jme3.renderer.Camera;
//import com.jme3.renderer.RenderManager;
//import com.jme3.renderer.ViewPort;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Spatial;
//import com.jme3.scene.control.AbstractControl;
//
//public class BaseOnRelayHoverControl extends AbstractControl {
//
//	public static final int DELAY = 2;
//	private int count;
//
//	private InputManager inputManager;
//	private Camera cam;
//	private Consumer<Spatial> consumer;
//
//	public BaseOnRelayHoverControl(Consumer<Spatial> consumer, InputManager inputManager, Camera cam) {
//		this.consumer = consumer;
//		this.inputManager = inputManager;
//		this.cam = cam;
//	}
//
//	@Override
//	protected void controlUpdate(float tpf) {
//		if (count < DELAY) {
//			count++;
//			return;
//		}
//		count = 0;
//
//		Ray ray = getRay();
//
//		CollisionResults results = new CollisionResults();
//		spatial.collideWith(ray, results);
//
//		if (results.size() > 0) {
//			Geometry geo = null;
//			for (CollisionResult collision : results) {
//				Geometry tmp = collision.getGeometry();
//
//				if (!MouseEventState.valiedate(tmp)) {
//					continue;
//				}
//				geo = tmp;
//				break;
//			}
//			if (geo == null) {
//				return;
//			}
//			consumer.accept(spatial);
//		}
//	}
//
//	@NotNull
//	private Ray getRay() {
//		Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
//		Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
//		direction.subtractLocal(origin).normalizeLocal();
//		return new Ray(origin, direction);
//	}
//
//	@Override
//	protected void controlRender(RenderManager rm, ViewPort vp) {
//
//	}
//}
