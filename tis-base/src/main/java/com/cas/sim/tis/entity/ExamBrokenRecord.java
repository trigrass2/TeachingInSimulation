package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamBrokenRecord {
	@Id
	private Integer id;
	@Column(name = "PID")
	private Integer publishId;
	private String archivePath;
	private Integer brokenNum;
	private Integer correctedNum;
	private Integer mistakeNum;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
}
