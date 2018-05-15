package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalRelationshipType {
	TASK(1), RESOURCE(2), QUIZ(3), OBJECTIVE(4);
	private int type;
}
