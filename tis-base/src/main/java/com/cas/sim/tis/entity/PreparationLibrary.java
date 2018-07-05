package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @功能 PreparationLibrary.java
 * @作者 Administrator
 * @创建日期 2018年3月5日
 * @修改人 Administrator
 */
@Getter
@Setter
public class PreparationLibrary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1804440953931638199L;
	@Id
	private Integer id;
	// 备课试题组名称
	private String name;
	// 组卷试题编号
	@Column(name = "QIDS")
	private String questionIds;
	// 创建人（管理员/教师编号）
	private Integer creator;
	// 创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Integer updater;
	// 修改时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Boolean del = false;
}
