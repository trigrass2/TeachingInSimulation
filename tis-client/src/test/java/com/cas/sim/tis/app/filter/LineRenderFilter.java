package com.cas.sim.tis.app.filter;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;

public class LineRenderFilter extends Filter {
//	private static final float WIRE_LENGTH = 0.014388919f;
	private Float lineWidthPx;
	private ColorRGBA lineColor;
//	private Vector3f oriPoint;
	private Camera cam;
	private InputManager inputManager;
	private Vector2f aimPos;
	private boolean followMouse = true;
	private Spatial[] endSpatials;
	private Spatial oriSpatial;

	public LineRenderFilter(Float lineWidthPx, InputManager inputManager) {
		super("BorderFilter");
		this.lineWidthPx = lineWidthPx;
		this.inputManager = inputManager;
	}

	@Override
	protected void initFilter(AssetManager assetManager, RenderManager renderManager, ViewPort vp, int w, int h) {
		MaterialDef matDef = (MaterialDef) assetManager.loadAsset("Shaders/lineRender/lineRender.j3md");
		material = new Material(matDef);
		material.setColor("LineColor", lineColor);
		material.setFloat("LineWidthPx", lineWidthPx);
		cam = vp.getCamera();
		material.setVector2("Resolution", new Vector2f(w, h));
		aimPos = new Vector2f(w / 2f, h / 2f);
//		this.renderManager = renderManager;
//		material.setVector2("OriPoint", new Vector2f(1, 1));
//		material.setVector2("EndPoint", new Vector2f(2, 1));
//		setEndPoint2f(new Vector2f(2, 1));
//		Vector3f[] endPos3fArray = new Vector3f[3];
//		endPos3fArray[0] = new Vector3f(0.34700036f, 0.6532851f, 0.68671185f);
//		endPos3fArray[1] = new Vector3f(0.34700036f, 0.6560309f, 0.67800313f);
//		endPos3fArray[2] = new Vector3f(0.34700036f, 0.65298283f, 0.66939557f);
//		showLineWithOutMouse(new Vector3f(0.36905527f, 0.6560331f, 0.67825425f), endPos3fArray);
	}

	@Override
	protected Material getMaterial() {
		return material;

	}

	@Override
	protected void preFrame(float tpf) {
		super.preFrame(tpf);
		if (oriSpatial == null) {
			return;
		}

		material.setColor("LineColor", lineColor);

		Vector3f oriScreenCoordi = cam.getScreenCoordinates(findEndPos(oriSpatial));
		material.setVector2("OriPoint", new Vector2f(oriScreenCoordi.x, oriScreenCoordi.y));
		if (followMouse) {
			Vector2f cursorPosition;
			if (inputManager.isCursorVisible()) {
				cursorPosition = inputManager.getCursorPosition();
			} else {
				cursorPosition = aimPos;
			}
			setEndPoint2f(cursorPosition);
		} else {
			if (endSpatials != null) {
				Vector2f[] endPos2fArray = new Vector2f[endSpatials.length];
				for (int i = 0; i < endSpatials.length; i++) {
					Vector3f endScreenCoordi = cam.getScreenCoordinates(findEndPos(endSpatials[i]));
					endPos2fArray[i] = new Vector2f(endScreenCoordi.x, endScreenCoordi.y);
				}
				setEndPoint2f(endPos2fArray);
			}
		}
	}

	private void setEndPoint2f(Vector2f... endPoint) {
//		System.out.println(endPoint.x + ";" + endPoint.y);
//		material.setVector2("EndPoint", endPoint);
//		Vector2f[] endPoints = { endPoint };
		if (endPoint == null) {
//			System.out.println(endPoint);
			return;
		}
		material.setInt("EndNum", endPoint.length);
		material.setParam("EndPoints", VarType.Vector2Array, endPoint);
	}

//	public void setOriPoint3f(Vector3f oriPoint) {
//		this.oriPoint = oriPoint;
//	}

	public void setOriSpatial(Spatial sp) {
		this.oriSpatial = sp;
	}

	public void setColor(ColorRGBA color) {
		if (color == null) {
			System.out.println("LineRenderFilter.setColor( null )");
		}
		this.lineColor = color;
	}

//	public void setEndPoint3f(Vector3f endPoint) {
//		this.endPoint = endPoint;
//	}

	public void setFollowMouse(boolean followMouse) {
		this.followMouse = followMouse;
	}

	public void showLineWithOutMouse(Spatial oriSpatial, Spatial... endSpatialfArray) {
//		System.out.println(oriPos + "......end" + endPos3fArray.length);
		setOriSpatial(oriSpatial);
		this.endSpatials = endSpatialfArray;
	}

	/**
	 * 获取模型末端位置
	 * @param model
	 * @return
	 */
	private Vector3f findEndPos(Spatial model) {
//		float length = ((BoundingBox) model.getWorldBound()).getYExtent() * 2;
//		float length = WIRE_LENGTH;

		String lineLocation = model.getUserData("LineLocation");
		if (lineLocation == null) {
			lineLocation = ((Node) model).getChild(0).getUserData("LineLocation");
		}
		if (lineLocation == null) {
			throw new RuntimeException(model.getName() + "模型的userdata中没有存入LineLocation的值");
		}

		String[] lineLocations = lineLocation.split(",");
		Vector3f termloc = new Vector3f(Float.parseFloat(lineLocations[0]), Float.parseFloat(lineLocations[1]), Float.parseFloat(lineLocations[2]));
		termloc = termloc.mult(model.getWorldScale());
		Vector3f point = model.getWorldRotation().toRotationMatrix().mult(termloc);
		point.setZ(point.getZ());
		Vector3f point1 = point.add(model.getWorldTranslation());
		return point1;
	}

}
