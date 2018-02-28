package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

public enum PublishType {
	PRACTICE(0, MsgUtil.getMessage("practice.title.record"), MsgUtil.getMessage("practice.title.detail")), EXAM(1, MsgUtil.getMessage("exam.title.record"), MsgUtil.getMessage("exam.title.detail"));

	private int type;
	private String title;
	private String detailTitle;

	private PublishType(int type, String title, String detialTitle) {
		this.type = type;
		this.title = title;
		this.detailTitle = detialTitle;
	}

	public int getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDetailTitle() {
		return detailTitle;
	}
}
