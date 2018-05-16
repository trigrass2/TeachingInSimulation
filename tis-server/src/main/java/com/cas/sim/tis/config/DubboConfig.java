package com.cas.sim.tis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
@Configuration
public class DubboConfig {
	
	@Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider-test");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setClient("curator");
        return registryConfig;
    }
    
	@Bean
	public ServiceBean<UserService> userServiceExporter(UserService service) {
		ServiceBean<UserService> serviceBean = new ServiceBean<UserService>();
        serviceBean.setVersion("1.0.0");
        serviceBean.setInterface(UserService.class.getName());
        serviceBean.setRef(service);
        serviceBean.setTimeout(5000);
        serviceBean.setRetries(3);
        return serviceBean;
	}
}
