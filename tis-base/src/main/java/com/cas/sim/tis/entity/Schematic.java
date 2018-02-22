package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

//一份图纸,电路原理图
public class Schematic {
	@Id
	private Integer id;

//	电路图所在路径
	private String path;

//	一份图纸的图纸数量
	private Integer sum;

//	图纸创建人
	private Integer creatorId;
//	图纸创建人角色（管理员、老师、学生）
	private Integer creatorRole;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;

	private Boolean del = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getCreatorRole() {
		return creatorRole;
	}

	public void setCreatorRole(Integer creatorRole) {
		this.creatorRole = creatorRole;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}

}
