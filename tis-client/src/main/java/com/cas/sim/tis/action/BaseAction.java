package com.cas.sim.tis.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.cas.sim.tis.services.BaseService;

public abstract class BaseAction<T extends BaseService<?>> {
	public static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);

	protected abstract RmiProxyFactoryBean getRmiProxyFactoryBean();

	@SuppressWarnings("unchecked")
	protected T getService() {
		try {
			Object obj = getRmiProxyFactoryBean().getObject();
			LOG.debug("获取Service对象{}", obj);
			return (T) obj;
		} catch (Exception e) {
			String errMsg = "获取Service对象失败！";

			LOG.warn(errMsg, e);
			throw new RuntimeException(errMsg, e);
		}
	};
}
