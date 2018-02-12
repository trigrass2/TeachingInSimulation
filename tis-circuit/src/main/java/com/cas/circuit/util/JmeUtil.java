package com.cas.circuit.util;

import java.util.List;

import com.cas.util.StringUtil;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

public final class JmeUtil {

	public static final String MOUSE_TRANSPARENT = "MouseTransparent";

	/**
	 * @param value eg.(-0.017576016, 0.011718482, -0.99977684)
	 * @return
	 */
	public static Vector3f parseVector3f(String value) {
		if (value == null) {
			return null;
		}

		value = trim(value);

		String[] arr = value.split(",");

		Vector3f result = new Vector3f();
		result.x = Float.parseFloat(arr[0]);
		result.y = Float.parseFloat(arr[1]);
		result.z = Float.parseFloat(arr[2]);

		return result;
	}

	/**
	 * @param value eg.(-0.017576016, 0.011718482, -0.99977684, 1)
	 * @return
	 */
	public static Quaternion parseQuaternion(String value) {
		if (value == null) {
			return null;
		}

		value = trim(value);
		String[] arr = value.split(",");

		Quaternion result = new Quaternion(//
				Float.parseFloat(arr[0]), //
				Float.parseFloat(arr[1]), //
				Float.parseFloat(arr[2]), //
				Float.parseFloat(arr[3])//
		);

		return result;
	}

	public static String[] parseArray(String value) {
		if (value == null) {
			return null;
		}

		value = trim(value);
		String[] arr = value.split(",");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	public static float[] parseFloatArray(String value) {
		String[] arr = parseArray(value);

		float[] result = new float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			result[i] = Float.parseFloat(arr[i]);
		}

		return result;
	}

	private static String trim(String value) {
		if (value == null) {
			return null;
		}
//		这个方法的局限性,只能针对形如：(1,2,3)|[1,2,3]的字符串
//		无法处理[[1,2],[3,4]]|((1,2),(3,4)),这样的字符串建议用用JSON,或StringUtil
		value = value.replace("(", "");
		value = value.replace(")", "");
		value = value.replace("[", "");
		value = value.replace("]", "");

		value = value.trim();
		return value;
	}

	/**
	 * Get a context point on spatial from cursor position.
	 * @param spatial the spatial.
	 * @return the contact point or null.
	 */
	
	public static Vector3f getContactPointFromCursor(final Spatial spatial, final Camera camera, InputManager inputManager) {
		final Vector2f cursor = inputManager.getCursorPosition();
		return getContactPointFromScreenPos(spatial, camera, cursor.getX(), cursor.getY());
	}

	/**
	 * Get a context point on spatial from screen position.
	 * @param spatial the spatial.
	 * @param screenX the screen X coord.
	 * @param screenY the screen Y coord.
	 * @return the contact point or null.
	 */
	
	public static Vector3f getContactPointFromScreenPos(final Spatial spatial, final Camera camera, final float screenX, final float screenY) {
		final CollisionResult collision = getCollisionFromScreenPos(spatial, camera, screenX, screenY);
		return collision == null ? null : collision.getContactPoint();
	}

	
	public static Node getNodeFromCursor(final Spatial spatial, final Camera camera, InputManager inputManager) {
		Geometry geom = getGeometryFromCursor(spatial, camera, inputManager);
		if (geom != null) {
			return geom.getParent();
		}
		return null;
	}

	/**
	 * Get a geometry on spatial from cursor position.
	 * @param spatial the spatial.
	 * @return the geometry or null.
	 */
	
	public static Geometry getGeometryFromCursor(final Spatial spatial, final Camera camera, InputManager inputManager) {
		final Vector2f cursor = inputManager.getCursorPosition();
		return getGeometryFromScreenPos(spatial, camera, cursor.getX(), cursor.getY());
	}

	
	public static Node getNodeFromScreenPos(final Spatial spatial, final Camera camera, final float screenX, final float screenY) {
		Geometry geom = getGeometryFromScreenPos(spatial, camera, screenX, screenY);
		if (geom != null) {
			return geom.getParent();
		}
		return null;
	}

	/**
	 * Get a geometry on spatial from screen position.
	 * @param spatial the spatial.
	 * @param screenX the screen X coord.
	 * @param screenY the screen Y coord.
	 * @return the geometry or null.
	 */
	
	public static Geometry getGeometryFromScreenPos(final Spatial spatial, final Camera camera, final float screenX, final float screenY) {
		final CollisionResult collision = getCollisionFromScreenPos(spatial, camera, screenX, screenY);
		return collision == null ? null : collision.getGeometry();
	}

	/**
	 * Get a collision on spatial from screen position.
	 * @param spatial the spatial.
	 * @param screenX the screen X coord.
	 * @param screenY the screen Y coord.
	 * @return the collision or null.
	 */
	
