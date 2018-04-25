package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

//备课的资源
@Getter
@Setter
public class LessonResource {
//	普通资源
	public static final int TYPE_NORMAL = 0;
//	仿真资源-认知部分
	public static final int TYPE_COGNITION = 1;
//	仿真资源-电路部分
	public static final int TYPE_CIRCUITRY = 2;
	@Id
	private Integer id;
//	备课老师的ID
	private Integer tid;
//	资源所属章节课时ID
	private Integer lessonId;
////	资源类型
//	/**
//	 * LessonResource.TYPE_NORMAL<br>
//	 * LessonResource.TYPE_COGNITION<br>
//	 * LessonResourceTYPE_CIRCUITRY
//	 */
//	private Integer type;

//	资源ID
	private Integer resourceId;
//	老师teacherId 在lessonId 下备课，资源的顺序（排序用）
	private Integer sort;
//	完成备课的时间
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
//	资源ID
	private Integer del = 0;
}
