package com.cas.sim.tis.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnswerState {
	// 未答题
	ANSWER_STATE_UNDO(0),
	// 错误未纠正
	ANSWER_STATE_WRONG(1),
	// 错误已纠正
	ANSWER_STATE_CORRECTED(2),
	// 回答正确
	ANSWER_STATE_RIGHT(3);

	private int type;
}
