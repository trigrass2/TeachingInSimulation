package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.services.LibraryAnswerService;

@Component
public class LibraryAnswerAction {
	@Resource
	@Qualifier("libraryAnswerServiceFactory")
	private RmiProxyFactoryBean libraryAnswerServiceFactory;

	public List<LibraryAnswer> findAnswersByPublish(int pid) {
		LibraryAnswerService service = (LibraryAnswerService) libraryAnswerServiceFactory.getObject();
		return service.findAnswersByPublish(pid);
	}

}
