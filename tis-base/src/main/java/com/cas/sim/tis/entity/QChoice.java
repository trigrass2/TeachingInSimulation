package com.cas.sim.tis.entity;

import javax.persistence.Column;

//选择题
public class QChoice extends Question {
//	试题选项
	@Column(name="CHOICE_ITEMS")
	private String options;
//	参考答案
	private String reference; // A\B\C\D

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
