package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreparationQuizType {
	LIBRARY(1), BROKEN_CASE(2), FREE(3);

	private int type;
}
