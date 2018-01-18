package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.StudentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ResourceServiceImpl extends AbstractService<Resource> implements ResourceService {

	@javax.annotation.Resource
	private StudentService studentService;
	@javax.annotation.Resource
	private ClassService classService;

	@Override
	public PageInfo<Resource> findAdminResources(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
		criteria.andEqualTo("creator", 1);
//		开始分页查询
		PageHelper.startPage(pagination, pageSize, orderByClause);
		List<com.cas.sim.tis.entity.Resource> result = findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("用户{}成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", 1, result.size(), pagination, pageSize, page.getPages());

		return page;
	}

	@Override
	public PageInfo<Resource> findTeacherResources(int userId, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause) {
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
//		开始分页查询
		PageHelper.startPage(pagination, pageSize, orderByClause);
		List<com.cas.sim.tis.entity.Resource> result = findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("用户{}成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", userId, result.size(), pagination, pageSize, page.getPages());

		return page;
	}

	@Override
	public PageInfo<Resource> findStudentResources(int userId, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
//		通过学生找班级，通过班级找老师
		Student student = studentService.findById(userId);
		Class clazz = classService.findById(student.getClsId());
//		条件2、只能查找管理员或自己上传的资源。
//		属于自己老师上传的资源
		criteria.andEqualTo("creator", clazz.getTid());
//		开始分页查询
		PageHelper.startPage(pagination, pageSize, orderByClause);
		List<com.cas.sim.tis.entity.Resource> result = findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("用户{}成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", userId, result.size(), pagination, pageSize, page.getPages());

		return page;
	}

	@Override
	public int countAdminResourceByType(int type, List<Integer> resourceTypes, String keyword) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
		criteria.andEqualTo("creator", 1);
		return getTotalBy(condition);
	}

	@Override
	public int countTeacherResourceByType(int userId, int type, List<Integer> resourceTypes, String keyword) {
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
		return getTotalBy(condition);
	}

	@Override
	public int countStudentResourceByType(int userId, int type, List<Integer> resourceTypes, String keyword) {
//		获取当前登陆者身份信息
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyword != null && !"".equals(keyword)) {
			criteria.andLike("keyword", keyword);
		}
//		通过学生找班级，通过班级找老师
		Student student = studentService.findById(userId);
		Class clazz = classService.findById(student.getClsId());
//		条件2、只能查找管理员或自己上传的资源。
//		属于自己老师上传的资源
		criteria.andEqualTo("creator", clazz.getTid());
		return getTotalBy(condition);
	}


}
