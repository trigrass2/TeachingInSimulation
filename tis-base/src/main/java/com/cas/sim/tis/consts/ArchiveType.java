package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArchiveType {
	TYPICAL(1, "typical"), FREE(2, "free");

	private int index;
	private String folder;
}
