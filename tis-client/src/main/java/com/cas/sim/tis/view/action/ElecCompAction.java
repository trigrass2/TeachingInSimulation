package com.cas.sim.tis.view.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.services.ElecCompService;

@Component
public class ElecCompAction {
	@Resource
	@Qualifier("elecCompServiceFactory")
	private RmiProxyFactoryBean elecCompServiceFactory;

	public List<ElecComp> getElecCompList() {

		ElecCompService compService = (ElecCompService) elecCompServiceFactory.getObject();

		return compService.findAll();
	}

	public Map<String, List<ElecComp>> getElecCompMap() {

		ElecCompService compService = (ElecCompService) elecCompServiceFactory.getObject();

		return compService.findElecCompGroupByType();
	}

}
