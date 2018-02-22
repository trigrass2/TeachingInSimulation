package com.cas.sim.tis.entity;

import java.io.Serializable;
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
public class Question implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9159794086496717804L;
	@Id
	private Integer id;
//	问题描述
	private String title;
//	选项（选择题）
	private String options;
//	参考答案（非问答题）
	private String reference;
//	题目解析
	private String analysis;
//	试题库类型：0-模拟题;1-真题;2-教师个人题库
	private Integer type;
//	所属题库
	@Column(name = "RID")
	private Integer relateId;
//	替他信息
//	所属教师
	private Integer creator;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
//	修改时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
//	删除状态标记
	private Boolean del = Boolean.FALSE;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRelateId() {
		return relateId;
	}

	public void setRelateId(Integer relateId) {
		this.relateId = relateId;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Integer getUpdater() {
		return updater;
	}

	public void setUpdater(Integer updater) {
		this.updater = updater;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}
}
