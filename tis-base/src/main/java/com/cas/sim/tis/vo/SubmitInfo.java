package com.cas.sim.tis.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * 学生考核提交状态
 * @功能 SubmitInfo.java
 * @作者 Caowj
 * @创建日期 2018年2月26日
 * @修改人 Caowj
 */
@Getter
@Setter
public class SubmitInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3261023400540920916L;
	private String code;
	private String name;
	private Boolean submited;
	private Integer score;
}
