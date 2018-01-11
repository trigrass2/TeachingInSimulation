package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * -Question :没有选择项 、没有标准答案<br>
 * -QChoice :有多个选择项、有标准答案<br>
 * -QJudgment :有两个选择项、有标准答案<br>
 * -QCompletion:没有选择项 、有标准答案<br>
 */
//问答题：只有问题描述与题目解析
public class Question {
	@Id
	protected Integer id;
//	试题库类型：0-模拟题;1-真题;2-教师个人题库
	@Column(name = "Q_TYPE")
	protected Integer type;
//	所属题库
	protected Integer libraryId;
//	问题描述
	protected String title;
//	题目解析
	protected String analysis;
//	替他信息
//	所属教师
	protected Integer creatorId;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	protected Date createDate;
//	删除状态标记
	protected Integer del = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
