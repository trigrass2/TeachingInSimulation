package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

public enum LibraryType {
	MOCK(0, MsgUtil.getMessage("library.title.mock")), OLD(1, MsgUtil.getMessage("library.title.old")), TEACHERS(2, MsgUtil.getMessage("library.title.teacher"));

	private int type;
	private String key;

	private LibraryType(int type, String key) {
		this.type = type;
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public String getKey() {
		return key;
	}
}
