package com.cas.sim.tis.app.state;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cas.sim.tis.app.event.RawInputAdapter;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.circuit.Multimeter;
import com.cas.sim.tis.circuit.meter.FLUKE_17B;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.controller.LCDController;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.collision.CollisionResults;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.plugins.AWTLoader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

/**
 * 万用表
 * @author zzy
 */
@Slf4j
public class MultimeterState extends BaseState {
	/**
	 * 旋钮每次旋转过的角度(in radian)
	 */
	public static final float PER = (360 - 151.7f) / 9 * FastMath.DEG_TO_RAD;

	/**
	 * 万用表对象
	 */
	private Multimeter meter;
	/**
	 * 万用表视口
	 */
	private ViewPort view;
	private Camera camera;
//	万用表模型
	private Node scene;
//	旋钮
	private Node rotary, hold, range, mode, rel, hz;
	private Geometry pick;
	private AWTLoader awtLoader;
	public static final int FLAG_SNAPSHOT = 0x01;
	public static final int FLAG_DONE = 0x02;
	private int lcdRefreshFlags;
	private Image jmeImage;
	private Geometry lcd;
	private LCDController monitor;

	private Map<Spatial, BtnAction> btnActions = new HashMap<>();

	private CircuitState circuitState;

	public MultimeterState(CircuitState circuitState) {
		this.circuitState = circuitState;
	}

	@FunctionalInterface
	interface BtnAction {
		void invoke();
	}

	@Override
	protected void initializeLocal() {
//		加载万用表模型
		scene = (Node) assetManager.loadModel("Model/FLUKE-17B/FLUKE-17B.j3o");
//		设置万用表视口
		setupView(scene);
//		初始化万用表对象
		meter = new FLUKE_17B();
//		设置万用表各部分组件
		setupMeterComponent();

		awtLoader = new AWTLoader();

//		LCD界面（javafx实现）
		loadLcdUI();

		initListener();

		initBtnActionListener();
	}

	private void initBtnActionListener() {
		btnActions.put(hold, () -> {
			boolean result = meter.hold();

			Platform.runLater(() -> monitor.hold(result));
		});
		btnActions.put(range, () -> {
			boolean result = meter.range();
			if (result) {
				Platform.runLater(() -> monitor.range());
			}
		});
		btnActions.put(mode, () -> {
			boolean result = meter.function();
			if (result) {
				Platform.runLater(() -> monitor.mode());
			}
		});
		btnActions.put(rel, () -> {
//			monitor.rel();
		});
	}

	private void setupMeterComponent() {
		// 旋钮
		rotary = (Node) scene.getChild("FLUKE_17B_03");
		// hold按钮
		hold = (Node) scene.getChild("FLUKE_17B_02");
		// range按钮
		range = (Node) scene.getChild("FLUKE_17B_05");
		// rel按钮
		rel = (Node) scene.getChild("FLUKE_17B_06");
		// hz按钮
		hz = (Node) scene.getChild("FLUKE_17B_07");
		// mode按钮
		mode = (Node) scene.getChild("FLUKE_17B_04");
		// lcd显示屏
		Node lcdNode = (Node) scene.getChild("FLUKE_17B_13");
		lcd = (Geometry) lcdNode.getChild("LCD");
	}

