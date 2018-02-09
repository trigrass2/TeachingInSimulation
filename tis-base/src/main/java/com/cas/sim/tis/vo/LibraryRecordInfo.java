package com.cas.sim.tis.vo;

import java.io.Serializable;

public class LibraryRecordInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8958520748299705590L;

	private Integer id;
	/**
	 * 学生姓名
	 */
	private String name;
	/**
	 * 学生成绩
	 */
	private Integer score;
	/**
	 * 考核用时
	 */
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
