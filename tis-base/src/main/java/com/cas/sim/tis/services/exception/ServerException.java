package com.cas.sim.tis.services.exception;

/**
 * 服务器异常
 * @author Administrator
 */
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