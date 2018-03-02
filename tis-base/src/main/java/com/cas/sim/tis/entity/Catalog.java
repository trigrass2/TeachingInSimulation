package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Integer getType() {
//		return type;
//	}
//
//	public void setType(Integer type) {
//		this.type = type;
//	}

	public Integer getLessons() {
		return lessons;
	}

	public void setLessons(Integer lessons) {
		this.lessons = lessons;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Float getIndex() {
		return index;
	}

	public void setIndex(Float index) {
		this.index = index;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

}
