package com.cas.circuit.logic.valve;

import com.cas.circuit.BaseElectricCompLogic;
import com.cas.circuit.Voltage;
import com.cas.circuit.util.MesureResult;
import com.cas.circuit.util.R;
import com.cas.circuit.vo.ControlIO;
import com.cas.circuit.vo.Jack;

public class ValveLogic extends BaseElectricCompLogic {

	@Override
	public void onButtonAction(ControlIO button, boolean press) {
		// 判断Jack是否满足电压
//		FIXME jackId
		String jackName = button.getProperties().get("jackName");
		Jack jack = elecComp.getJack(jackName);
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
