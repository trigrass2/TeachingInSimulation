package com.cas.sim.tis.view.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.SpringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component
public class ResourceAction {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceAction.class);

	public PageInfo<Resource> findResourcesByCreator(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause,Iterable<Integer> creators) {
		RmiProxyFactoryBean factoryBean = (RmiProxyFactoryBean) SpringUtil.getBean("resourceServiceFactory");
		ResourceService service = (ResourceService) factoryBean.getObject();
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		// 筛选创建人
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
		if (creators!=null) {
			criteria.andIn("creator", creators);
		}
//		开始分页查询
		PageHelper.startPage(pagination, pageSize, orderByClause);
		List<com.cas.sim.tis.entity.Resource> result = service.findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pagination, pageSize, page.getPages());
		return page;
	}
	
	public int countResourceByType(int userId, int type, List<Integer> resourceTypes, String keyword) {
		RmiProxyFactoryBean factoryBean = (RmiProxyFactoryBean) SpringUtil.getBean("resourceServiceFactory");
		ResourceService service = (ResourceService) factoryBean.getObject();
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
		criteria.andEqualTo("creator", userId);
		return service.getTotalBy(condition);
	}
}
