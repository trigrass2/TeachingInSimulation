/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.view.control.imp.LoginDecoration;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * 设置服务器信息界面
 * @功能 NetworkController.java
 * @作者 Caowj
 * @创建日期 2018年1月17日
 * @修改人 Caowj
 * @修改人 张振宇
 */
public class NetworkController implements Initializable {
	
	private static final Logger LOG = LoggerFactory.getLogger(NetworkController.class);
	@FXML
	private VBox pane;
	@FXML
	private LoginDecoration loginDecoration;
	@FXML
	private TextField baseIP;
	@FXML
	private TextField basePort;
	@FXML
	private TextField ftpIP;
	@FXML
	private TextField ftpPort;
	@FXML
	private TextField httpdIP;
	@FXML
	private TextField httpdPort;

//	@Value("${server.base.address}")
//	private String address;
//
//	@Value("${server.base.port}")
//	private Integer num;

	private Properties prop = new Properties();

	private Region loginScene;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 读取服务器信息配置
		try {
			prop.load(new FileInputStream("cfg.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		baseIP.setText(prop.getProperty("server.base.address", "127.0.0.1"));
		basePort.setText(prop.getProperty("server.base.port", "9000"));
		ftpIP.setText(prop.getProperty("server.ftp.address", "127.0.0.1"));
		ftpPort.setText(prop.getProperty("server.ftp.port", "21"));
		httpdIP.setText(prop.getProperty("server.httpd.address", "127.0.0.1"));
		httpdPort.setText(prop.getProperty("server.httpd.port", "8082"));
	}
	
	@FXML
	public void ok() throws FileNotFoundException, IOException {
		// 记录服务器信息
		prop.setProperty("server.base.address", baseIP.getText());
		prop.setProperty("server.base.port", basePort.getText());
		prop.setProperty("server.ftp.address", ftpIP.getText());
		prop.setProperty("server.ftp.port", ftpPort.getText());
		prop.setProperty("server.httpd.address", httpdIP.getText());
		prop.setProperty("server.httpd.port", httpdPort.getText());
		
		LOG.info("修改与服务器的连接配置，修改后的服务器地址:{}, 端口:{}", baseIP.getText(), basePort.getText());
//		服务器信息保存
		prop.store(new FileOutputStream("cfg.properties"), "");
		back();
	}

	/**
	 * 取消
	 */
	@FXML
	public void cancel() {
		back();
	}

	/**
	 * 返回登录界面
	 */
	private void back() {
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), pane.getScene().getRoot());
		rotateTransition.setAxis(new Point3D(0, 1, 0));
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(90);
		rotateTransition.setOnFinished(e -> {
//			Application.showView(LoginView.class);
			
			Parent root = pane.getScene().getRoot();
			
			pane.getScene().setRoot(loginScene);
			
			RotateTransition r = new RotateTransition(Duration.millis(200), root);
			r.setAxis(new Point3D(0, 1, 0));
			r.setFromAngle(90);
			r.setToAngle(0);
			r.play();
//			ip.setText(address);
//			port.setText(String.valueOf(num));
		});
		rotateTransition.play();
	}
	
	public void setLoginView(Region loginScene) {
		this.loginScene = loginScene;
		setSettingView(loginScene);
	}
	
	private void setSettingView(Region settingView) {
		this.loginDecoration.setSettingView(settingView);
	}
}
