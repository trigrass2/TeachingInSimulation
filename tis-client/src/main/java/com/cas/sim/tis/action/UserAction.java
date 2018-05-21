package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageInfo;

@Component
public class UserAction extends BaseAction {
	@Resource
	private UserService service;

	/**
	 * 根据用户编号查询用户对象
	 * @param id 用户编号
	 * @return 用户对象
	 */
	public User findUserById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();

		ResponseEntity resp = service.findUserById(req);
		return JSON.parseObject(resp.data, User.class);
	}

	/**
	 * 查询所有教师信息
	 * @return 教师信息集合
	 */
	public List<User> findTeachers() {
		ResponseEntity resp = service.findTeachers(null);
		return JSON.parseArray(resp.data, User.class);
	}

	/**
	 * 分页查询教师信息
	 * @param pageIndex 查询页
	 * @param pageSize 查询条数
	 * @return 教师信息分页集合
	 */
	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.build();
		ResponseEntity resp = service.findTeachers(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<User>>() {});
	}

	/**
	 * 按班级分页查询学生信息
	 * @param pageIndex 查询页
	 * @param pageSize 查询条数
	 * @param classId
	 * @return 学生信息分页集合
	 */
	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.set("classId", classId)//
				.build();
		ResponseEntity resp = service.findStudents(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<User>>() {});
	}

	/**
	 * 批量新增用户信息
	 * @param users 用户集合
	 */
	public void addUsers(List<User> users) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("users", users)//
				.build();
		service.saveUsers(req);
	}

	/**
	 * 修改用户信息
	 * @param user 用户信息
	 */
	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("user", user)//
				.build();
		service.updateUser(req);
	}

	/**
	 * 逻辑删除用户信息
	 * @param user 用户信息
	 */
	public void deleteUserByLogic(int id) {
		User user = new User();
		user.setId(id);
		user.setDel(true);
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("user", user)//
				.build();
		service.updateUser(req);
	}

}
