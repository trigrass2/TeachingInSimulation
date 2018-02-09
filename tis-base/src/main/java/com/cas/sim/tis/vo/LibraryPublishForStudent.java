package com.cas.sim.tis.vo;

import java.io.Serializable;

public class LibraryPublishForStudent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4236350438959994038L;
	private Integer id;
	private String name;
	private Integer score;
	private Long cost;

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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}
}
