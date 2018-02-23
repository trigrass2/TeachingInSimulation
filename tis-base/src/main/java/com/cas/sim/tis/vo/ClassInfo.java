package com.cas.sim.tis.vo;

import java.io.Serializable;

public class ClassInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -957976755194326770L;
	private String name;
	private String teacherCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}

}
