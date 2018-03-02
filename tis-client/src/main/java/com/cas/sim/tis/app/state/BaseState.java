package com.cas.sim.tis.app.state;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.app.event.MouseEventListener;
import com.cas.sim.tis.app.event.MouseEventState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public abstract class BaseState extends AbstractAppState {

	protected static final Logger LOG = LoggerFactory.getLogger(BaseState.class);

	protected SimpleApplication app;

	protected InputManager inputManager;

	protected AssetManager assetManager;

	protected AppStateManager stateManager;

	protected Camera cam;

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
			LOG.warn("没有MouseEventState");
			return;
		}
		candidates.add(sp);
		mouseEventState.addCandidate(sp, l);
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {
		LOG.debug("添加状态机{}", getClass());
		super.stateAttached(stateManager);
	}

	@Override
	public final void initialize(AppStateManager stateManager, Application app) {
		this.app = (SimpleApplication) app;
		inputManager = app.getInputManager();
		assetManager = app.getAssetManager();
		cam = app.getCamera();
		this.stateManager = this.app.getStateManager();
		rootNode = this.app.getRootNode();
		settings = app.getContext().getSettings();

		mouseEventState = stateManager.getState(MouseEventState.class);

		initializeLocal();

		super.initialize(stateManager, app);
	}

	/**
	 * 获取到一些常用的内置变量: inputManager、 assetManager、stateManager、rootNode、cam、settings
	 */
	protected abstract void initializeLocal();

	@Override
	public void stateDetached(AppStateManager stateManager) {
		LOG.debug("移除状态机{}", getClass());
		super.stateDetached(stateManager);
	}

	@Override
	public void cleanup() {
		LOG.info("清除{}中添加的事件映射及监听", getClass());
		inputMappings.entrySet().stream().forEach(e -> e.getValue().forEach(t -> inputManager.deleteTrigger(e.getKey(), t)));// 删除
		inputMappings.clear();
		inputListeners.stream().forEach(inputManager::removeListener);
		inputListeners.clear();

		if (mouseEventState == null) {
			LOG.warn("没有MouseEventState");
		} else {
			LOG.info("清除{}中添加的鼠标事件监听", getClass());
			candidates.forEach(c -> mouseEventState.removeCandidate(c));
			candidates.clear();
		}

		super.cleanup();
	}

	protected <T> T loadAsset(AssetKey<T> key) {
		T t = null;
		try {
			t = assetManager.loadAsset(key);
			LOG.info("成功加载资源{}", key);
			return t;
		} catch (Exception e) {
			String errMsg = MessageFormat.format("加载{0}加载失败", key);
			LOG.warn(errMsg, e);
			throw new RuntimeException(MessageFormat.format("无法加载资源{0}", key));
		}
	}

}
