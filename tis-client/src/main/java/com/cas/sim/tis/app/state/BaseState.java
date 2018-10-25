package com.cas.sim.tis.app.state;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.app.event.MouseEventListener;
import com.cas.sim.tis.app.event.MouseEventState;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseState extends AbstractAppState {

	protected JmeApplication app;

	protected @Getter InputManager inputManager;

	protected AssetManager assetManager;

	protected AppStateManager stateManager;
	
	protected RenderManager renderManager;

	protected @Getter Camera cam;

	protected Node rootNode;

	protected AppSettings settings;

//	记录state中用到的映射名和事件，退出清除
	private Map<String, List<Trigger>> inputMappings = new HashMap<>();
	private List<InputListener> inputListeners = new ArrayList<>();

	private List<Spatial> candidates = new ArrayList<>();

	protected MouseEventState mouseEventState;

	/**
	 * @param string
	 * @param mouseButtonTrigger
	 */
	protected void addMapping(String mappingName, Trigger... triggers) {
		if (triggers == null) {
			throw new RuntimeException("必须给映射名称配上触发器");
		}
		inputManager.addMapping(mappingName, triggers);
		List<Trigger> triggerList = inputMappings.get(mappingName);
		if (triggerList == null) {
			triggerList = new ArrayList<>();
			inputMappings.put(mappingName, triggerList);
		}
		triggerList.addAll(Arrays.asList(triggers));
	}

	protected void addListener(InputListener listener, String... mappingNames) {
		if (mappingNames == null) {
			throw new RuntimeException("必须给监听对象配上映射名称");
		}
		inputManager.addListener(listener, mappingNames);
		inputListeners.add(listener);
	}

	protected void addListener(Spatial sp, MouseEventListener l) {
		if (mouseEventState == null) {
			log.warn("没有MouseEventState");
			return;
		}
		mouseEventState.addCandidate(sp, l);
		candidates.add(sp);
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
		log.debug("添加状态机{}", getClass());
		super.stateAttached(stateManager);
	}

	@Override
	public final void initialize(AppStateManager stateManager, Application app) {
		this.app = (JmeApplication) app;
		this.inputManager = app.getInputManager();
		this.assetManager = app.getAssetManager();
		this.cam = app.getCamera();
		this.stateManager = stateManager;
		this.renderManager = app.getRenderManager();
		this.rootNode = this.app.getRootNode();
		this.settings = app.getContext().getSettings();

		this.mouseEventState = stateManager.getState(MouseEventState.class);

		initializeLocal();

		super.initialize(stateManager, app);
	}

	/**
	 * 获取到一些常用的内置变量: inputManager、 assetManager、stateManager、rootNode、cam、settings
	 */
	protected abstract void initializeLocal();

	@Override
	public void stateDetached(AppStateManager stateManager) {
		log.debug("移除状态机{}", getClass());
		super.stateDetached(stateManager);
	}

	@Override
	public void cleanup() {
		cleanInputs();

		cleanEvents();

		super.cleanup();
	}

	protected void cleanInputs() {
		log.info("清除{}中添加的事件映射及监听", getClass());
		inputMappings.entrySet().stream().forEach(e -> e.getValue().forEach(t -> inputManager.deleteTrigger(e.getKey(), t)));// 删除
		inputMappings.clear();
		inputListeners.stream().forEach(inputManager::removeListener);
		inputListeners.clear();
	}

	protected void cleanEvents() {
		if (mouseEventState == null) {
			log.warn("没有MouseEventState");
		} else {
			log.info("清除{}中添加的鼠标事件监听", getClass());
			candidates.forEach(c -> mouseEventState.removeCandidate(c));
			candidates.clear();
		}
	}

	protected <T> T loadAsset(AssetKey<T> key) {
		T t = null;
		try {
			t = assetManager.loadAsset(key);
			log.info("加载资源{}", key);
			return t;
		} catch (Exception e) {
			String errMsg = MessageFormat.format("加载{0}加载失败", key);
			log.warn(errMsg, e);
			throw new RuntimeException(MessageFormat.format("无法加载资源{0}", key));
		}
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public Node getRootNode() {
		return rootNode;
	}

}
