package com.cas.sim.tis.entity;

import javax.persistence.Column;

import org.apache.ibatis.type.Alias;

//选择题
@Alias("q_choice")
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
