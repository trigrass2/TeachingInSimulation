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
public class GoalRelationship implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -965254270675334654L;

	@Id
	private Integer id;
	@Column(name = "RID")
	private Integer relationId;
	@Column(name = "GID")
	private Integer goalId;
	private Integer type;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Boolean del = false;

}
