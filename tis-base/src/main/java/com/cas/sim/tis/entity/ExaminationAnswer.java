package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//试卷作答结果
public class ExaminationAnswer {
	@Id
	private Integer id;
//	试卷ID
	private Integer examId;
//	我的答案(题目类型，题目ID，我的答案)
	private String answers;
//	试卷ID
	private Float score;
//	创建人id
	private Integer creatorId;
//	开始考试的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date examTime;
//	提交试卷的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date submitTime;
	private Integer del = 0;
}
