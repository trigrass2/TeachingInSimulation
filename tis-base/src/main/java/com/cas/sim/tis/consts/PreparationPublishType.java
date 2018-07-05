package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreparationPublishType {
	LIBRARY(1), QUIZ(2);
	private int type;
}
