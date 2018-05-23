/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.util.StringUtils;

import com.cas.sim.tis.consts.LoginResult;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.message.handler.ExamMessageHandler;
import com.cas.sim.tis.message.handler.LoginMessageHandler;
import com.cas.sim.tis.message.handler.SerializerRegistrationsMessageHandler;
import com.cas.sim.tis.message.listener.ClientMessageListener;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.AppPropertiesUtil;
import com.cas.sim.tis.util.SocketUtil;
import com.cas.sim.tis.view.control.imp.LoginDecoration;
import com.jme3.network.message.SerializerRegistrationsMessage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

/**
 * Login Controller.
 */
@Slf4j
public class LoginController implements Initializable {
	protected double xOffset;
	protected double yOffset;

	public static int USER_ROLE = -1;

	private ResourceBundle resources;

	@FXML
	private Region loginView;
	@FXML
	private Region loginBtn;
	@FXML
	private LoginDecoration loginDecoration;
	@FXML
	private TextField userId;
	@FXML
	private PasswordField password;
	@FXML
	private Label status;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
		userId.setText(AppPropertiesUtil.getStringValue("login.account"));
	}

	@FXML
	public void processLogin() {
		setStatusMsgKey("server.connect.waiting");
//		0、验证登录信息的完整性
		if (StringUtils.isEmpty(userId.getText())) {
			setStatusMsgKey("login.account.notnull");
			return;
		}
		if (StringUtils.isEmpty(password.getText())) {
			setStatusMsgKey("login.password.notnull");
			return;
		}
//		我这里把登录按钮禁用掉，你登录失败后记得再启动
		loginBtn.setDisable(true);

//		这里应当启动一个线程去登录，以免影响页面的渲染
//		将登录结果的处理一并告诉那个线程
		String address = AppPropertiesUtil.getStringValue("server.base.address");
		int port = AppPropertiesUtil.getIntValue("server.base.port", 0);
//		ip和端口都给你了， 你去找连服务器吧。
		new Thread(() -> {
			log.info("连接服务器{}:{}", address, port);
			SocketUtil.INSTENCE.connect(address, port, //
					result -> Platform.runLater(() -> connectServerResult(result)));
		}).start();
	}

//	连接服务器结果的处理
	private void connectServerResult(boolean result) {
//		连接成功
		if (result) {
			setStatusMsgKey("server.connect.success");

//			注册消息及消息处理类
			LoginMessageHandler loginMessageHandler = new LoginMessageHandler();
			ClientMessageListener.INSTENCE.registerMessageHandler(LoginMessage.class, loginMessageHandler);
			loginMessageHandler.setLoginUIController(this);

			ClientMessageListener.INSTENCE.registerMessageHandler(SerializerRegistrationsMessage.class, new SerializerRegistrationsMessageHandler());
			ClientMessageListener.INSTENCE.registerMessageHandler(ExamMessage.class, new ExamMessageHandler());

			SocketUtil.INSTENCE.start();

//			3、项服务器发送登录消息
			LoginMessage msg = new LoginMessage();
			msg.setUserCode(userId.getText());
			msg.setUserPwd(password.getText());
			SocketUtil.INSTENCE.send(msg);
			log.info("发送登录请求。。。");
		} else {
//			连接失败
			loginBtn.setDisable(false);
			setStatusMsgKey("server.connect.failure");
		}
	}

//	登录失败
	public void failure(LoginMessage m) {
		String messageKey = m.getResult().getMsgKey();
		if (LoginResult.DUPLICATE == m.getResult()) {
			AlertUtil.showConfirm(resources.getString(messageKey), resp -> {
				if (ButtonType.YES == resp) {
					loginBtn.setDisable(true);
					LoginMessage msg = new LoginMessage();
					msg.setUserCode(userId.getText());
					msg.setUserPwd(password.getText());
					msg.setFocus(true);
					SocketUtil.INSTENCE.send(msg);
				}
			});
		} else {
			setStatusMsgKey(messageKey);
		}
		loginBtn.setDisable(false);
	}

	public void setStatusMsgKey(String messageKey) {
		status.setText(resources.getString(messageKey));
		log.debug(status.getText());
	}

	public void setSettingView(Region settingView) {
		this.loginDecoration.setSettingView(settingView);
	}

	public void close() {
		loginView.getScene().getWindow().hide();
	}

}
