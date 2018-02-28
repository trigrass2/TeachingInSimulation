package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 试题发布记录表
 * @功能 LibraryPublish.java
 * @作者 Caowj
 * @创建日期 2018年2月7日
 * @修改人 Caowj
 */
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Float getAverage() {
		return average;
	}

	public void setAverage(Float average) {
		this.average = average;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
