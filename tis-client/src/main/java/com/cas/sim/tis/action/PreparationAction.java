package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.services.PreparationService;

@Component
public class PreparationAction extends BaseAction<PreparationService> {
	@Resource
	@Qualifier("preparationServiceFactory")
	private RmiProxyFactoryBean preparationServiceFactory;

	/**
	 * 根据任务编号和创建人获得备课内容
	 * @param cid
	 * @param creator
	 * @return
	 */
	public Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator) {
		PreparationService service = getService();
		return service.findPreparationByTaskIdAndCreator(cid, creator);
	}

	public Preparation addPreparation(Preparation preparation) {
		PreparationService service = getService();
		return service.addPreparation(preparation);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return preparationServiceFactory;
	}
}
