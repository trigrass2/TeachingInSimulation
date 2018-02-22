package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

//班级类
public class Class {
	@Id
	private Integer id;
	private String name;
//	班主任教师编号
	@Column(name = "TID")
	private Integer teacherId;
//	创建人
	private Integer creator;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
//	默认是0
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

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
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

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

}
