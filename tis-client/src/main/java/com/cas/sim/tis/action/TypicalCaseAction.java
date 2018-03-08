package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.services.TypicalCaseService;

@Component
public class TypicalCaseAction extends BaseAction<TypicalCaseService> {
	@Resource
	@Qualifier("typicalCaseServiceFactory")
	private RmiProxyFactoryBean typicalCaseServiceFactory;

	public TypicalCase findTypicalCaseById(Integer id) {
		return getService().findById(id);
	}

	public List<TypicalCase> getTypicalCaseList() {

		return getService().findAll();
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return typicalCaseServiceFactory;
	}

}
