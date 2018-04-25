package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//试卷
public class Examination {
	@Id
	private Integer id;
//	考试试卷名称
	private String name;
//	试卷所引用的试题（试题类型：试题ID）
	private String questionIds;
//	创建人id
	private Integer creatorId;
//	创建的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
//	修改的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Integer del = 0;
}
