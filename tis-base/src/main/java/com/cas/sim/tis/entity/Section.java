package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

//章节目录 课程-项目-任务-知识点、 课程-章-节-知识点
public class Section {
	public static final int LVL_0_SUBJECT = 0;
	public static final int LVL_1_PROJECT = 1;
	public static final int LVL_2_TASK = 2;
	public static final int LVL_3_KNOWLEDGE = 3;

	@Id
	private Integer id;

	private String name;

//	4个级别：0-课程、1-章、2-节、3-知识点
	private Integer lvl = 0;

//	上一个级别的ID
	private Integer upperId;
//	创建者ID
	private Integer creatorId;

//	学时
	private Integer period = 0;

	private Integer sort = 0;

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

	public Integer getLvl() {
		return lvl;
	}

	public void setLvl(Integer lvl) {
		this.lvl = lvl;
	}

	public Integer getUpperId() {
		return upperId;
	}

	public void setUpperId(Integer upperId) {
		this.upperId = upperId;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	@Override
	public String toString() {
		return "Section [id=" + id + ", name=" + name + ", lvl=" + lvl + ", upperId=" + upperId + ", creatorId=" + creatorId + ", sort=" + sort + ", createDate=" + createDate + ", updateDate=" + updateDate + ", del=" + del + "]";
	}

}
