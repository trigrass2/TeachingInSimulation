package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.SpringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITest {

	@Test
	public void testGetObject() throws Exception {
		RmiProxyFactoryBean factoryBean = (RmiProxyFactoryBean) SpringUtil.getBean("resourceServiceFactory");
		ResourceService service = (ResourceService) factoryBean.getObject();
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		// 筛选创建人
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
//		criteria.andIn("type", resourceTypes);
////		条件2、关键字搜索
//		if (keyword != null && !"".equals(keyword)) {
//			criteria.andLike("keyword", keyword);
//		}
//		if (creators!=null) {
//			criteria.andIn("creator", creators);
//		}
//		开始分页查询
		PageHelper.startPage(1, 10);
		List<com.cas.sim.tis.entity.Resource> result = service.findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
	}
}
