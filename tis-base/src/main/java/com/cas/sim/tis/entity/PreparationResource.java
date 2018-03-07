package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @功能 Preparation.java
 * @作者 Administrator
 * @创建日期 2018年3月5日
 * @修改人 Administrator
 */
public class PreparationResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1804440953931638199L;

	@Id
	private Integer id;
	private Integer type;
	@Column(name = "PID")
	private Integer preparationId;
	@Column(name = "RID")
	private Integer relationId;
//	创建人（管理员/教师编号）
	private Integer creator;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
//	修改时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPreparationId() {
		return preparationId;
	}

	public void setPreparationId(Integer preparationId) {
		this.preparationId = preparationId;
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
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
}
