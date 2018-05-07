package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CatalogType {
	SUBJECT(1), PROJECT(2), TASK(3), ATTITUDE(4), SKILL(5), KNOWLEDGE(6);
	private int type;
}
