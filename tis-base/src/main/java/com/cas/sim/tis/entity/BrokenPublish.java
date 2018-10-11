package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokenPublish {
	@Id
	private Integer id;
	@Column(name = "BID")
	private Integer brokenId;
	@Column(name = "CID")
	private Integer classId;
	private Integer publisher;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date publishDate;
	private Boolean state = false;
	private BrokenCase brokenCase;
	private Class clazz;
}
