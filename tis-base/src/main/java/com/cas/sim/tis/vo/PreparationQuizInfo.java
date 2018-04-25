package com.cas.sim.tis.vo;

import java.io.Serializable;

import com.cas.sim.tis.entity.Question;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PreparationQuizInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5829717022076682639L;
	private Integer id;
	private Question question;
}
