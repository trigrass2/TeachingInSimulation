package com.cas.sim.tis.message;

import com.cas.sim.tis.consts.LoginResult;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端登录的消息组件
 * @author Administrator
 */
@Serializable()
@Getter
@Setter
public class LoginMessage extends AbstractMessage {

//	默认是学生类型
	private int userId;
	private int userType;
	private String userCode;
	private String userPwd;
	private boolean focus;// 强制登录

	private LoginResult result;
	
	private String user;
}
