package com.cas.sim.tis.app.hold;

import java.util.List;
import java.util.stream.Collectors;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.component.ElecCompProxy;
import com.cas.circuit.component.RelyOn;
import com.cas.circuit.util.JmeUtil;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.app.event.MouseEventState;
import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElecCompHoldHandler implements HoldHandler {
	private ElecCaseState<?> caseState;

	private Spatial spatial;
	private ElecComp elecComp;
	private ElecCompDef elecCompDef;

	public ElecCompHoldHandler(ElecCaseState<?> caseState, ElecComp elecComp) {
		this.caseState = caseState;
		this.elecComp = elecComp;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void initialize() {
//		设置Holding的模型对鼠标不可见
		MouseEventState.setMouseVisible(spatial, false);

//		加载元器件配置文件，准备初始化元器件逻辑对象
		elecCompDef = SpringUtil.getBean(ElecCompAction.class).parse(elecComp.getCfgPath());
		elecCompDef.setProxy(new ElecCompProxy());
		elecCompDef.getProxy().setId(elecComp.getId());
	}

	@Override
	public void rotate(boolean up) {
		if (up) {
			spatial.rotate(0, FastMath.DEG_TO_RAD * 90, 0);
		} else {
			spatial.rotate(0, -FastMath.DEG_TO_RAD * 90, 0);
		}
	}

	@Override
	public void move() {
//		判断是否需要底座
		Vector3f contactPoint = null;
		if (ElecComp.COMBINE_RELY_ON == elecComp.getCombine()) {
			RelyOn relay = elecCompDef.getRelyOn();
			if (relay != null) {
				List<ElecCompDef> list = caseState.getCircuitState().getCompList().stream().filter(e -> {
					return e.getBase() != null // 是底座
							&& e.getBase().checkMatched(elecCompDef) // 两个元器件可搭配使用
							&& !e.getBase().isUsed(elecCompDef);// 底座没有被占用
				}).collect(Collectors.toList());

				for (ElecCompDef e : list) {
					contactPoint = JmeUtil.getContactPointFromCursor(//
							e.getSpatial(), //
							caseState.getCam(), //
							caseState.getInputManager().getCursorPosition());
					if (contactPoint != null) {
						Node baseMdl = e.getSpatial();
						contactPoint = baseMdl.getLocalTranslation().add(relay.getTranslation());
					}
				}
			}
		}

		if (contactPoint == null) {
			contactPoint = JmeUtil.getContactPointFromCursor(//
					caseState.getCompPlane(), //
					caseState.getCam(), //
					caseState.getInputManager().getCursorPosition());
		}
		if (contactPoint == null) {
			return;
		}
		/*
		 * 在x-z平面上，每次移动一个单位
		 */
		spatial.setLocalTranslation(//
				(int) contactPoint.x, //
				contactPoint.y, //
				(int) contactPoint.z//
		);
	}

	@Override
	public boolean putDown() {
		if (ElecComp.COMBINE_RELY_ON == elecComp.getCombine()) {
			return false;
		}
//		设置Holding的模型对鼠标可见
		MouseEventState.setMouseVisible(spatial, true);

//		将元器件模型与元器件对象一起加入电路板中
		caseState.getCircuitState().attachToCircuit(spatial, elecCompDef);

		return true;
	}

	@Override
	public void discard() {
//		nothing to do
//		最终将模型从手中丢掉
		spatial.removeFromParent();
	}

	@Override
	public boolean putDownOn(Spatial base) {
		ElecCompDef e = base.getUserData("entity");
		boolean suitable = e.getBase() != null // 是底座
				&& e.getBase().checkMatched(elecCompDef) // 两个元器件可搭配使用
				&& !e.getBase().isUsed(elecCompDef);// 底座没有被占用
		if (suitable) {
//			设置Holding的模型对鼠标可见
			MouseEventState.setMouseVisible(spatial, true);
//			将元器件模型与元器件对象一起加入电路板中
			caseState.getCircuitState().attachToBase(elecComp.getSpatial(), elecCompDef, e);
		}
		return suitable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ElecComp getData() {
		return elecComp;
	}
	
}
