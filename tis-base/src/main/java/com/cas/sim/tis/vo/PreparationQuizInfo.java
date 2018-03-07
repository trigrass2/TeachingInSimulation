package com.cas.sim.tis.vo;

import java.io.Serializable;

import com.cas.sim.tis.entity.Question;

public class PreparationQuizInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5829717022076682639L;

	private Integer id;

	private Question question;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}
