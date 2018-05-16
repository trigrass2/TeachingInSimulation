package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CatalogType {
	SUBJECT(1), PROJECT(2), TASK(3);
	private int type;
}