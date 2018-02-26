package com.cas.circuit.vo.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.QuaternionAdapter;
import com.cas.circuit.xml.adapter.Vector3fAdapter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

@XmlAccessorType(XmlAccessType.NONE)
public class ElecCompProxy {
//	元器件ID
	@XmlAttribute
	private String model;
//	元器件ID
	@XmlAttribute
	private String tagName;

//	摆放位置
	@XmlAttribute
	@XmlJavaTypeAdapter(value = Vector3fAdapter.class)
	private Vector3f location = Vector3f.ZERO;

//	旋转量
	@XmlAttribute
	@XmlJavaTypeAdapter(value = QuaternionAdapter.class)
	private Quaternion rotation = Quaternion.ZERO;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

}
