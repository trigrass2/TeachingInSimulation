package com.cas.sim.tis.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.FtpAttr;

@Configuration
@PropertySource("file:cfg.properties")
public class ClientConfig {
	@Value(value = "${server.base.address}")
	private String address;

	@Value(value = "${server.ftp.port}")
	private Integer port;

	@Bean
	public FTPUtils buildFtpClient() {
		FTPUtils util = new FTPUtils();

		FTPClient client = new FTPClient();
		util.setFtpClient(client);

		FtpAttr attr = new FtpAttr();
		attr.setHost(address);
		attr.setPort(port);
		attr.setUsername("anonymous");
		attr.setPassword("");
		util.setAttr(attr);

		return util;
	}

}
