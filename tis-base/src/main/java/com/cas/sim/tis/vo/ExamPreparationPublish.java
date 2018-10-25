package com.cas.sim.tis.vo;

import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.entity.PreparationLibrary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamPreparationPublish extends ExamPublish {
	private PreparationLibrary library;
}
