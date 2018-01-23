package com.cas.sim.tis.vo;

import org.springframework.format.annotation.DateTimeFormat;

public class ResourceInfo {
	private String creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String createDate;
	private Integer browsedTimes;
	private Integer collectedTimes;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
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
