package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

@XmlAccessorType(XmlAccessType.NONE)
public class LightIO {// extends BaseVO<LightIOPO> {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA glowColor;

	private Spatial spatial;

	public void openLight() {
//		JmeUtil.setSpatialHighLight(model, glowColor);
	}

	public void closeLight() {
//		JmeUtil.setSpatialHighLight(model, ColorRGBA.BlackNoAlpha);
	}

	public void setSpatial(Spatial spatial) {
//		if (model == null) {
//			log.error("没有找到指示灯名为" + po.getName() + "的模型");
//		}
		this.spatial = spatial;
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public String getMdlName() {
		return mdlName;
	}
}
