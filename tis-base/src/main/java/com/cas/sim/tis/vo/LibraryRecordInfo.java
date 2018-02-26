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
	private Float score;
	/**
	 * 考核用时
	 */
	private Integer cost;

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

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
}
