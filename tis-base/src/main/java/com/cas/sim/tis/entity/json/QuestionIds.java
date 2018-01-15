package com.cas.sim.tis.entity.json;

import com.alibaba.fastjson.annotation.JSONField;

public class QuestionIds {
	@JSONField(name = "t")
	private Integer type;

	@JSONField(name = "i")
	private Integer id;

	@JSONField(name = "a")
	private String answer;

	public QuestionIds() {
	}

	public QuestionIds(Integer type, Integer questionId) {
		super();
		this.type = type;
		this.id = questionId;
	}

	public QuestionIds(Integer type, Integer questionId, String answer) {
		this(type, questionId);

		this.answer = answer;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
