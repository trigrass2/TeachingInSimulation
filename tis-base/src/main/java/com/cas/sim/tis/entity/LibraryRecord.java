package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 试题发布记录表
 * @功能 LibraryPublish.java
 * @作者 Caowj
 * @创建日期 2018年2月7日
 * @修改人 Caowj
 */
public class LibraryRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -90069710975023747L;
	@Id
	private Integer id;
	@Column(name = "PID")
	private Integer publishId;
	private Integer score;
	private Long cost;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPublishId() {
		return publishId;
	}

	public void setPublishId(Integer publishId) {
		this.publishId = publishId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
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
}
