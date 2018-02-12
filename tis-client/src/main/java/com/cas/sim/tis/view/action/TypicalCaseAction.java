package com.cas.sim.tis.view.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.services.TypicalCaseService;

@Component
public class TypicalCaseAction {
	@Resource
	@Qualifier("typicalCaseServiceFactory")
	private RmiProxyFactoryBean typicalCaseServiceFactory;

	public List<TypicalCase> getTypicalCaseList() {

		TypicalCaseService compService = (TypicalCaseService) typicalCaseServiceFactory.getObject();

		return compService.findAll();
	}

}
