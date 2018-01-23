/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.view.control.imp.LoginDecoration;
import com.jme3.network.NetworkClient;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 * Login Controller.
 */
public class LoginController implements Initializable {
	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	protected double xOffset;
	protected double yOffset;

	public static int USER_ROLE = -1;

	private NetworkClient client;

	private ResourceBundle resources;

	@FXML
	private Region loginView;
	@FXML
	private LoginDecoration loginDecoration;
	@FXML
	private TextField userId;
	@FXML
	private PasswordField password;
	@FXML
	private Label errorMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
//		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("cfg.properties"));
		} catch (FileNotFoundException e) {
			LOG.warn("未找到配置文件cfg.properties");
		} catch (IOException e) {
			LOG.warn("文件解析失败cfg.properties");
		}
		userId.setText(prop.getProperty("login.account", ""));
	}

	@FXML
	public void processLogin() {
//		0、验证登录信息的完整性
		if (StringUtils.isEmpty(userId.getText())) {
			setErrorMsg(resources.getString("login.account.notnull"));
			return;
		}
		if (StringUtils.isEmpty(password.getText())) {
			setErrorMsg(resources.getString("login.password.notnull"));
			return;
		}

		if (!client.isConnected()) {
			String address = null;
			int port = 0;
//			1、尝试与服务器连接
			try {
				Properties prop = new Properties();
				prop.load(new FileInputStream("cfg.properties"));
				address = prop.getProperty("server.base.address", "127.0.0.1");
				port = Integer.parseInt(prop.getProperty("server.base.port", "9000"));
				client.connectToServer(address, port, port);
			} catch (IOException e) {
				LOG.error("连接服务器失败IP：{}，端口：{}", address, port);
				setErrorMsg(resources.getString("server.connect.failure"));
				return;
			}
//			2、启动客户端
			client.start();
		}

//		3、项服务器发送登录消息
		LoginMessage msg = new LoginMessage();
		msg.setUserCode(userId.getText());
		msg.setUserPwd(password.getText());
		client.send(msg);
	}

	public void setErrorMsg(String msg) {
		errorMessage.setText(msg);
	}

	public void setSettingView(Region settingView) {
		this.loginDecoration.setSettingView(settingView);
	}

	public void setClient(NetworkClient client) {
		this.client = client;
	}

	public void close() {
		loginView.getScene().getWindow().hide();
	}

}
