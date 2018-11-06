//package com.cas.sim.tis.app.state.typical;
//
//import com.cas.circuit.component.ElecCompDef;
//import com.cas.circuit.component.ElecCompProxy;
//import com.cas.circuit.component.RelyOn;
//import com.cas.circuit.util.JmeUtil;
//import com.cas.circuit.util.Util;
//import com.cas.sim.tis.action.ElecCompAction;
//import com.cas.sim.tis.anno.JmeThread;
//import com.cas.sim.tis.app.event.MouseEventState;
//import com.cas.sim.tis.app.listener.TypicalCaseListener;
//import com.cas.sim.tis.app.state.BaseState;
//import com.cas.sim.tis.app.state.SceneCameraState;
//import com.cas.sim.tis.entity.ElecComp;
//import com.cas.sim.tis.util.AlertUtil;
//import com.cas.sim.tis.util.MsgUtil;
//import com.cas.sim.tis.util.SpringUtil;
//import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
//import com.jme3.asset.ModelKey;
//import com.jme3.collision.CollisionResults;
//import com.jme3.math.FastMath;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3x.jfx.util.JFXPlatform;
//
//import javafx.scene.control.Alert.AlertType;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class HoldState extends BaseState {
//
////	当前手中的元器件对象
//	ElecComp elecComp;
//	ElecCompDef elecCompDef;
//
////	手持节点模型
//	private Node root;
////	电路板模型
//	private Spatial compPlane;
//	private SceneCameraState cameraState;
//	private TypicalCaseListener listener;
//
//	public HoldState(Node root, Spatial compPlane, SceneCameraState cameraState) {
//		this.root = root;
//		this.compPlane = compPlane;
//		this.cameraState = cameraState;
//	}
//
//	@Override
//	protected void initializeLocal() {
//		listener = new TypicalCaseListener(this);
//		listener.registerWithInput(inputManager);
//	}
//
//	@Override
//	public void cleanup() {
////		删除监听事件
//		listener.unregisterInput();
//		super.cleanup();
//	}
//
//	/**
//	 * 拿着一个元器件，准备放置在电路板上
//	 */
//	@JmeThread
//	public void hold(ElecComp elecComp) {
////		检查当前手中时候已经有一个元器件了
//		if (this.elecComp != null) {
////			如果有，就将模型删除掉，准备换成新元器件模型
//			this.elecComp.getSpatial().removeFromParent();
//		}
////		禁用相机的滚轮事件，留给旋转模型用。
//		cameraState.setZoomEnable(false);
////		加载相应的模型
//		try {
//			Spatial spatial = loadAsset(new ModelKey(elecComp.getMdlPath()));
////			设置Holding的模型对鼠标透明
//			MouseEventState.setMouseVisible(spatial, false);
////			spatial.scale(25);
//			root.attachChild(spatial);
//
////			设置当前元器件的模型对象
//			elecComp.setSpatial(spatial);
////			记录当前的元器件对象
//			this.elecComp = elecComp;
//
//			this.elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
//			this.elecCompDef.setProxy(new ElecCompProxy());
//			this.elecCompDef.getProxy().setId(elecComp.getId());
//		} catch (Exception e) {
//			cameraState.setZoomEnable(true);
//		}
//	}
//
//	/**
//	 * 放置手中的元器件
//	 */
//	@JmeThread
//	public void putDown(CircuitState circuitState) {
//		if (elecComp == null || circuitState == null) {
//			return;
//		}
//
//		CollisionResults results = new CollisionResults();
//		circuitState.getRootCompNode().collideWith(elecComp.getSpatial().getWorldBound(), results);
//		if (results.size() > 0) {
//			JFXPlatform.runInFXThread(() -> AlertUtil.showTip(TipType.WARN, MsgUtil.getMessage("alert.warning.collision")));
//			return;
//		}
//
//		try {
//			if (ElecComp.COMBINE_RELY_ON == elecComp.getCombine()) {
////				需要底座的元器件不可直接加入电路板
//				JFXPlatform.runInFXThread(() -> AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.base.need")));
//				elecComp.getSpatial().removeFromParent();
//			} else {
////				将元器件模型与元器件对象一起加入电路板中
//				circuitState.attachToCircuit(elecComp.getSpatial(), elecCompDef);
//			}
////			取消Holding的模型对鼠标透明
//			MouseEventState.setMouseVisible(elecComp.getSpatial(), true);
//		} catch (Exception e) {
////			删除出错的模型
//			elecComp.getSpatial().removeFromParent();
//			log.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
//			e.printStackTrace();
//		} finally {
//			elecComp = null;
//			cameraState.setZoomEnable(true);
//		}
//	}
//
//	/**
//	 * 丢弃手中的元器件
//	 */
//	public void discard() {
//		if (elecComp == null) {
//			return;
//		}
//		elecComp.getSpatial().removeFromParent();
//		elecComp = null;
//		cameraState.setZoomEnable(true);
//	}
//
//	/**
//	 * 将元器件放置在指定底座上
//	 * @param baseDef 指定底座
//	 */
//	public void putDownOnBase(ElecCompDef def, CircuitState circuitState) {
//		if (elecComp == null || ElecComp.COMBINE_RELY_ON != elecComp.getCombine()) {
//			return;
//		}
//		if (def.getBase().isUsed(elecCompDef)) {
//			return;
//		}
//		try {
////			取消Holding的模型对鼠标透明
//			MouseEventState.setMouseVisible(elecComp.getSpatial(), true);
////			1、判断元器件与底座是否匹配
//			if (def.getBase().checkMatched(elecCompDef)) {
////				2、将元器件模型与元器件对象一起加入电路板中
//				circuitState.attachToBase(elecComp.getSpatial(), elecCompDef, def);
//			} else {
//				JFXPlatform.runInFXThread(() -> AlertUtil.showAlert(AlertType.WARNING, MsgUtil.getMessage("alert.warning.base.unmatch")));
//				elecComp.getSpatial().removeFromParent();
//			}
//			elecComp = null;
//			cameraState.setZoomEnable(true);
//		} catch (Exception e) {
////			删除出错的模型
//			if (elecComp != null) {
//				elecComp.getSpatial().removeFromParent();
//				log.error("初始化元器件{}时出现了一个错误:{}", elecComp.getModel(), e.getMessage());
//			}
//			e.printStackTrace();
//		}
//	}
//
//	public void relayOnBase(ElecCompDef base) {
//		if (elecComp == null) {
//			return;
//		}
//		if (base.getBase().checkMatched(elecCompDef)) {
//			RelyOn relyOn = elecCompDef.getRelyOn();
//			Node baseMdl = base.getSpatial();
//			Vector3f loc = baseMdl.getLocalTranslation().add(relyOn.getTranslation());
//			elecComp.getSpatial().setLocalTranslation(loc);
//		}
//	}
//
//	public void mouseMoved() {
//		// moved_before_putdown = true;
//		if (elecComp == null) {
//			return;
//		}
//
//		Vector3f contactPoint = JmeUtil.getContactPointFromCursor(compPlane, cam, inputManager.getCursorPosition());
//		if (contactPoint == null) {
//			return;
//		}
//		elecComp.getSpatial().setLocalTranslation(new Vector3f(//
//				Util.round(contactPoint.x, 3, 5), //
//				contactPoint.y, //
//				Util.round(contactPoint.z, 3, 5)//
//		));
//	}
//
//	public void mouseWheel(boolean positive) {
//		if (elecComp == null) {
//			return;
//		}
//		if (positive) {
//			elecComp.getSpatial().rotate(0, FastMath.DEG_TO_RAD * 90, 0);
//		} else {
//			elecComp.getSpatial().rotate(0, -FastMath.DEG_TO_RAD * 90, 0);
//		}
//	}
//
//	public ElecComp getHold() {
//		return elecComp;
//	}
//}
