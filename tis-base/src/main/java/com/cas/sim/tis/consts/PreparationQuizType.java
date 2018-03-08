package com.cas.sim.tis.consts;

public enum PreparationQuizType {
	LIBRARY(1), BROKEN_CASE(2), FREE(3);

	private int type;

	private PreparationQuizType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
