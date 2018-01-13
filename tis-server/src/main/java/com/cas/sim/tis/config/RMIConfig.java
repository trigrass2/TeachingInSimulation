package com.cas.sim.tis.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.cas.sim.tis.services.ResourceService;

/**
 * 远程方法调用（RMI：Remote Method invoke）相关配置
 */
@Configuration
public class RMIConfig {
	@Resource
	private ResourceService resourceService;

	@Bean
	public RmiServiceExporter resourceServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(ResourceService.class);
		exporter.setServiceName("resourceService");
		exporter.setService(resourceService);
		exporter.setServicePort(9901);
		return exporter;
	}
}
