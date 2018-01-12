package com.cas.sim.tis.socket.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 客户端登录的消息组件
 * @author Administrator
 */
@Serializable()
public class LoginMessage extends AbstractMessage {

	public static final int RESULT_FAILURE = 0;

	public static final int RESULT_SUCCESS = 1;
	/**
	 * 重复登录
	 */
	public static final int RESULT_DUPLICATE = 2;
	/**
	 * 人员已满
	 */
	public static final int RESULT_MAX_SIZE = 3;
	/**
	 * 有新用户登录
	 */
	public static final int CLIENT_LOGIN = 10;

	/**
	 * 有客户端离线
	 */
	public static final int CLIENT_LOGOUT = 11;

//	默认是学生类型
	private int userType;
	private String userCode;
	private String userPwd;

	private int result;

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

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
