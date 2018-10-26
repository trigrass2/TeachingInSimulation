package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 试题发布记录表
 * @功能 LibraryPublish.java
 * @作者 Caowj
 * @创建日期 2018年2月7日
 * @修改人 Caowj
 */
@Getter
@Setter
public class ExamLibraryRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -90069710975023747L;
	@Id
	private Integer id;
	@Column(name = "PID")
	private Integer publishId;
	private Float score;
	private Integer cost;
	private Integer type;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
}
