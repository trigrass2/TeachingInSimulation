package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

//试卷作答结果
public class ExaminationAnswer {
	@Id
	private Integer id;
//	试卷ID
	private Integer examId;
//	我的答案(题目类型，题目ID，我的答案)
	private String answers;
//	试卷ID
	private Float score;
//	创建人id
	private Integer creatorId;
//	开始考试的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date examTime;
//	提交试卷的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date submitTime;

	private Integer del = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getExamTime() {
		return examTime;
	}

	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

}
