package com.cas.sim.tis.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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
}
