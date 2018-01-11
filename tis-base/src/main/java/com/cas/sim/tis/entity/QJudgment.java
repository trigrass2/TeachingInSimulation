package com.cas.sim.tis.entity;

//判断题
public class QJudgment extends Question {
//	正确答案
	private String reference; // 对|错

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
