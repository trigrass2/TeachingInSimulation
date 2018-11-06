package com.cas.sim.tis.vo;

import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.entity.Library;

import lombok.Getter;
import lombok.Setter;

/**
 * 试题发布记录表
 * @功能 LibraryPublish.java
 * @作者 Caowj
 * @创建日期 2018年2月7日
 * @修改人 Caowj
 */
@Getter
@Setter
public class ExamLibraryPublish extends ExamPublish {
	private Library library;
}
