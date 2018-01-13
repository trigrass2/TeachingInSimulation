package com.cas.sim.tis.services.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ResourceServiceImpl extends AbstractService<Resource> implements ResourceService {

	@javax.annotation.Resource
	private ClassService classService;

	@Override
	public PageInfo<Resource> findResources(User user, int pagination, int pageSize, List<Integer> resourceTypes) throws RemoteException {
		return findResources(user, pagination, pageSize, resourceTypes, null);
	}

	@Override
	public PageInfo<Resource> findResources(User user, int pagination, int pageSize, List<Integer> resourceTypes, String keyWord) throws RemoteException {
//		获取当前登陆者身份信息
		if (user == null) {
			return null;
		}
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resourceTypes);
//		条件2、关键字搜索
		if (keyWord != null && !"".equals(keyWord)) {
			criteria.andLike("keyword", keyWord);
		}
		if (user.getRole() == RoleConst.STUDENT) {
//			通过学生找班级，通过班级找老师
			Student student = (Student) user;
			Class clazz = classService.findById(student.getClsId());
//			条件2、只能查找管理员或自己上传的资源。
//			属于自己老师上传的资源
			criteria.andEqualTo("creatorId", clazz.getTid());
//			条件3、公开的资源
			criteria.orEqualTo("open", true);
		} else if (user.getRole() == RoleConst.TEACHER) {
//			条件2、只能查找管理员或自己上传的资源。
//			属于自己的资源
			criteria.andEqualTo("creatorId", user.getId());
//			条件3、公开的资源
			criteria.orEqualTo("open", true);
		} else if (user.getRole() == RoleConst.ADMIN) {
//			管理员查找所有人的资源
		} else {
			return null;
		}

//		开始分页查询
		PageHelper.startPage(pagination, pageSize);
		List<com.cas.sim.tis.entity.Resource> result = findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		LOG.info("用户{}成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", user.getCode(), result.size(), pagination, pageSize, page.getPages());

		return page;
	}

}
