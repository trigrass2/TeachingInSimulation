package com.cas.sim.tis.config;

import org.apache.commons.net.ftp.FTPClient;
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
	@Value(value = "${server.ftp.address}")
	private String ftpAddress;

	@Value(value = "${server.ftp.port}")
	private Integer ftpPort;

	@Value(value = "${server.httpd.address}")
	private String httpdAddress;

	@Value(value = "${server.httpd.port}")
	private Integer httpdPort;

	@Bean
	public FTPUtils buildFtpClient() {
		FTPUtils util = new FTPUtils();

		FTPClient client = new FTPClient();
		util.setFtpClient(client);

		util.setHost(ftpAddress);
		util.setPort(ftpPort);
		util.setUsername("admin");
		util.setPassword("admin");

		try {
			SocketTest.test(ftpAddress, ftpPort);
		} catch (RuntimeException e) {
			throw e;
		}

		return util;
	}

	@Bean
	public HTTPUtils buildHttpUtil() {
		HTTPUtils util = new HTTPUtils();

		util.setHost(httpdAddress);
		util.setPort(httpdPort);

		try {
			SocketTest.test(httpdAddress, httpdPort);
		} catch (RuntimeException e) {
			throw e;
		}
		return util;
	}
}
