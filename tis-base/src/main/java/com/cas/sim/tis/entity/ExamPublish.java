package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamPublish {

	@AllArgsConstructor
	@Getter
	public enum Type {
		LIBRARY_PRACTICE(0), LIBRARY_EXAM(1), PREPARATION_EXAM(2), BROKEN_EXAM(3);

		private int type;
	}

	@Id
	private Integer id;
	@Column(name = "RID")
	private Integer relationId;
	@Column(name = "CID")
	private Integer classId;
	private Integer type;
	private Float average;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Boolean state = false;
	
	private Class clazz;
}
