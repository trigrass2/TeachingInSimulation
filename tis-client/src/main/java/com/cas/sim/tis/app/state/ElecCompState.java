package com.cas.sim.tis.app.state;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cas.sim.tis.util.JmeUtil;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResult;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.util.SkyFactory;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;

public class ElecCompState extends BaseState {

	private static final String ROOT_NAME = "ELEC_COMP_ROOT";

	private Node root;

	private ChaseCamera chaseCamera;

	private boolean pickEnable;

	private boolean transparent;

	private boolean explode;

	private List<Spatial> shells = new ArrayList<>();

	private boolean autoRotate;

	private float scale = 1;

	@Override
	protected void initializeLocal() {
		app.getFlyByCamera().setEnabled(false);

		root = new Node(ROOT_NAME);
		LOG.debug("创建元器件状态机的根节点{}", root.getName());
		rootNode.attachChild(root);

		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
		rootNode.addLight(dl);
		dl.setColor(ColorRGBA.White);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
//      fpp.addFilter(new FXAAFilter());
		fpp.addFilter(new ToneMapFilter(Vector3f.UNIT_XYZ.mult(4.0f)));
//      fpp.addFilter(new SSAOFilter(0.5f, 3, 0.2f, 0.2f));
		app.getViewPort().addProcessor(fpp);

		chaseCamera = new ChaseCamera(cam, root, inputManager);

		app.enqueue(() -> {
			final LightProbe probe = LightProbeFactory.makeProbe(stateManager.getState(EnvironmentCamera.class), rootNode);
			((BoundingSphere) probe.getBounds()).setRadius(100);
			rootNode.addLight(probe);
		});

//		添加鼠标监听
		addMapping("pick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		addListener((ActionListener) (name, isPressed, tpf) -> {
			if ("pick".equals(name) && pickEnable && isPressed) {
				@Nullable
				CollisionResult collision = JmeUtil.getCollisionFromScreenPos(root, cam, inputManager);
				if (collision != null) {
					Geometry picked = collision.getGeometry();
					System.err.println(picked);
				}
			}
		}, "pick");

	}

	/**
	 * @param path 模型路径
	 * @param shell 元器件的外壳（由1个或多个节点组成）
	 */
	public void setModelPath(String path, String... shell) {
//		clean up
		shells.clear();
		LOG.debug("移除{}中所有模型", root.getName());
		int childrenSize = root.getChildren().size();
		assert childrenSize <= 1;
		root.detachAllChildren();

		if (path == null) {
			return;
		}
		Node model = null;
		try {
			model = (Node) assetManager.loadModel(path);
		} catch (Exception e) {
			LOG.warn(MessageFormat.format("加载模型失败{0}", path), e);
			throw e;
		}
//		将模型放大100倍
		model.scale(100);

		LOG.debug("加载模型{}", path);
		root.attachChild(model);

		if (shell != null) {
			for (int i = 0; i < shell.length; i++) {
				Spatial shellNode = model.getChild(shell[i]);
				if (shellNode == null) {
					LOG.error("模型{}中没有名为{}的节点", path, shell[i]);
					continue;
				}
				shells.add(shellNode);
			}
		}

		transparentShell();

		explode();
	}

	@Override
	public void cleanup() {
		boolean result = root.removeFromParent();
		if (result) {
			LOG.debug("删除根节点{}", root.getName());
		} else {
			LOG.warn("删除根节点{}失败", root.getName());
		}

		shells.clear();
		chaseCamera.cleanupWithInput(inputManager);
		super.cleanup();
	}

	@Override
	public void update(float tpf) {
		if (autoRotate) {
			root.rotate(0, tpf / 2, 0);
		}
		super.update(tpf);
	}

	public void explode(@NotNull Boolean n) {
		this.explode = n.booleanValue();
		explode();
	}

	private void explode() {
		if (explode) {

		} else {

		}
	}

	public void transparent(@NotNull Boolean n) {
		this.transparent = n.booleanValue();
		app.enqueue(() -> {
			transparentShell();
		});
	}

	private void transparentShell() {
		if (transparent) {
			shells.forEach(shell -> shell.setCullHint(CullHint.Always));
		} else {
			shells.forEach(shell -> shell.setCullHint(CullHint.Dynamic));
		}
	}

	public void setNameVisible(@NotNull Boolean n) {
		this.pickEnable = n.booleanValue();
	}

	public void autoRotate(Boolean n) {
		this.autoRotate = n.booleanValue();
	}

	public void center() {
		// TODO Auto-generated method stub
		System.out.println("ElecCompState.center()");
	}

	public void move() {
		// TODO Auto-generated method stub
		System.out.println("ElecCompState.move()");
	}

	public void rotate() {
		// TODO Auto-generated method stub
		System.out.println("ElecCompState.rotate()");
	}

	public void zoomIn() {
		scale *= 1.1;
		root.setLocalScale(scale);
	}

	public void zoomOut() {
		scale /= 1.1;
		root.setLocalScale(scale);
	}

}
