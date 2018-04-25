package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//章节目录 课程-项目-任务-知识点、 课程-章-节-知识点
public class Catalog implements Serializable {
//	public static final int LVL_0_SUBJECT = 0;
//	public static final int LVL_1_PROJECT = 1;
//	public static final int LVL_2_TASK = 2;
//	public static final int LVL_3_KNOWLEDGE = 3;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3396959600260668844L;
	@Id
	private Integer id;
	private String name;
//	4个级别：0-课程、1-章、2-节、3-知识点
//	private Integer type = 0;
//	学时
	private Integer lessons = 0;
//	上一目录的ID
	private Integer rid;
//	排序
	private Float index;
//	创建者ID
	private Integer creatorId;
	private Date createDate;
	private Date updateDate;
	private Integer del = 0;
}
