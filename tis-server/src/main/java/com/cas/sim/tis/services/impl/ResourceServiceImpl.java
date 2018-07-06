package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.BrowseHistory;
import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.mapper.BrowseHistoryMapper;
import com.cas.sim.tis.mapper.CollectionMapper;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.utils.OfficeConverter;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

	@javax.annotation.Resource
	private ResourceMapper mapper;

	@javax.annotation.Resource
	private OfficeConverter converter;
	@javax.annotation.Resource
	private CollectionMapper collectionMapper;
	@javax.annotation.Resource
	private BrowseHistoryMapper browseHistoryMapper;

	@Override
	public ResponseEntity findResourceInfoById(RequestEntity entity) {
		ResourceInfo result = mapper.selectResourceInfoByID(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findResourceById(RequestEntity entity) {
		Resource result = mapper.selectByPrimaryKey(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findResourceByIds(RequestEntity entity) {
		Condition condition = new Condition(Resource.class);
		condition.selectProperties("id", "name");
		condition.createCriteria()//
				.andEqualTo("del", 0)//
				.andIn("id", entity.getList("ids", Integer.class));//

		List<Resource> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findResourcesByCreator(RequestEntity entity) {

		Condition condition = new Condition(Resource.class);
		condition.selectProperties("id", "name");
//		条件1、查找用户指定的几种资源类型
		List<Integer> resourceTypes = JSON.parseArray(entity.getString("resourceTypes"), Integer.class);
		if (resourceTypes.size() == 0) {
			return ResponseEntity.success(new ArrayList<Resource>());
		}
		Criteria criteria = condition.createCriteria();
//		获取当前登陆者身份信息
		criteria.andEqualTo("creator", entity.getInt("creator"))//
				.andEqualTo("del", 0)//
				.andIn("type", resourceTypes);
//		条件2、关键字搜索
		String keyword = entity.getString("keyword");
		if (StringUtils.isNotBlank(keyword)) {
			List<String> words = StringUtil.split(keyword, ' ');
			for (String word : words) {
				criteria.andLike("keyword", String.format("%%%s%%", word));
			}
		}

		if (entity.pageNum != -1) {
//			开始分页查询
			PageHelper.startPage(entity.pageNum, entity.pageSize);
		}
		List<Resource> result = mapper.selectByCondition(condition);

		if (entity.pageNum != -1) {
			PageInfo<Resource> page = new PageInfo<Resource>(result);
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findResourcesByBrowseHistory(RequestEntity entity) {
		if (entity.pageNum != -1) {
//			开始分页查询
			PageHelper.startPage(entity.pageNum, entity.pageSize, entity.getString("orderByClause"));
		}

		List<Integer> resourceTypes = JSON.parseArray(entity.getString("resourceTypes"), Integer.class);

		List<Resource> result = mapper.findResourcesByBrowseHistory(resourceTypes, entity.getString("keyword"), entity.getInt("creator"));

		if (entity.pageNum != -1) {
			PageInfo<Resource> page = new PageInfo<Resource>(result);
//			查到的总记录数
//			解释一下：这个page.getTotal()，是所有符合条件的记录数。
//			result.size()：是当前页中的数据量 <= pageSize
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}

		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findResourcesByCollection(RequestEntity entity) {
		if (entity.pageNum == -1) {
//			分页查询
			PageHelper.startPage(entity.pageNum, entity.pageSize, entity.getString("orderByClause"));
		}
		List<Integer> resourceTypes = JSON.parseArray(entity.getString("resourceTypes"), Integer.class);
		List<Resource> result = mapper.findResourcesByCollection(resourceTypes, entity.getString("keyword"), entity.getInt("creator"));
		if (entity.pageNum != -1) {
			PageInfo<Resource> page = new PageInfo<Resource>(result);
//			查到的总记录数
//			解释一下：这个page.getTotal()，是所有符合条件的记录数。
//			result.size()：是当前页中的数据量 <= pageSize
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity countResourceByType(RequestEntity entity) {
		Condition condition = new Condition(Resource.class);
		condition.createCriteria()
				// 获取当前登陆者身份信息
				.andEqualTo("creator", entity.getInt("creator"))
				// 未删除
				.andEqualTo("del", 0)
				// 查找用户指定的资源类型
				.andEqualTo("type", entity.getString("type"));
		String keyword = entity.getString("keyword");
		// 条件2、关键字搜索
		if (StringUtils.isNotBlank(keyword)) {
			Criteria criteria2 = condition.createCriteria();
			List<String> words = StringUtil.split(keyword, ' ');
			for (String word : words) {
				criteria2.orLike("keyword", String.format("%%%s%%", word));
			}
			condition.and(criteria2);
		}
		Object data = mapper.selectCountByCondition(condition);
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity countBrowseResourceByType(RequestEntity entity) {
		int data = mapper.countBrowseResourceByType(entity.getInt("type"), entity.getString("keyword"), entity.getInt("creator"));
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity countCollectionResourceByType(RequestEntity entity) {
		int data = mapper.countCollectionResourceByType(entity.getInt("type"), entity.getString("keyword"), entity.getInt("creator"));
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity countBrowseResourceByTypes(RequestEntity entity) {
		List<String> data = mapper.countBrowseResourceByTypes(//
				entity.getList("types", Integer.class), //
				entity.getString("keyword"), //
				entity.getInt("creator"));
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity countCollectionResourceByTypes(RequestEntity entity) {
		List<String> data = mapper.countCollectionResourceByTypes(//
				entity.getList("types", Integer.class), //
				entity.getString("keyword"), //
				entity.getInt("creator"));
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity countResourceByTypes(RequestEntity entity) {
		List<String> data = mapper.countResourceByTypes(//
				entity.getList("types", Integer.class), //
				entity.getString("keyword"), //
				entity.getInt("creator"));
		return ResponseEntity.success(data);
	}

	@Override
	public ResponseEntity addResources(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);
		List<Resource> resources = entity.getList("resources", Resource.class);
		for (Resource resource : resources) {
			ResourceType type = ResourceType.getResourceType(resource.getType());
			if (type.isConvertable()) {
				converter.resourceConverter(resource.getPath());
			}
		}
		List<Integer> ids = new ArrayList<>(resources.size());
		try {
			mapper.insertList(resources);

			resources.forEach(r -> ids.add(r.getId()));

			transactionManager.commit(status);

		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}

		return ResponseEntity.success(ids);
	}

	@Override
	public ResponseEntity browsed(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			// 增加资源查看数量
			ResourceMapper resourceMapper = (ResourceMapper) mapper;
			resourceMapper.increaseBrowse(entity.getInt("id"));

			Condition condition = new Condition(BrowseHistory.class);
			condition.orderBy("createDate").asc();
			Criteria criteria = condition.createCriteria();
			criteria.andEqualTo("creator", entity.getInt("creator"));
			List<BrowseHistory> histories = browseHistoryMapper.selectByCondition(condition);
			// 查看历史记录是否超过100条，注意这里是物理删除！！！
			if (histories.size() >= 100) {
				BrowseHistory history = histories.get(0);
				browseHistoryMapper.deleteByPrimaryKey(history.getId());
			}
			BrowseHistory history = new BrowseHistory();
			history.setResourceId(entity.getInt("id"));
			history.setCreator(entity.getInt("creator"));
			browseHistoryMapper.insert(history);
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return ResponseEntity.success(null);
	}

	@Override
	public ResponseEntity uncollect(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			// 减少资源收藏数量
			ResourceMapper resourceMapper = (ResourceMapper) mapper;
			resourceMapper.decreaseCollection(entity.getInt("id"));

//			FIXME SQL优化
			// 修改用户收藏记录
			Condition condition = new Condition(Collection.class);
			Criteria criteria = condition.createCriteria();
			criteria.andEqualTo("creator", entity.getInt("creator"));
			criteria.andEqualTo("resourceId", entity.getInt("id"));
			criteria.andEqualTo("del", 0);

			List<Collection> collections = collectionMapper.selectByCondition(condition);
			for (Collection collection : collections) {
				collection.setDel(1);
				collectionMapper.updateByPrimaryKeySelective(collection);
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return ResponseEntity.success(null);
	}

	@Override
	public ResponseEntity collected(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			// 增加资源收藏数量
			mapper.increaseCollection(entity.getInt("id"));

			// 新增用户收藏记录
			Collection collection = new Collection();
			collection.setResourceId(entity.getInt("id"));
			collection.setCreator(entity.getInt("creator"));
			collection.setDel(0);
//			FIXME
			collectionMapper.insert(collection);
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		return ResponseEntity.success(null);
	}

	@Override
	public ResponseEntity updateResource(RequestEntity entity) {
		Resource resource = entity.getObject("resource", Resource.class);
		mapper.updateByPrimaryKeySelective(resource);
		return ResponseEntity.success(null);
	}

}
