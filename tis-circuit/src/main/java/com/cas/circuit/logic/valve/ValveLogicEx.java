package com.cas.circuit.logic.valve;

import java.util.List;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.Voltage;
import com.cas.circuit.util.MesureResult;
import com.cas.circuit.util.R;
import com.cas.circuit.vo.ControlIO;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.circuit.vo.Jack;
import com.cas.circuit.vo.Terminal;
import com.cas.gas.util.G;
import com.cas.gas.vo.BlockRelation;
import com.cas.gas.vo.BlockState;
import com.jme3.scene.Node;

public class ValveLogicEx extends BaseElectricCompLogic {
	private Jack ya2_c1;
	private Jack ya2_c2;
	private boolean ya2_c1_effect;
	private boolean ya2_c2_effect;

	private Jack ya4_c1;
	private Jack ya4_c2;

	private boolean ya4_c1_effect;
	private boolean ya4_c2_effect;

	@Override
	public void initialize(Node elecCompMdl) {
		super.initialize(elecCompMdl);
		ya2_c1 = elecComp.getJack("YA2_C1");
		ya2_c2 = elecComp.getJack("YA2_C2");
		ya4_c1 = elecComp.getJack("YA4_C1");
		ya4_c2 = elecComp.getJack("YA4_C2");
	}

	@Override
	protected void onReceivedLocal(Terminal terminal) {
		if (ya2_c1.hasStitch(terminal)) {
			Terminal term1 = ya2_c1.getStitch("1");
			Terminal term2 = ya2_c1.getStitch("2");

			boolean matchVolt = R.matchRequiredVolt(Voltage.IS_DC, term1, term2, 24, 2) != null;

			if (matchVolt) { // 另一头没按下 也没有通电
//			切换状态 A
				if (!ya2_c2_effect && !ya2_c1_effect) {
					changeBlockState("YA2_Break", "YA2_Link");
				}
				ya2_c1_effect = true;
			} else {
				ya2_c1_effect = false;
			}
		} else if (ya2_c2.hasStitch(terminal)) {
			Terminal term1 = ya2_c2.getStitch("1");
			Terminal term2 = ya2_c2.getStitch("2");
			boolean matchVolt = R.matchRequiredVolt(Voltage.IS_DC, term1, term2, 24, 2) != null;

			if (matchVolt) { // 另一头没按下 也没有通电
//				切换状态 B
				if (!ya2_c2_effect && !ya2_c1_effect) {
					changeBlockState("YA2_Link", "YA2_Break");
				}
				ya2_c2_effect = true;
			} else {
				ya2_c2_effect = false;
			}
		}

		if (ya4_c1.hasStitch(terminal)) {
			Terminal term1 = ya4_c1.getStitch("1");
			Terminal term2 = ya4_c1.getStitch("2");
			boolean matchVolt = R.matchRequiredVolt(Voltage.IS_DC, term1, term2, 24, 2) != null;

			if (matchVolt) { // 另一头没按下 也没有通电
//			切换状态 A
				if (!ya4_c2_effect && !ya4_c1_effect) {
					changeBlockState("YA4_Break", "YA4_Link");
				}
				ya4_c1_effect = true;
			} else {
				ya4_c1_effect = false;
			}
		} else if (ya4_c2.hasStitch(terminal)) {
			Terminal term1 = ya4_c2.getStitch("1");
			Terminal term2 = ya4_c2.getStitch("2");
			boolean matchVolt = R.matchRequiredVolt(Voltage.IS_DC, term1, term2, 24, 2) != null;

			if (matchVolt) { // 另一头没按下 也没有通电
//				切换状态 B
				if (!ya4_c2_effect && !ya4_c1_effect) {
					changeBlockState("YA4_Link", "YA4_Break");
				}
				ya4_c2_effect = true;
			} else {
				ya4_c2_effect = false;
			}
		}
		super.onReceivedLocal(terminal);
	}

	@Override
	public void onButtonAction(ControlIO button, boolean press) {
		String paramValue = button.getProperties().get("param");
		if (Util.isEmpty(paramValue)) {
			singleValve(button, press);
		}else{
			doubleValve(button, press, paramValue);
		}
	}

	private void singleValve(ControlIO button, boolean press) {
		// 判断Jack是否满足电压
		String jackName = button.getProperties().get("jackName");
		Jack jack = def.getJackMap().get(jackName);
		MesureResult result = R.matchRequiredVolt(Voltage.IS_DC, jack.getStitch("1"), jack.getStitch("2"), 24, 2);
		// 如果电磁阀上没有满足条件的电压，则允许切换
		if (result != null) {
			return;
		}
		String switchFrom = button.getProperties().get("switchFrom");
		String switchTo = button.getProperties().get("switchTo");
		if (press) {
			changeBlockState(switchFrom, switchTo);
		} else {
			changeBlockState(switchTo, switchFrom);
		}
	}

	private void doubleValve(ControlIO button, boolean press, String paramValue){
		String[] arr = paramValue.split(",");
		try {
			if (!press) {
				getClass().getDeclaredField(arr[1]).set(this, false);
				return;
			}
			boolean ya_cm_effect = getClass().getDeclaredField(arr[0]).getBoolean(this);
			boolean ya_cn_effect = getClass().getDeclaredField(arr[1]).getBoolean(this);

			// 如果当前情况为两头都没有通电，且两头没有按钮按下的情况下
			if (!ya_cm_effect && !ya_cn_effect) {
				// 切换气路
				changeBlockState(button.getProperties().get("switchFrom"), button.getProperties().get("switchTo"));
				getClass().getDeclaredField(arr[1]).set(this, true);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	private void changeBlockState(String oldId, String newId) {
//		FIXME
//		BlockState oldBlock = def.getBlockStatesMap().get(oldId);
//		if (oldBlock != null) {
//			List<BaseVO<?>> oldBlockList = oldBlock.getChildren();
//			for (BaseVO<?> baseVO : oldBlockList) {
//				if (baseVO instanceof BlockRelation) {
//					BlockRelation relation = (BlockRelation) baseVO;
//					def.blockRelationRemoved(relation);
//					G.g().refreshGasPressure();
//				}
//			}
//		}
//		BlockState newBlock = def.getBlockStatesMap().get(newId);
//		if (newBlock != null) {
//			List<BaseVO<?>> newBlockList = newBlock.getChildren();
//			for (BaseVO<?> baseVO : newBlockList) {
//				if (baseVO instanceof BlockRelation) {
//					BlockRelation relation = (BlockRelation) baseVO;
//					def.blockRelationAdded(relation);
//					G.g().refreshGasPressure();
//				}
//			}
//		}
	}
}
