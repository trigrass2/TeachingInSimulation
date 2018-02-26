package com.cas.sim.tis.vo;

import java.io.Serializable;
/**
 * 学生考核提交状态
 * @功能 SubmitInfo.java
 * @作者 Caowj
 * @创建日期 2018年2月26日
 * @修改人 Caowj
 */
public class SubmitInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3261023400540920916L;
	private String code;
	private String name;
	private Boolean submited;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getSubmited() {
		return submited;
	}
	public void setSubmited(Boolean submited) {
		this.submited = submited;
	}
}
