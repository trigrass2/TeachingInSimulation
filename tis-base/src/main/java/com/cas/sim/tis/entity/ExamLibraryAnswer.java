package com.cas.sim.tis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import com.cas.sim.tis.consts.AnswerState;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ExamLibraryAnswer implements Serializable {
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
	private Integer corrected = AnswerState.ANSWER_STATE_UNDO.getType();
	/**
	 * 用户最后一次更正后的答案
	 */
	private String correctAnswer;
	private Question question;
}
