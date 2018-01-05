package com.cas.sim.tis.socket.message;

import com.jme3.network.serializing.Serializable;

@Serializable()
public class LoginMessage extends BaseMessage {

	public static final byte RESULT_FAILURE = 0;

	public static final byte RESULT_SUCCESS = 1;
	/**
	 * 重复登录
	 */
	public static final byte RESULT_DUPLICATE = 2;
	/**
	 * 人员已满
	 */
	public static final byte RESULT_MAX_SIZE = 3;
	/**
	 * 有新用户登录
	 */
	public static final byte CLIENT_NEW_LOGGED = 10;

	/**
	 * 有客户端离线
	 */
	public static final byte CLIENT_LOGOUT = 11;

//	默认是学生类型
	private int userType;
	private String userCode;
	private String userPwd;
	private String content;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