	private void loadLcdUI() {
		Platform.runLater(() -> {
//			FIXME 这里是临时写法
			IContent content = SpringUtil.getBean(PageController.class).getIContent();
			Pane c = (Pane) content.getContent()[1];

			FXMLLoader loader = new FXMLLoader();
			try {
				Pane lcdView = loader.load(getClass().getResourceAsStream("/view/jme/MultimeterLCD.fxml"));
				monitor = loader.getController();
				monitor.setMultimeter(meter);
//				FIXME 设置lcdView界面不可见
				lcdView.setTranslateX(c.getWidth() - 500);
				c.getChildren().add(lcdView);

				lcdRefreshFlags |= FLAG_SNAPSHOT;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void setupView(Spatial scene) {
		camera = cam.clone();
//		左、右、下、上的顺序
		camera.setViewPort(0.8f, 1f, 0f, 0.5f);
//		平视
//		camera.setLocation(new Vector3f(0f, 0f, 28));
//		camera.lookAtDirection(Vector3f.UNIT_Z.negate(), Vector3f.UNIT_Y);
//		平视偏上
		camera.setLocation(new Vector3f(0f, 3f, 24.5f));
		camera.lookAtDirection(Vector3f.UNIT_Z.negate(), Vector3f.UNIT_Y);
//		camera.setLocation(new Vector3f(0f, -7f, 27));
//		camera.lookAtDirection(Vector3f.UNIT_Z.negate().add(0, FastMath.DEG_TO_RAD * 15, 0), Vector3f.UNIT_Y);
//		后倾
//		camera.setLocation(new Vector3f(0f, 10f, 28));
//		camera.lookAtDirection(Vector3f.UNIT_Z.negate().add(0, -FastMath.DEG_TO_RAD * 20, 0), Vector3f.UNIT_Y);

		camera.setFrustumPerspective(45, (float) camera.getWidth() * .4f / camera.getHeight(), 10f, 100f);
		camera.setParallelProjection(false);

		view = renderManager.createMainView("Bottom Right", camera);
		view.setClearFlags(false, true, true);

		view.attachScene(scene);
	}

	private void initListener() {
		inputManager.addRawInputListener(new RawInputAdapter() {
			@Override
			public void onMouseMotionEvent(MouseMotionEvent evt) {
				if (!isEnabled()) {
					return;
				}
				onMouseMotionEvent0(evt);
			}

			@Override
			public void onMouseButtonEvent(MouseButtonEvent evt) {
				if (!isEnabled() || pick == null) {
					return;
				}
				onMouseButtonEvent0(evt);
			}
		});
	}

	protected void onMouseButtonEvent0(MouseButtonEvent evt) {
		Node button = null;
		if (pick.hasAncestor(hold)) {
			button = hold;
		} else if (pick.hasAncestor(range)) {
			button = range;
		} else if (pick.hasAncestor(rel)) {
			button = rel;
		} else if (pick.hasAncestor(hz)) {
			button = hz;
		} else if (pick.hasAncestor(mode)) {
			button = mode;
		}
		if (button != null) {
			if (evt.isPressed()) {
				button.move(0, -0.3f, 0);
				try {
					BtnAction action = btnActions.get(button);
					if (action != null) {
						action.invoke();
						lcdRefreshFlags |= FLAG_SNAPSHOT;
					}
				} catch (Exception e) {
					log.error("万用表按钮{}按下时出现了异常{}", button, e);
				}
			} else if (evt.isReleased()) {
				button.move(0, 0.3f, 0);
			}
		}
		evt.setConsumed();
	}

	protected void onMouseMotionEvent0(MouseMotionEvent evt) {
		pick();

		if (pick == null) {
			return;
		}
		if (pick.hasAncestor(rotary)) {
//			鼠标滚轮向上滑动， 旋钮顺时针旋转，万用表切换至下一档位
			if (evt.getDeltaWheel() > 0 && meter.hasNextGear()) {
//				万用表播动档位
				meter.rotary(1);
//				旋钮模型旋转到对应位置
				rotary.rotate(0, -PER, 0);
//				屏幕上显示对应内容

				Platform.runLater(() -> monitor.update());
//				标记此时需要更新lcd内容
				lcdRefreshFlags |= FLAG_SNAPSHOT;
			} else if (evt.getDeltaWheel() < 0 && meter.hasPreGear()) {
//			鼠标滚轮向下滑动， 旋钮逆时针旋转，万用表切换至上一档位
				meter.rotary(-1);
				rotary.rotate(0, PER, 0);
				Platform.runLater(() -> monitor.update());
				lcdRefreshFlags |= FLAG_SNAPSHOT;
			}
		}
		evt.setConsumed();
	}

	protected void pick() {
		Vector2f click2d = null;
		if (inputManager.isCursorVisible()) {
			click2d = inputManager.getCursorPosition();
		} else {
			click2d = new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2);
		}
		Vector3f click3d = camera.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
		Vector3f dir = camera.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtract(click3d).normalizeLocal();

		Ray ray = new Ray(click3d, dir);
		CollisionResults results = new CollisionResults();
		scene.collideWith(ray, results);

		if (results.size() > 0) {
			pick = results.getClosestCollision().getGeometry();
		} else {
			pick = null;
		}
	}

	@Override
	public void update(float tpf) {
		if ((lcdRefreshFlags & FLAG_SNAPSHOT) != 0) {
			updateLCD();
			lcdRefreshFlags &= ~FLAG_SNAPSHOT;
			log.debug("需要更新LCD");
		}

		if ((lcdRefreshFlags & FLAG_DONE) != 0) {
			lcd.getMaterial().getTextureParam("DiffuseMap").getTextureValue().setImage(jmeImage);
			lcdRefreshFlags &= ~FLAG_DONE;
			log.info("完成更新LCD");
		}

		camera.setLocation(new Vector3f(0f, 0f, 30));
		camera.lookAtDirection(Vector3f.UNIT_Z.negate(), Vector3f.UNIT_Y);

		scene.updateLogicalState(tpf);
		scene.updateGeometricState();
		super.update(tpf);
	}

	private void updateLCD() {
		Platform.runLater(() -> {
			try {
				jmeImage = awtLoader.load(monitor.snapshot(), true);
				lcdRefreshFlags |= FLAG_DONE;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		view.setEnabled(enabled);
	}
}
