package com.cas.sim.tis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cas.sim.tis.thrift.ThriftClient;

@Configuration
public class ThriftClientConfig {
	@Value("${thrift.host}")
	private String host;
	@Value("${thrift.port}")
	private int port;

	@Bean(initMethod = "init")
	public ThriftClient thriftClient() {
		ThriftClient jazzClient = new ThriftClient();
		jazzClient.setHost(host);
		jazzClient.setPort(port);
		return jazzClient;
	}
}
