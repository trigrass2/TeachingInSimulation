package com.cas.sim.tis.entity;

import java.util.Date;

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
	private Integer id;
//	试题库类型：0-模拟题;1-真题;2-教师个人题库
	private Integer type;
//	所属题库
	private Integer libraryId;
//	问题描述
	private String title;
//	选项（选择题）
	private String options;
//	参考答案（非问答题）
	private String refrence;
//	题目解析
	private String analysis;
//	替他信息
//	所属教师
	private Integer creatorId;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
//	删除状态标记
	private Boolean del = Boolean.FALSE;

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

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getRefrence() {
		return refrence;
	}

	public void setRefrence(String refrence) {
		this.refrence = refrence;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
