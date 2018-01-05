package com.cas.gas.po;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 气口信息对象
 * @功能 GasPortPO.java
 * @作者 CWJ
 * @创建日期 2016年5月18日
 * @修改人 CWJ
 */
public class GasPort {
	// 气口编号
	private String id;
	// 气口名称
	private String name;
	// 气口模型名
	private String mdlName;
	// 气口接入气管的轴方向
	private String direction;
	// 气口类型正常气口、消声器
	private String type;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id + "-" + name;
	}
}
