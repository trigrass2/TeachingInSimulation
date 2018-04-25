package com.cas.sim.tis.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LibraryPublishForTeacher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1284175815009682791L;

	private Integer id;
	private String name;
	@Column(name = "CNAME")
	private String className;
	private Float average;
	private Date date;
}
