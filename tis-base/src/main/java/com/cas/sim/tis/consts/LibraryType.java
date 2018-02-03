package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

public enum LibraryType {
	MOCK(0, MsgUtil.getMessage("library.menu.mock")), OLD(1, MsgUtil.getMessage("library.menu.old")), TEACHERS(2, "");

	private int type;
	private String key;

	private LibraryType(int type, String key) {
		this.type = type;
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
