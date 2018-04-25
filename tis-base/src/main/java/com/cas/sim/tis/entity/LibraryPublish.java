package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

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
public class LibraryPublish implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -90069710975023747L;

	public enum LibraryPublishType {
		PRACTICE(0), EXAM(1);

		private int type;

		private LibraryPublishType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}
	@Id
	private Integer id;
	@Column(name = "LID")
	private Integer libraryId;
	@Column(name = "CID")
	private Integer classId;
	/**
	 * 0-个人练习 <br>
	 * 1-教师考核 <br>
	 */
	private Integer type;
	private Float average;
	private Integer creator;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createDate;
	private Boolean state = false;
	private Library library;
	private Class clazz;
}
