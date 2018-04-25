package com.cas.sim.tis.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResourceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5430155705714314530L;
	private String creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private Integer browsedTimes;
	private Integer collectedTimes;
}
