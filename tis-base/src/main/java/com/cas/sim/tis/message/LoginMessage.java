package com.cas.sim.tis.message;

import com.cas.sim.tis.consts.LoginResult;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 客户端登录的消息组件
 * @author Administrator
 */
@Serializable()
public class LoginMessage extends AbstractMessage {

//	默认是学生类型
	private int userId;
	private int userType;
	private String userCode;
	private String userPwd;
	private boolean focus;// 强制登录

	private LoginResult result;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public LoginResult getResult() {
		return result;
	}

	public void setResult(LoginResult result) {
		this.result = result;
	}

}
