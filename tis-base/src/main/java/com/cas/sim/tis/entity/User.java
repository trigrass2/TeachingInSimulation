package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4521912822438530684L;
	@Id
	private Integer id;
//	姓名（用于显示）
	protected String name;
//	账号（用户登录）
	protected String code;
	protected String password;
//	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	protected Date createDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	protected Date updateDate;

	@Transient
	protected Integer role;

	protected Integer del = 0;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}
