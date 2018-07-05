package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreparationAnswer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164279578886061449L;
	@Id
	private Integer id;
	@Column(name = "PPID")
	private Integer preparationPublishId;
	@Column(name = "SID")
	private Integer studentId;
	private String answer;
	private String feedback;
	private Integer score;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date answerDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date feedbackDate;
}
