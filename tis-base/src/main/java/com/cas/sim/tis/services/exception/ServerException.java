package com.cas.sim.tis.services.exception;

import io.airlift.drift.annotations.ThriftException;

/**
 * 服务器异常
 * @author Administrator
 */
@ThriftException(id = 1, type = RuntimeException.class)
public class ServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1438026008296383821L;

	public ServerException() {
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}
}