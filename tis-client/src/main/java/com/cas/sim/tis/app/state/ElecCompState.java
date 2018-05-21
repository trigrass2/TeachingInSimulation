package com.cas.sim.tis.app.state;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.anno.JmeThread;
import com.cas.sim.tis.app.control.ShowNameOnHoverControl;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.AnimUtil;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.util.StringUtil;
import com.jme3.asset.ModelKey;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
/**
 * 元器件认知模块
 * @author Administrator
 *
 */
@Slf4j
public class ElecCompState extends BaseState {

	private static final String ROOT_NAME = "ELEC_COMP_ROOT";
	private Node root;
	private boolean transparent;
	private boolean explode;
	private List<Spatial> shells = new ArrayList<>();
	private boolean autoRotate;
	private float scale = 1;
	private Recongnize3D ui;
	private PointLight pointLight;
	private MyCameraState chaserState;

	@Override
	protected void initializeLocal() {
//		认知模块的根节点
		root = new Node(ROOT_NAME);
		root.addControl(new ShowNameOnHoverControl((name) -> {
			Vector2f point = inputManager.getCursorPosition();
			Platform.runLater(() -> ui.showName(name, point.getX(), point.getY()));
		}, inputManager, cam));
		log.debug("创建元器件状态机的根节点{}", root.getName());
		rootNode.attachChild(root);

		setupLight();

		stateManager.attach(chaserState = new MyCameraState());

////		PBR indirect lighting
//		final EnvironmentCamera envCam = new EnvironmentCamera(256, new Vector3f(0, 3f, 0));
//		stateManager.attach(envCam);
//
//		stateManager.attach(new BreakPointState((a) -> {
//			LightProbe probe = LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode, new JobProgressAdapter<LightProbe>() {
//				@Override
//				public void done(LightProbe result) {
////					加载结束
//					Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
//				}
//			});
//			((BoundingSphere) probe.getBounds()).setRadius(100);
//			rootNode.addLight(probe);
//		}));

//		加载结束
		Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
	}

	private void setupLight() {
		pointLight = new PointLight();
//		dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		pointLight.setColor(ColorRGBA.White);
		rootNode.addLight(pointLight);
	}

	@JmeThread
	public void setElecComp(ElecComp elecComp) {
//		clean up
		cleanRoot();
//		加载模型(认知模块使用AnimPath的model)
		Spatial model = loadAsset(new ModelKey(elecComp.getAnimPath()));

//		PBR能在系统中被照亮
//		MikktspaceTangentGenerator.generate(model);
//		将模型放大100倍
		model.scale(100);
		root.attachChild(model);
//		
//		获取相应元器件
		ElecCompDef elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());

//		找出元器件外壳模型名称
		loadShell(elecCompDef.getParam(ElecCompDef.PARAM_KEY_SHELL));

//		FIXME 这里应更为精准地设置为元器件模型。
		elecCompDef.bindModel(root);
		
		model.setUserData("entity", elecCompDef);

////		添加事件
//		Map<Spatial, String> nameMap = collectName(elecCompDef);
//
//		nameMap.entrySet().forEach(e -> addListener(e.getKey(), new MouseEventAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent evt) {
//				if (!pickEnable) {
//					return;
//				}
////				FilterUtil.showOutlineEffect(evt.getSpatial());
//				System.out.println("ElecCompState.setElecComp(...).new MouseEventAdapter() {...}.mouseClicked()");
//
//			}
//		}));

		explode0();
	}

	private void cleanRoot() {
		shells.clear();
		log.debug("移除{}中所有模型", root.getName());
		root.detachAllChildren();
	}

//	@param shell 元器件的外壳（由1个或多个节点组成）
	private void loadShell(String shell) {
		if (shell == null) {
			return;
		}
		StringUtil.split(shell, ',').forEach(s -> {
//			找出元器件外壳模型
			Spatial shellNode = root.getChild(s);
			if (shellNode != null) {
				shells.add(shellNode);
			} else {
				log.error("模型{}中没有名为{}的节点", root, s);
			}
		});

		transparentShell0();
	}

	@Override
	public void cleanup() {
		boolean result = root.removeFromParent();
		if (result) {
			log.debug("删除根节点{}", root.getName());
		} else {
			log.warn("删除根节点{}失败", root.getName());
		}

		shells.clear();
		super.cleanup();
	}

	@Override
	public void update(float tpf) {
		if (autoRotate) {
			root.rotate(0, tpf / 2, 0);
		}
		pointLight.setPosition(cam.getLocation());
		root.setLocalScale(scale);
		super.update(tpf);
	}

	public void explode(@Nonnull Boolean n) {
		this.explode = n.booleanValue();
		explode0();
	}

	@JmeThread
	private void explode0() {
		if (explode) {
			AnimUtil.simplePlay(root);
		} else {
			AnimUtil.animReset(root);
		}
	}

	public void transparent(@Nonnull Boolean n) {
		this.transparent = n.booleanValue();
		app.enqueue(() -> {
			transparentShell0();
		});
	}

	@JmeThread
	private void transparentShell0() {
		if (transparent) {
			shells.forEach(shell -> JmeUtil.transparent(shell, .7f));
		} else {
			shells.forEach(shell -> JmeUtil.untransparent(shell));
		}
	}

	public void autoRotate(Boolean n) {
		this.autoRotate = n.booleanValue();
	}

	public void reset() {
		if (chaserState.isInitialized()) {
			chaserState.getChaser().setLookAtOffset(Vector3f.ZERO);
		}
		scale = 1;
	}

	public void zoomIn() {
		scale *= 1.1;
	}

	public void zoomOut() {
		scale /= 1.1;
	}

	public void setUI(Recongnize3D ui) {
		this.ui = ui;
	}
}
