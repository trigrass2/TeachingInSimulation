package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.mapper.ClassMapper;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("classService")
public class ClassServiceImpl extends AbstractService<Class> implements ClassService {

	@Resource
	private UserService userService;
	
	@Override
	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		ClassMapper classMapper = (ClassMapper) mapper;
		PageHelper.startPage(pageIndex, pageSize);
		List<Class> result = classMapper.findClasses();
		PageInfo<Class> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", page.getTotal(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public List<Class> findClassesByTeacher(int teacherId) {
		Condition condition = new Condition(Class.class);
		condition.orderBy("createDate").desc();
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("teacherId", teacherId);
		criteria.andEqualTo("del", 0);
		return findByCondition(condition);
	}

	@Override
	public void saveClasses(List<ClassInfo> infos, Integer creator) {
		ClassMapper classMapper = (ClassMapper) mapper;
		classMapper.insertClasses(infos, creator);

	}

	@Override
	public void modifyClass(Class clazz) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			update(clazz);
			
			userService.updateTeacherIdByClassId(clazz);

			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

}
