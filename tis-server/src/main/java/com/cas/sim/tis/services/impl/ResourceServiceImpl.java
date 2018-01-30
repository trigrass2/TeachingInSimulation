package com.cas.sim.tis.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ResourceServiceImpl extends AbstractService<Resource> implements ResourceService {

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
	public boolean addResource(Resource resource, boolean convertable) {
		// 判断是否需要转化
		if (convertable) {
			// TODO
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

}
