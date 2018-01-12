package com.cas.sim.tis.socket.serializers;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.jme3.network.serializing.Serializer;

public class IntSerializer extends Serializer {

	public Integer readObject(ByteBuffer data, Class c) throws IOException {
		return data.getInt();
	}

	public void writeObject(ByteBuffer buffer, Object object) throws IOException {
		if (object == null) {
			return;
		}
		buffer.putInt((Integer) object);
	}
}
