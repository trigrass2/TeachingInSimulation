package com.cas.sim.tis.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class LibraryPublishForTeacher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1284175815009682791L;

	private Integer id;
	private String name;
	@Column(name = "CNAME")
	private String className;
	private Float average;
	private Date date;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Float getAverage() {
		return average;
	}

	public void setAverage(Float average) {
		this.average = average;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
