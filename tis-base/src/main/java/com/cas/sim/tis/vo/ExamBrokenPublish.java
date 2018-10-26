package com.cas.sim.tis.vo;

import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.entity.ExamPublish;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamBrokenPublish extends ExamPublish {
	private BrokenCase brokenCase;
}
