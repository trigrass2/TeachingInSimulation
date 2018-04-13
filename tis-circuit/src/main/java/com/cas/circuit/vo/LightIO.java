package com.cas.circuit.vo;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.circuit.xml.adapter.ColorRGBAAdapter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

@XmlAccessorType(XmlAccessType.NONE)
public class LightIO implements Savable {// extends BaseVO<LightIOPO> {
	private static final Logger LOG = LoggerFactory.getLogger(Terminal.class);
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
		if (spatial == null) {
			LOG.error("没有找到LightIO::name为{}的模型{}", name, mdlName);
		}
		this.spatial = spatial;
		spatial.setUserData("entity", this);
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public String getMdlName() {
		return mdlName;
	}

	public String getName() {
		return name;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void read(JmeImporter im) throws IOException {
		// TODO Auto-generated method stub

	}
}
