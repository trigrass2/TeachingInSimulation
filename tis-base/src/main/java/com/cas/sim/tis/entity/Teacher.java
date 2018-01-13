package com.cas.sim.tis.entity;

import com.cas.sim.tis.consts.RoleConst;

public class Teacher extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7486945271362330401L;

	public Teacher() {
		role = RoleConst.TEACHER;
	}

	@Override
	public String toString() {
		return "Teacher [name=" + name + ", code=" + code + ", password=" + password + ", createDate=" + createDate + ", updateDate=" + updateDate + ", del=" + del + "]";
	}

}
