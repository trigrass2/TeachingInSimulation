package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.utils.OfficeConverter;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ResourceServiceImpl extends AbstractService<Resource> implements ResourceService {

	@javax.annotation.Resource
	private OfficeConverter converter;
	@javax.annotation.Resource
	private CollectionService collectionService;

	@Override
	public ResourceInfo findResourceInfoByID(int id) {
		ResourceMapper resourceMapper = (ResourceMapper) mapper;
		ResourceInfo result = resourceMapper.selectResourceInfoByID(id);
		return result;
	}

	@Override
	public PageInfo<Resource> findResourcesByCreator(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, List<Integer> creators) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(Resource.class);
		// 筛选创建人
//		条件1、查找用户指定的几种资源类型
		if (resourceTypes.size() == 0) {
			return new PageInfo<Resource>(new ArrayList<Resource>());
		} else {
			Criteria criteria = condition.createCriteria();
			criteria.andIn("type", resourceTypes);
		}
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			Criteria criteria = condition.createCriteria();
			List<String> words = StringUtil.split(keyword, ' ');
			for (String word : words) {
				criteria.orLike("keyword", "%" + word + "%");
			}
			condition.and(criteria);
		}
		if (creators != null && creators.size() > 0) {
			Criteria criteria = condition.createCriteria();
			criteria.andIn("creator", creators);
			condition.and(criteria);
		}
//		开始分页查询
		PageHelper.startPage(pagination, pageSize, orderByClause);
		List<Resource> result = findByCondition(condition);
		PageInfo<Resource> page = new PageInfo<Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pagination, pageSize, page.getPages());
		return page;
	}

	@Override
	public int countResourceByType(int type, String keyword, List<Integer> creators) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(Resource.class);
		Criteria criteria1 = condition.createCriteria();
//		条件1、查找用户指定的资源类型
		criteria1.andEqualTo("type", type);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			Criteria criteria2 = condition.createCriteria();
			List<String> words = StringUtil.split(keyword, ' ');
			for (String word : words) {
				criteria2.orLike("keyword", "%" + word + "%");
			}
			condition.and(criteria2);
		}
		if (creators != null && creators.size() > 0) {
			Criteria criteria3 = condition.createCriteria();
			criteria3.andIn("creator", creators);
			condition.and(criteria3);
		}
		return getTotalBy(condition);
	}

	@Override
	public boolean addResource(Resource resource) {
		// 判断是否需要转化
		ResourceType type = ResourceType.getResourceType(resource.getType());
		if (type.isConvertable()) {
			OfficeConverter.resourceConverter(resource.getPath());
		}
		resource.setCreateDate(new Date());
		save(resource);
		return true;
	}

	@Override
	public void browsed(Integer id) {
		ResourceMapper resourceMapper = (ResourceMapper) mapper;
		resourceMapper.increaseBrowse(id);
	}

	@Override
	public void uncollect(Integer id, Integer userId) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean("txManager", DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			// 减少资源收藏数量
			ResourceMapper resourceMapper = (ResourceMapper) mapper;
			resourceMapper.decreaseCollection(id);

			status.flush();
			// 修改用户收藏记录
			Condition condition = new Condition(Collection.class);
			Criteria criteria = condition.createCriteria();
			criteria.andEqualTo("creator", userId);
			criteria.andEqualTo("resourceId", id);
			criteria.andEqualTo("del", 0);

			List<Collection> collections = collectionService.findByCondition(condition);
			for (Collection collection : collections) {
				collection.setDel(1);
				collectionService.update(collection);
			}
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		} finally {
			transactionManager.commit(status);
		}
	}

	@Override
	public void collected(Collection collection) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean("txManager", DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			// 增加资源收藏数量
			ResourceMapper resourceMapper = (ResourceMapper) mapper;
			resourceMapper.increaseCollection(collection.getResourceId());

			status.flush();

			// 新增用户收藏记录
			collection.setCreateDate(new Date());
			collectionService.save(collection);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		} finally {
			transactionManager.commit(status);
		}
	}

	@Override
	public void deteleResource(Integer id) {
		Resource resource = findById(id);
		resource.setDel(true);
		update(resource);
	}

}
