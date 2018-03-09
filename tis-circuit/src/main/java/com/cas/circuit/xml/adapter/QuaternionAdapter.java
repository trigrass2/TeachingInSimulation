package com.cas.circuit.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

public class QuaternionAdapter extends XmlAdapter<String, Quaternion> {

	@Override
	public Quaternion unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		String[] arr = v.split(",");

		Quaternion quaternion = new Quaternion();
		float x = Float.parseFloat(arr[0]) * FastMath.DEG_TO_RAD;
		float y = Float.parseFloat(arr[1]) * FastMath.DEG_TO_RAD;
		float z = Float.parseFloat(arr[2]) * FastMath.DEG_TO_RAD;
		float w = Float.parseFloat(arr[3]) * FastMath.DEG_TO_RAD;

		quaternion.set(x, y, z, w);
		return quaternion;
	}

	@Override
	public String marshal(Quaternion v) throws Exception {
		if (v == null) {
			return null;
		}
		v.multLocal(FastMath.RAD_TO_DEG);

		return v.getX() + "," + v.getY() + "," + v.getZ() + "," + v.getW();
	}

}
