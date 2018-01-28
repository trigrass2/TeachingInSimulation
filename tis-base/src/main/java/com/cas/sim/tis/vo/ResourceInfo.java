package com.cas.sim.tis.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getBrowsedTimes() {
		return browsedTimes;
	}

	public void setBrowsedTimes(Integer browsedTimes) {
		this.browsedTimes = browsedTimes;
	}

	public Integer getCollectedTimes() {
		return collectedTimes;
	}

	public void setCollectedTimes(Integer collectedTimes) {
		this.collectedTimes = collectedTimes;
	}
}
