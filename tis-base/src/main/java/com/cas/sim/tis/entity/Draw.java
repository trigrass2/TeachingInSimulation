package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

//图纸
public class Draw {
	@Id
	private Integer id;

	private String name;

	private String paths;

	private Integer creatorId;
	
	private Date createDate;
	
	private Boolean del = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}
}
