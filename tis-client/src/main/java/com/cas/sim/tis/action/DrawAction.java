package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.services.DrawService;

@Component
public class DrawAction extends BaseAction<DrawService> {

	@Resource
	@Qualifier("drawServiceFactory")
	private RmiProxyFactoryBean drawServiceFactory;

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return drawServiceFactory;
	}

	public List<Draw> getDrawListAll() {
		return getService().findAll();
	}

	public List<Draw> getDrawBySystem() {
		return getService().findByCreatorId(1);
	}

	public List<Draw> getDrawByMine() {
		return getService().findByCreatorId(Session.get(Session.KEY_LOGIN_ID));
	}
}
