/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates. All rights reserved. Use is subject to license terms. This file is available and licensed under the following license: Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. - Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.cas.sim.tis.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.StudentService;
import com.cas.sim.tis.services.TeacherService;
import com.jme3.network.NetworkClient;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Login Controller.
 */
@FXMLController
@PropertySource(value = { "file:cfg.properties" })
public class LoginController extends AnchorPane implements Initializable, ApplicationContextAware {
	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	protected double xOffset;
	protected double yOffset;

	public static int USER_ROLE = -1;

	@Resource
	private NetworkClient client;

	@Value("${login.account}")
	private String account;

	@Value("${server.base.address}")
	private String address;

	@Value("${server.base.port}")
	private Integer port;

	@Value("${server.rmi.registry}")
	private Integer rmiPort;

	@Resource
	private MessageSource messageSource; // 自动注入对象

	@FXML
	private TextField userId;
	@FXML
	private PasswordField password;
	@FXML
	private Button login;
	@FXML
	private Label errorMessage;

	private ApplicationContext applicationContext;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userId.setText(account);
	}

	@FXML
	public void processLogin() {
//		0、验证登录信息的完整性
		if (StringUtils.isEmpty(userId.getText())) {
			setErrorMsg(messageSource.getMessage("login.account.notnull", null, Locale.getDefault()));
			return;
		}
		if (StringUtils.isEmpty(password.getText())) {
			setErrorMsg(messageSource.getMessage("login.password.notnull", null, Locale.getDefault()));
			return;
		}

		if (!client.isStarted()) {
//			1、尝试与服务器连接
			try {
				client.connectToServer(address, port, port);
			} catch (IOException e) {
				LOG.error("连接服务器失败IP：{}，端口：{}", address, port);
				setErrorMsg(messageSource.getMessage("server.connect.failure", null, Locale.getDefault()));
				return;
			}
//			2、启动客户端
			client.start();

//			注册远程服务
			new Thread(() -> {
				Map<String, Class<?>> map = new HashMap<>();
				map.put("resourceServiceFactory", ResourceService.class);
				map.put("studentServiceFactory", StudentService.class);
				map.put("teacherServiceFactory", TeacherService.class);
//				TODO 注册更多的远程服务
				DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
				map.entrySet().forEach(e -> {
//					注册的对象通过SpringUtil.getBean获取
					BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RmiProxyFactoryBean.class);
					String serviceUrl = "rmi://" + address + ":" + rmiPort + "/" + StringUtils.uncapitalize(e.getValue().getSimpleName());
					LoggerFactory.getLogger(getClass()).info("远程访问路径：{}", serviceUrl);
					beanDefinitionBuilder.addPropertyValue("serviceUrl", serviceUrl);
					beanDefinitionBuilder.addPropertyValue("serviceInterface", e.getValue());
//					动态注册bean.  
					defaultListableBeanFactory.registerBeanDefinition(e.getKey(), beanDefinitionBuilder.getBeanDefinition());
				});
			}).start();
		}

//		3、项服务器发送登录消息
		LoginMessage msg = new LoginMessage();
		msg.setUserType(USER_ROLE);
		msg.setUserCode(userId.getText());
		msg.setUserPwd(password.getText());
		client.send(msg);
	}

	public void setErrorMsg(String msg) {
		errorMessage.setText(msg);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
