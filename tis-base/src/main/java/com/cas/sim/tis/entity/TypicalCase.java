package com.cas.sim.tis.entity;

import java.io.Serializable;

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

	private Integer creatorId;

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

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

}
