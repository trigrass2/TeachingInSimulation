package com.cas.sim.tis.app.control;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.cas.sim.tis.app.event.MouseEventState;
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

	public static final int DELAY = 2;
	private int count;

	private InputManager inputManager;
	private Camera cam;
	private Consumer<String> consumer;

	// 是否显示认知信息
	private boolean showRecongnize;
	
	private Geometry wire;

	public ShowNameOnHoverControl(Consumer<String> consumer, InputManager inputManager, Camera cam) {
		this(consumer, inputManager, cam, false);
	}

	public ShowNameOnHoverControl(Consumer<String> consumer, InputManager inputManager, Camera cam, boolean showRecongnize) {
		this.inputManager = inputManager;
		this.cam = cam;
		this.consumer = consumer;
		this.showRecongnize = showRecongnize;
	}

	@Override
	protected void controlUpdate(float tpf) {
		if (count < DELAY) {
			count++;
			return;
		}
		count = 0;

		Ray ray = getRay();

		CollisionResults results = new CollisionResults();
		spatial.collideWith(ray, results);

		if (results.size() > 0) {
			Geometry geo = null;
			for (CollisionResult collision : results) {
				Geometry tmp = collision.getGeometry();

				if (!MouseEventState.valiedate(tmp)) {
					continue;
				}
				geo = tmp;
				break;
			}
			if (geo == null) {
				consumer.accept(null);
				return;
			}
			Object recongnize = findEntity(geo, "recongnize");
			if (showRecongnize && recongnize != null) {
				consumer.accept(String.valueOf(recongnize));
				return;
			}
//			geo 或者是其parent节点，但不是spatial的模型，userdata中必有entity属性
			Object entity = findEntity(geo, "entity");
			if (entity == null) {
				return;
			}
			@Nonnull
			String name = (String) Util.invokeGetMethod(entity, "name");

			consumer.accept(name);
		} else {
			consumer.accept(null);
		}
	}

//	从实体中获取entity
	@NotNull
	private Object findEntity(Spatial geo, String key) {
		if (geo == spatial) {
//			throw new RuntimeException("没有找到Entity");
			return null;
		}

		Object entity = geo.getUserData(key);
		if (entity != null) {
			return entity;
		}
		return findEntity(geo.getParent(), key);
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
