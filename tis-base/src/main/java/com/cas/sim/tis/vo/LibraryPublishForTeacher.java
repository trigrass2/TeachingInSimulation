package com.cas.sim.tis.vo;

import java.io.Serializable;

public class LibraryPublishForTeacher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1284175815009682791L;

	private Integer id;
	private String name;
	private String className;
	private Float average;
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
}
