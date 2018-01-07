package com.cas.gas.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

/**
 * 通阻关系信息封装对象
 * @功能 BlockState.java
 * @作者 CWJ
 * @创建日期 2016年5月18日
 * @修改人 CWJ
 */

@XmlAccessorType(XmlAccessType.NONE)
public class BlockState  {

	@XmlAttribute
	private String id;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean isDef;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDef() {
		return isDef;
	}
	public void setDef(boolean isDef) {
		this.isDef = isDef;
	}

}
