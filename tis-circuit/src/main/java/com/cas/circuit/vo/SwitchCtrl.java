package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cas.circuit.util.R;

public abstract class SwitchCtrl {
//	protected List<String> getResisStateIds() = new ArrayList<String>();
	protected int switchIndex = 0;

	protected String ioType;
	// 禁止切换电路、气路
	private boolean disable;

	public SwitchCtrl() {
	}

	public void switchStart(Integer index) {
		if (disable || switchIndex == index || getResisStateIds().size() == 0) {
			return;
		}
//		Terminal terminal = null;

		// 关灯
		ResisState oldState = getElecComp().getResisStatesMap().get(getResisStateIds().get(switchIndex));
//		if (oldState.getLightIO() != null) {
//			oldState.getLightIO().closeLight();
//		}
		Set<String> envs = new HashSet<String>();
		if (oldState != null) {
			// 把原来的state控制的relation删除
			List<ResisRelation> oldRelationList = oldState.getResisRelationList();
			oldRelationList.forEach(r -> {
				getElecComp().resisRelationRemoved(r);
//				找出两个连接头上所有的电源环境,用来通知对应的电源电路发生了变化,重新计算电路
				envs.addAll(R.findEnvsOn(r.getTerm1(), r.getTerm2()));
			});
		}

		if (index == null) {
			changeStateIndex(index);
		} else {
			switchIndex = index;
		}
		for (String env : envs) {
			R r = R.getR(env);
			r.shareVoltage();
		}
	}

	/**
	 * 按钮将其控制的state切换
	 * @param index index不为null则强制切换到某档
	 */
	public void doSwitch(Integer index) {
		if (disable) {
			return;
		}
		if (switchIndex == index) {
			// System.err.println("但是 已经处于这种状态了,不跳变");
			return;
		}
		// System.err.println(this + "跳变 " + switchIndex + " -> " + index);

		Set<String> envs = new HashSet<String>();

		if (getResisStateIds().size() > 0) {
			// System.err.println(elecComp.getResisStatesMap() + " :: " + getResisStateIds() +" , " + getResisStateIds().get(switchIndex));
			ResisState oldState = getElecComp().getResisStatesMap().get(getResisStateIds().get(switchIndex));
			if (oldState != null) {
				// 把原来的state控制的relation删除
				List<ResisRelation> oldRelationList = oldState.getResisRelationList();
				oldRelationList.forEach(r -> {
					getElecComp().resisRelationRemoved(r);
					// 找出两个连接头上所有的电源环境,用来通知对应的电源电路发生了变化,重新计算电路
					envs.addAll(r.getTerm1().getResidualVolt().keySet());
					envs.addAll(r.getTerm2().getResidualVolt().keySet());
				});
			}
		}
		if (index == null) {
			changeStateIndex(index);
		} else {
			switchIndex = index;
		}
		if (getResisStateIds().size() > 0) {
			// 开灯
			ResisState newState = getElecComp().getResisStatesMap().get(getResisStateIds().get(switchIndex));
			if (newState != null) {
				// 把当前的state控制的relation增加
				List<ResisRelation> newRelationList = newState.getResisRelationList();

				newRelationList.forEach(r -> {
					getElecComp().resisRelationAdded(r);
					// 找出两个连接头上所有的电源环境,用来通知对应的电源电路发生了变化,重新计算电路
					envs.addAll(r.getTerm1().getResidualVolt().keySet());
					envs.addAll(r.getTerm2().getResidualVolt().keySet());
					// 如果这两个连接头上都没有找到电源环境,则有可能是两端都有电阻,所以电没有到达这两个连接头,但是闭合这两个连接头后会通路。
					R tmpR = new R("Tmp_REMOVE", r.getTerm1(), r.getTerm2());
					R.findAllIsopotential(tmpR.getStartTerminal(), tmpR, true);
					R.findAllIsopotential(tmpR.getEndTerminal(), tmpR, true);
					tmpR.getAllIsopoList().forEach(ip -> {
						ip.getTerminals().forEach(t -> envs.addAll(t.getIsopotential().keySet()));
					});
					envs.remove("Tmp_REMOVE");
					tmpR.shutPowerDown();
				});
			}
		}
		// System.out.println("SwitchCtrl.doSwitch() -----------shareVoltage-----------");
		for (String env : envs) {
			R r = R.getR(env);
			r.shareVoltage();
		}
	}

	public void switchEnd() {
		if (disable) {
			return;
		}
		if (getResisStateIds().size() == 0) {
			return;
		}
		// 开灯
		ResisState newState = getElecComp().getResisStatesMap().get(getResisStateIds().get(switchIndex));
		Set<String> envs = new HashSet<String>();
		if (newState != null) {
			// 把当前的state控制的relation增加
			List<ResisRelation> newRelationList = newState.getResisRelationList();

			newRelationList.forEach(r -> {
				getElecComp().resisRelationAdded(r);
//				找出两个连接头上所有的电源环境,用来通知对应的电源电路发生了变化,重新计算电路
				HashSet<String> tmpEnvs = new HashSet<String>();
				tmpEnvs.addAll(r.getTerm1().getResidualVolt().keySet());
				tmpEnvs.addAll(r.getTerm2().getResidualVolt().keySet());

//				如果这两个连接头上都没有找到电源环境,则有可能是两端都有电阻,所以电没有到达这两个连接头,但是闭合这两个连接头后会通路。
				R tmpR = new R("Tmp_REMOVE", r.getTerm1(), r.getTerm2());

				R.findAllIsopotential(tmpR.getStartTerminal(), tmpR, true);
				R.findAllIsopotential(tmpR.getEndTerminal(), tmpR, true);
				List<IP> allIP = tmpR.getAllIsopoList();
				for (IP ip : allIP) {
					ip.getTerminals().forEach(t -> tmpEnvs.addAll(t.getIsopotential().keySet()));
				}
				tmpEnvs.remove("Tmp_REMOVE");
				tmpR.shutPowerDown();
				envs.addAll(tmpEnvs);
			});
		}

		for (String env : envs) {
			R r = R.getR(env);
			r.shareVoltage();
		}
	}

	public List<String> getResisStateIds() {
		return new ArrayList<>();
	}

	public String getIoType() {
		return ioType;
	}

	public void switchStateChanged(Integer wheel) {
	}

	protected abstract void changeStateIndex(Integer index);

	protected abstract ElecCompDef getElecComp();

	public int getSwitchIndex() {
		return switchIndex;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

}
