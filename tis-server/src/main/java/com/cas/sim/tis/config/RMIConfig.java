package com.cas.sim.tis.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.StudentService;
import com.cas.sim.tis.services.TeacherService;
import com.cas.sim.tis.services.UserService;

/**
 * 远程方法调用（RMI：Remote Method invoke）相关配置<br>
 * 相应地，客户端也要有套类似的配置{@link ClientRMIConfig}
 */
@Configuration
public class RMIConfig {
	@Resource
	private UserService userService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private StudentService studentService;
	@Resource
	private TeacherService teacherService;

	@Value("${server.rmi.registry}")
	private Integer registPort;
	@Value("${server.rmi.service}")
	private Integer servicePort;

	@Bean
	public RmiServiceExporter userServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(UserService.class);
		exporter.setServiceName("userService");
		exporter.setService(userService);
		
		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}
	
	@Bean
	public RmiServiceExporter resourceServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(ResourceService.class);
		exporter.setServiceName("resourceService");
		exporter.setService(resourceService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter studentServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(StudentService.class);
		exporter.setServiceName("studentService");
		exporter.setService(studentService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}
	
	@Bean
	public RmiServiceExporter teacherServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(TeacherService.class);
		exporter.setServiceName("teacherService");
		exporter.setService(teacherService);
		
		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

}
