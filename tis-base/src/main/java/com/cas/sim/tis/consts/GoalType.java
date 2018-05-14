package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalType {
	ATTITUDE(1), SKILL(2), KNOWLEDGE(3);
	private int type;
}
