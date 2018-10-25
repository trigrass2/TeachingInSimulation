package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//（老师准备的）一堂课程，也叫一次备课
public class Lesson {
	@Id
	private Integer id;
//	备课的老师
	private Integer tid;
//	章节课时的id
	private Integer sectionId;
//	章节课时的id
	private Integer creatorId;
//	完成备课的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
//	修改备课的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updateDate;
	private Integer del = 0;
}