	public static CollisionResult getCollisionFromScreenPos(final Spatial spatial, final Camera camera, InputManager inputManager) {
		final Vector2f cursor = inputManager.getCursorPosition();
		return getCollisionFromScreenPos(spatial, camera, cursor.getX(), cursor.getY());
	}
	
	public static CollisionResult getCollisionFromScreenPos(final Spatial spatial, final Camera camera, final float screenX, final float screenY) {

		final Vector2f cursor = new Vector2f(screenX, screenY);
		final Vector3f click3d = camera.getWorldCoordinates(cursor, 0f);
		final Vector3f dir = camera.getWorldCoordinates(cursor, 1f).subtractLocal(click3d).normalizeLocal();

		final Ray ray = new Ray();
		ray.setOrigin(click3d);
		ray.setDirection(dir);

		final CollisionResults results = new CollisionResults();

		spatial.updateModelBound();
		spatial.collideWith(ray, results);

		if (results.size() < 1) {
			return null;
		}

		for (CollisionResult collisionResult : results) {
			if (collisionResult.getGeometry().getCullHint() == CullHint.Always) {
				continue;
			}
			Boolean mouseTransparent = collisionResult.getGeometry().getUserData(MOUSE_TRANSPARENT);
			if (mouseTransparent != null && mouseTransparent.booleanValue()) {
				continue;
			}

			return collisionResult;
		}

		return null;
	}

	public static void setMouseTransparent(Spatial spatial, boolean transparent) {
		if (spatial != null) {
			spatial.setUserData(MOUSE_TRANSPARENT, transparent);
		}

		if (spatial instanceof Node) {
			((Node) spatial).getChildren().forEach(child -> setMouseTransparent(child, transparent));
		}
	}

	/**
	 * 模型半透明，模型材质必须含有Diffuse
	 * @param sp
	 * @param alpha
	 */
	public static void transparent(Spatial sp, float alpha) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = ((Geometry) sp).getMaterial();
			sp.setUserData("Transparent", mat);

			Material transMat = mat.clone();
			MatParam diffuseParam = transMat.getParam("Diffuse");
			if (diffuseParam != null) {
				ColorRGBA color = (ColorRGBA) transMat.getParam("Diffuse").getValue();
				transMat.setColor("Diffuse", new ColorRGBA(color.r, color.g, color.b, alpha));
				transMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

				sp.setMaterial(transMat);
				sp.setQueueBucket(Bucket.Transparent);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				transparent(child, alpha);
			}
		}
	}

	/**
	 * 取消透明效果
	 * @param sp
	 */
	public static void untransparent(Spatial sp) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = sp.getUserData("Transparent");
			if (mat != null) {
				sp.setUserData("Transparent", null);
				sp.setMaterial(mat);
				sp.setQueueBucket(Bucket.Opaque);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				untransparent(child);
			}
		}
	}

	/**
	 * 改变材质颜色
	 * @param sp
	 * @param color
	 */
	public static void color(Spatial sp, ColorRGBA color, boolean saveMat) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = ((Geometry) sp).getMaterial();
			if (saveMat) {
				sp.setUserData("Color", mat);
			}

			Material colorMat = mat.clone();
			MatParam diffuseParam = colorMat.getParam("Diffuse");
			if (diffuseParam != null) {
				colorMat.setColor("Diffuse", color);
				sp.setMaterial(colorMat);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				color(child, color, saveMat);
			}
		}
	}

	/**
	 * 恢复材质本来颜色
	 * @param sp
	 */
	public static void uncolor(Spatial sp) {
		if (sp == null) {
			return;
		}
		if (sp instanceof Geometry) {
			Material mat = sp.getUserData("Color");
			if (mat != null) {
				sp.setUserData("Color", null);
				sp.setMaterial(mat);
			}
		} else if (sp instanceof Node) {
			for (Spatial child : ((Node) sp).getChildren()) {
				uncolor(child);
			}
		}
	}

	public static String getSpatailPath(Spatial sp, Node parent) {
		StringBuffer buf = new StringBuffer();
		getSpatailPath0(sp, parent, buf);
		return buf.toString();
	}

	private static void getSpatailPath0(Spatial sp, Node parent, StringBuffer buf) {
		if (sp == null) {
			return;
		}
		if (sp == parent) {
			return;
		}
		buf.insert(0, sp.getName());

		if (sp.getParent() != parent) {
			buf.insert(0, "-");
		}
		getSpatailPath0(sp.getParent(), parent, buf);
	}

	public static Spatial findByPath(Node elevatorNode, String mdlPath) {
		List<String> nameList = StringUtil.split(mdlPath, '-');
		String name = null;
		Node node = elevatorNode;
		try {
			for (int i = 0; i < nameList.size(); i++) {
				name = nameList.get(i);
				node = (Node) node.getChild(name);
			}
		} catch (Exception e) {
			node = null;
			e.printStackTrace();
			return null;
		}
		return node;
	}

}
