package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4521912822438530684L;
	@Id
	private Integer id;
//	姓名（用于显示）
	private String name;
//	账号（用户登录）
	private String code;
	private String password = "123456";
	@Column(name = "CID")
	private Integer classId;
	@Column(name = "TID")
	private Integer teacherId;
	private Integer role;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date createDate;
	private Integer updater;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Boolean del = false;
	
	@Override
	public String toString() {
		return name;
	}
}
