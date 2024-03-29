package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreparationResourceType {
	RESOURCE(1), COGNITION(2), TYPICAL(3);

	private int type;
}
