package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum LibraryType {
	MOCK(0, MsgUtil.getMessage("library.title.mock")), OLD(1, MsgUtil.getMessage("library.title.old")), TEACHERS(2, MsgUtil.getMessage("library.title.teacher"));

	private int type;
	private String key;
}
