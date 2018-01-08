package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class SensorIO {// extends SwitchCtrl<SensorIOPO> {
	@XmlAttribute private String switchIn;

//	@Override
//	protected void toValueObject() {
//		super.toValueObject();
//		if (!Util.isEmpty(po.getSwitchIn())) {
//			resisStateIds = StringUtil.split(po.getSwitchIn());
//		}
//	}

}
