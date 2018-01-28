package com.cas.sim.tis.config;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.SocketTest;

@Configuration
@PropertySource("file:cfg.properties")
public class ClientConfig {
	@Value(value = "${server.base.address}")
	private String address;

	@Value(value = "${server.ftp.port}")
	private Integer port;

//	@Value(value = "${server.base.address}")
//	private String address;

	@Value(value = "${server.httpd.port}")
	private Integer apachePort;

	@Bean
	public FTPUtils buildFtpClient() {
		FTPUtils util = new FTPUtils();

		FTPClient client = new FTPClient();
		util.setFtpClient(client);

		util.setHost(address);
		util.setPort(port);
		util.setUsername("anonymous");
		util.setPassword("");

		try {
			SocketTest.test(address, apachePort);
		} catch (RuntimeException e) {
			throw e;
		}

		return util;
	}

	@Bean
	public HTTPUtils buildHttpUtil() {
		HTTPUtils util = new HTTPUtils();

		util.setHost(address);
		util.setPort(apachePort);

		try {
			SocketTest.test(address, apachePort);
		} catch (RuntimeException e) {
			throw e;
		}
		return util;
	}
}
