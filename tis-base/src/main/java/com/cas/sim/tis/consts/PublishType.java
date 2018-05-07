package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum PublishType {
	PRACTICE(0, MsgUtil.getMessage("practice.title.record"), MsgUtil.getMessage("practice.title.detail")), EXAM(1, MsgUtil.getMessage("exam.title.record"), MsgUtil.getMessage("exam.title.detail"));

	private int type;
	private String title;
	private String detailTitle;
}
