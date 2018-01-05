package com.cas.sim.tis.socket.message;

import com.jme3.network.AbstractMessage;

public class BaseMessage extends AbstractMessage {
	private int reason;

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

}
