package com.cas.circuit.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.cas.circuit.BaseElectricCompLogic;

public class CompLogicAdapter extends XmlAdapter<String, BaseElectricCompLogic> {

	@Override
	public BaseElectricCompLogic unmarshal(String v) throws Exception {
		if (v == null) {
			return new BaseElectricCompLogic();
		}
		try {
			return (BaseElectricCompLogic) Class.forName(v).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public String marshal(BaseElectricCompLogic v) throws Exception {
		if (v == null) {
			return null;
		}
		return v.getClass().getName();
	}

}
