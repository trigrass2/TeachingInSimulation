package com.cas.gas.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * 通阻状态信息对象
 * @功能 BlockStatePO.java
 * @作者 CWJ
 * @创建日期 2016年5月18日
 * @修改人 CWJ
 */
public class BlockState {
	private String id;
	private String isDef;

	private List<BlockRelation> blockRelationList;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getIsDef() {
		return isDef;
	}

	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

	@XmlElement(name = "BlockRelation")
	public List<BlockRelation> getBlockRelationList() {
		return blockRelationList;
	}

	public void setBlockRelationList(List<BlockRelation> blockRelationList) {
		this.blockRelationList = blockRelationList;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "id=" + id + ", isDef=" + isDef;
	}

}
