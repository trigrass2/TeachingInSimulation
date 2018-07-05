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
public class PreparationPublish implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3925664054671884772L;
	@Id
	private Integer id;
	@Column(name = "RID")
	private Integer relationId;
	@Column(name = "CID")
	private Integer classId;
	private Integer type;
	private Integer publisher;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date publishDate;
	private PreparationLibrary library;
	private Class clazz;
	private Boolean state = false;
}
