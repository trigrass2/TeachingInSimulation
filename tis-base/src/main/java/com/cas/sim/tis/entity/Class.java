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
//班级类
public class Class implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3789608723768522104L;
	@Id
	private Integer id;
	private String name;
//	班主任教师编号
	@Column(name = "TID")
	private Integer teacherId;
//	创建人
	private Integer creator;
//	创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
//	默认是0
	private Integer del = 0;
	private User teacher;
}
