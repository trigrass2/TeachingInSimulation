package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

//一份图纸,电路原理图
@Getter
@Setter
public class Schematic {
	@Id
	private Integer id;
//	电路图所在路径
	private String path;
//	一份图纸的图纸数量
	private Integer sum;
//	图纸创建人
	private Integer creatorId;
//	图纸创建人角色（管理员、老师、学生）
	private Integer creatorRole;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Boolean del = false;
}
