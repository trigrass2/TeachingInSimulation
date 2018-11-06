package com.cas.sim.tis.app.state;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.hold.ElecCompHoldHandler;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.SceneCameraState.Mode;
import com.cas.sim.tis.app.state.SceneCameraState.View;
import com.cas.sim.tis.consts.CaseMode;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.asset.ModelKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3x.jfx.util.JFXPlatform;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ElecCaseState<T>extends BaseState {
//	每次打开新的案例，都会创建一个新的节点，存放与案例相关的模型
	private static final String CASE_ROOT_NODE = "CASE_ROOT_NODE";

	private @Getter Node root;
	private PointLight pointLight;

	protected @Getter Spatial compPlane;
	protected @Getter SceneCameraState cameraState;
	protected @Getter @Setter ElecCase3D<T> ui;
	protected @Getter CircuitState circuitState;
	protected @Getter T elecCase;
	protected @Getter CaseMode mode;

	private Queue<Runnable> onChangedListener = new ConcurrentLinkedQueue<>();

	@Override
	protected void initializeLocal() {
		arrangementScene();

		bindEvents();

		// 添加操作模式State
		stateManager.attach(cameraState = new SceneCameraState());

		addMapping("toggleView", new KeyTrigger(KeyInput.KEY_CAPITAL));
		addListener((ActionListener) (name, pressed, value) -> {
			if ("toggleView".equals(name) && pressed) {
				if (cameraState.getMode() == Mode.Ortho) {
					switchTo3D();
				} else {
					switchTo2D();
				}
			}
		}, "toggleView");

		HoldStatePro.ins.setRoot(root);
		HoldStatePro.ins.registerWithInput(inputManager);

		// 结束加载界面
		JFXPlatform.runInFXThread(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	@Override
	public void update(float tpf) {
		pointLight.setPosition(cam.getLocation());

//		手中无物品，相机才能缩放
		cameraState.setZoomEnable(HoldStatePro.ins.isIdle());

		Runnable l;
		if ((l = onChangedListener.poll()) != null) {
			log.info("存档或模式发生变化..");
			l.run();
		}

		super.update(tpf);
	}

	@Override
	public void cleanup() {
		// 移除所有模型
		rootNode.detachAllChildren();
		// 移除操作模式State
		stateManager.detach(cameraState);
		stateManager.detach(circuitState);

		HoldStatePro.ins.unregisterInput();

		super.cleanup();
	}

	public void switchTo2D() {
		cameraState.toggleOrthoPerspMode(Mode.Ortho);
		cameraState.switchToView(View.Top);
	}

	public void switchTo3D() {
		cameraState.toggleOrthoPerspMode(Mode.Persp);
	}

//	布置场景
	private void arrangementScene() {
		// 0、新建场景节点
		root = new Node(CASE_ROOT_NODE);
		rootNode.attachChild(root);
		// 1、加载工作台模型
		Node plane = (Node) loadAsset(new ModelKey("Model/Desktop/plane.j3o"));
		root.attachChild(plane);
		this.compPlane = plane.getChild("COMP-PLANE");
		// 2、添加灯光
		pointLight = new PointLight();
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	private void bindEvents() {
		/*
		 * 左键点击桌面，放置手中的元器件 右键点击桌面，取消手中的元器件
		 */
		addListener(compPlane, new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HoldStatePro.ins.putDown();
			}

			@Override
			public void mouseRightClicked(MouseEvent e) {
				HoldStatePro.ins.discard();
			}
		});
	}

	public void hold(ElecComp elecComp) {
		if (mode == null) {
			return;
		}
		if (mode.isHoldEnable()) {
//			只要当前模式下允许拿东西，二话不说，先将元器件的模型加载出来
			Spatial spatial = loadAsset(new ModelKey(elecComp.getMdlPath()));
//			设置当前元器件的模型对象
			elecComp.setSpatial(spatial);

//			拿之前先检查一下手中是否有东西，如果有，则先丢弃
			if (!HoldStatePro.ins.isIdle()) {
				HoldStatePro.ins.discard();
			}

//			最后就能光明正大地拿元器件了
			HoldStatePro.ins.pickUp(spatial, new ElecCompHoldHandler(this, elecComp));
		}
	}

	public void putDownOnBase(Spatial def) {
		if (CaseMode.VIEW_MODE == mode || circuitState == null) {
			return;
		}
		HoldStatePro.ins.putDownOn(def);
	}

	public boolean isClean() {
		if (CaseMode.VIEW_MODE == mode || circuitState == null) {
			return true;
		} else {
			return circuitState.isClean();
		}
	}

	public abstract void save();

	public abstract void newCase();

	public final void setupCase(T elecCase, CaseMode mode) {
		if (this.elecCase == elecCase && this.mode == mode) {
//			既然存档和模式都没变化，大家就当无事发生过 :)
			return;
		}
		this.elecCase = elecCase;
		this.mode = mode;
		onChangedListener.add(() -> setupCase0());
	}

	protected abstract void setupCase0();

	public void setMultimeterVisible(boolean visible) {
	}
}
