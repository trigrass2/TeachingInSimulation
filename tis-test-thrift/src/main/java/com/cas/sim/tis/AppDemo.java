package com.cas.sim.tis;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.sim.tis.thrift.ThriftServer;

@SpringBootApplication
public class AppDemo implements ApplicationRunner {
	@Resource
	private ThriftServer thriftServer;

	public static void main(String[] args) {
		SpringApplication.run(AppDemo.class);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		thriftServer.start();
	}
}
