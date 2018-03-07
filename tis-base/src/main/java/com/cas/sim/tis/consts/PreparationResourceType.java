package com.cas.sim.tis.consts;

public enum PreparationResourceType {
	RESOURCE(1), COGNITION(2), TYPICAL(3);

	private int type;

	private PreparationResourceType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
