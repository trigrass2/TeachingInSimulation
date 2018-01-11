package com.cas.sim.tis.entity;

//填空题
public class QCompletion extends Question {
//	参考答案
	private String reference; // 答案1|答案2

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
