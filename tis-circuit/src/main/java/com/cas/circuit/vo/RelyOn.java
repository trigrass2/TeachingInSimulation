package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.FloatArrayAdapter;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.cas.circuit.xml.adapter.Vector3fAdapter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

@XmlAccessorType(XmlAccessType.NONE)
public class RelyOn {
	public static final int RELY_ON_TYPE_PLUG = 1;
	public static final int RELY_ON_TYPE_RESIS = 2;

	@XmlAttribute
	private int type;
	@XmlAttribute
	@XmlJavaTypeAdapter(Vector3fAdapter.class)
	private Vector3f localTranslation;
	@XmlAttribute
	@XmlJavaTypeAdapter(FloatArrayAdapter.class)
	private float[] localRotation;
	@XmlAttribute
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] relyIds;
	// 依赖底座
	private ElecCompDef baseDef;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Vector3f getLocalTranslation() {
		return localTranslation;
	}

	public Quaternion getLocalRotations() {
		return new Quaternion(localRotation);
	}

	public String[] getRelyIds() {
		return relyIds;
	}

	public void setRelyIds(String[] relyIds) {
		this.relyIds = relyIds;
	}

	public ElecCompDef getBaseDef() {
		return baseDef;
	}

	public void setBaseDef(ElecCompDef baseDef) {
		this.baseDef = baseDef;
	}
	
//	public void unbind() {
//		for (String relyId : relyIds) {
//			Terminal terminal = getTerminal(relyId);
//			terminal.getContacted().setContacted(null);
//			terminal.setContacted(null);
//		}
//		base.setRelyOnPlug(null);
//	}

//	public void bind(Base base) {
//		if (base == null) {
//			unbind();
//			return;
//		}
//		for (String relyId : relyIds) {
//			Terminal terminal1 = getTerminal(relyId);
//			Terminal terminal2 = base.getTerminal(relyId);
//			terminal1.setContacted(terminal2);
//			terminal2.setContacted(terminal1);
//		}
//		base.setRelyOnPlug(this);
//	}
}
