package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

//资源
public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5682087546758059830L;

	public static final int RES_WORD = 0;
	public static final int RES_PPT = 1;
	public static final int RES_EXCEL = 2;
	public static final int RES_PDF = 3;
	public static final int RES_SWF = 4;
	public static final int RES_IMAGE = 5;
	public static final int RES_VIDEO = 6;

	@Id
	protected Integer id;
	protected String name;
//	0-Word
//	1-EXCEL
//	2-PPT
//	3-PDF
//	4-SWF
//	5-PNG/JPG/GIF
//	6-MP4/WMV/RMVB/FLV/AVI
	protected Integer type;
//	资源路径（在服务器中的文件路径，服务器提供给客户端的是FTP://server_ip:port/path）
	protected String path;
//	关键词（“,”分割）
	private String keyword;
//	资源描述
	@Column(name = "DETAIL")
	private String desc;
//	创建人（教师/管理员）
	protected Integer creator;
//	默认资源是私有的
	@Column(name = "OPEN")
	protected Boolean share = Boolean.FALSE;
	protected Date createDate;
//	默认资源没有被删除
	protected Boolean del = Boolean.FALSE;

	private Integer browsedTimes;
	private Integer collectedTimes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public Boolean getShare() {
		return share;
	}

	public void setShare(Boolean share) {
		this.share = share;
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}

	public Integer getBrowsedTimes() {
		return browsedTimes;
	}

	public void setBrowsedTimes(Integer browsedTimes) {
		this.browsedTimes = browsedTimes;
	}

	public Integer getCollectedTimes() {
		return collectedTimes;
	}

	public void setCollectedTimes(Integer collectedTimes) {
		this.collectedTimes = collectedTimes;
	}

}
