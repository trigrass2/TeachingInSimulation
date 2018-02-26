package com.cas.sim.tis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

public class LibraryAnswer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -864648825188563471L;
	@Id
	private Integer id;
	/**
	 * 试题序号
	 */
	private Integer index;
	/**
	 * 学生答案
	 */
	private String answer;
	/**
	 * 得分
	 */
	private Float score = 0f;
	/**
	 * 试题编号
	 */
	@Column(name = "QID")
	private Integer questionId;
	/**
	 * 考核/练习记录编号
	 */
	@Column(name = "LRID")
	private Integer recordId;
	/**
	 * 0-未更正(还是错的状态)<br>
	 * 1-已更正
	 */
	private Boolean corrected = false;
	/**
	 * 用户最后一次更正后的答案
	 */
	private String correctAnswer;
	
	private Question question;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Boolean getCorrected() {
		return corrected;
	}

	public void setCorrected(Boolean corrected) {
		this.corrected = corrected;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}
