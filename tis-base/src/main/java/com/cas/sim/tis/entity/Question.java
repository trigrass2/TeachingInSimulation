package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * -Question :没有选择项 、没有标准答案<br>
 * -QChoice :有多个选择项、有标准答案<br>
 * -QJudgment :有两个选择项、有标准答案<br>
 * -QCompletion:没有选择项 、有标准答案<br>
 */
//问答题：只有问题描述与题目解析
@Getter
@Setter
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
//	题目得分
	private Float point = 0f;
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
}
