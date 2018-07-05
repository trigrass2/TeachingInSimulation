package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LibraryRecordType {
	LIBRARY(0),PREPARATION(1);
	private int type;
}
