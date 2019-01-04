package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CaseMode {
	VIEW_MODE(MsgUtil.getMessage("elec.case.mode.view"), true, false), // 查看模式（控制一步步显示元器件，导线）
	TYPICAL_TRAIN_MODE(MsgUtil.getMessage("elec.case.mode.train"), true, true), // 练习模式（教师、学生根据案例自由摆放元器件导线）
	BROKEN_TRAIN_MODE(MsgUtil.getMessage("elec.case.mode.train"), false, false), // 练习模式（教师、学生根据案例检测故障）
	BROKEN_EXAM_MODE(MsgUtil.getMessage("elec.case.mode.exam"), false, false), // 考核模式（学生根据案例检测故障）
	EDIT_MODE(MsgUtil.getMessage("elec.case.mode.edit"), false, true);// 编辑模式（教师编辑案例）

	private String name;
	private boolean hideCircuit;// 是否在初始化时隐藏电路
	private boolean holdEnable;// 模式下是否允许拿去元器件

	@Override
	public String toString() {
		return name;
	}
}
