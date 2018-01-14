package com.cas.sim.tis.services.impl;

import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.TeacherMapper;
import com.cas.sim.tis.services.ServiceException;
import com.cas.sim.tis.services.TeacherService;
import com.cas.sim.tis.services.UserService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("teacherService")
public class TeacherServiceImpl extends AbstractService<Teacher> implements TeacherService, UserService {

	@Override
	public User login(String usercode, String password) throws ServiceException, TooManyResultsException {
		Condition condition = new Condition(Student.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("code", usercode);
		criteria.andEqualTo("password", password);
//		criteria.andEqualTo("role", 3);

		TeacherMapper teacherMapper = (TeacherMapper) mapper;
		List<Teacher> teachers = teacherMapper.selectByCondition(condition);
		if (teachers.size() == 1) {
			return teachers.get(0);
		} else if (teachers.size() == 0) {
			throw new ServiceException("用户名或密码错误！");
		} else {
			throw new TooManyResultsException();
		}
	}

}