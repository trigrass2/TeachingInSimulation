package com.cas.sim.tis.vo;

import java.io.Serializable;

public class QuestionStatistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 477823502497338328L;

	private Integer state;
	private Integer num;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
