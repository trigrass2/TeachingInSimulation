package com.cas.sim.tis.view.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
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

	/**
	 * key:元器件型号
	 * @return
	 */
	public Map<String, List<ElecComp>> getElecCompMap() {

		ElecCompService compService = (ElecCompService) elecCompServiceFactory.getObject();

		return compService.findElecCompGroupByType();
	}

	@Nullable public ElecComp getElecComp(String model) {
		ElecCompService compService = (ElecCompService) elecCompServiceFactory.getObject();
		return compService.findElecCompByModel(model);
	}

}
