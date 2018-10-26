package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

//题库
@Getter
@Setter
public class Library implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -769102637940852815L;
	@Id
	private Integer id;
	private String name;
	/**
	 * 限定考核时间（单位：分钟）
	 */
	private Integer time;
//	试题库类型：0-模拟题;1-真题;2-教师个人题库
	private Integer type;
//	题目数量（脏数据，为了提高查询效率）
	private Integer num;
//	所属教师
	private Integer creator;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Integer del = 0;
	private List<Question> questions;
}
