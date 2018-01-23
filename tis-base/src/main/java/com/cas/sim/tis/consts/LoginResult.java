package com.cas.sim.tis.consts;

public enum LoginResult {
	/**
	 * 登录失败
	 */
	FAILURE("login.failure"),
	/**
	 * 登录成功
	 */
	SUCCESS("login.failure.success"),
	/**
	 * 用户已经登录
	 */
	DUPLICATE("login.failure.duplicate"),
	/**
	 * 客户端人数已满
	 */
	MAX_SIZE("login.failure.max_size"),
	/**
	 * 服务器发生错误
	 */
	SERVER_EXCE("login.failure.exception");

	private String msgKey;

	private LoginResult(String msgKey) {
		this.msgKey = msgKey;
	}

	public String getMsg() {
		return msgKey;
	}
}
