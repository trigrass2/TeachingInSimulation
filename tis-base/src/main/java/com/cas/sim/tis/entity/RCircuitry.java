package com.cas.sim.tis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class RCircuitry {
	@Id
	protected Integer id;
//	电路名称
	protected String name;
//	电路存档路径（在服务器中的文件路径，服务器提供给客户端的是FTP://server_ip:port/path）
	protected String path;
//	电路描述
	@Column(name = "COMMENT")
	protected String desc;

//	图纸ID
	protected Integer drawId;

//	创建人（教师/管理员/学生）
	protected Integer creatorRole;
//	创建人ID
	protected Integer creatorId;

	protected Date createDate;

	protected Date updateDate;

	protected Integer del = 0;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getCreatorRole() {
		return creatorRole;
	}

	public void setCreatorRole(Integer creatorRole) {
		this.creatorRole = creatorRole;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

}
