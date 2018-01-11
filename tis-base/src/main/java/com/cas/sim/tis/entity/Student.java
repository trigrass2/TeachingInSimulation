package com.cas.sim.tis.entity;

import javax.persistence.Column;

//学生
public class Student extends User {
//	所属班级ID
	@Column(name="CID")
	private Integer clsId;

	public Integer getClsId() {
		return clsId;
	}

	public void setClsId(Integer clsId) {
		this.clsId = clsId;
	}

}
