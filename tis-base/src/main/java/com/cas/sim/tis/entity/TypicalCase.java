package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class TypicalCase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8103115364848459642L;

	@Id
	private Integer id;

//	电路名称
	private String name;

//	电路存档的路径
	private String archivePath;

	private String drawings;

	private Integer creator;

	private Date createDate;

	private Integer updater;

	private Date updateDate;

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

	public String getArchivePath() {
		return archivePath;
	}

	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}

	public String getDrawings() {
		return drawings;
	}

	public void setDrawings(String drawings) {
		this.drawings = drawings;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getUpdater() {
		return updater;
	}

	public void setUpdater(Integer updater) {
		this.updater = updater;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Boolean getDel() {
		return del;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}
}
