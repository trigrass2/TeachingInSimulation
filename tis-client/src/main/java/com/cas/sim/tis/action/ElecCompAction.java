package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class ElecCompAction extends BaseAction<ElecCompService> {
	@Resource
	private HTTPUtils httpUtils;

	@Resource
	@Qualifier("elecCompServiceFactory")
	private RmiProxyFactoryBean elecCompServiceFactory;

	public List<ElecComp> getElecCompList() {
		return getService().findAll();
	}

	/**
	 * key:元器件型号
	 * @return
	 */
	public Map<String, List<ElecComp>> getElecCompMap() {
		return getService().findElecCompGroupByType();
	}

	@Nullable
	public ElecComp getElecComp(String model) {
		return getService().findElecCompByModel(model);
	}

	public ElecCompDef parse(String cfgPath) {
		return JaxbUtil.converyToJavaBean(httpUtils.getUrl(cfgPath), ElecCompDef.class);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return elecCompServiceFactory;
	}
}
