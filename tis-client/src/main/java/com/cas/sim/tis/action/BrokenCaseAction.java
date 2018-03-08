package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.services.BrokenCaseService;

@Component
public class BrokenCaseAction extends BaseAction<BrokenCaseService> {
	@Resource
	@Qualifier("brokenCaseServiceFactory")
	private RmiProxyFactoryBean brokenCaseServiceFactory;

	public List<BrokenCase> getBrokenCaseList() {

		return getService().findAll();
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return brokenCaseServiceFactory;
	}

}
