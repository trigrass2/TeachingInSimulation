package com.cas.sim.tis.services.impl;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.StudentMapper;
import com.cas.sim.tis.services.ServiceException;
import com.cas.sim.tis.services.StudentService;
import com.cas.sim.tis.services.UserService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("studentService")
public class StudentServiceImpl extends AbstractService<Student> implements StudentService, UserService {
	@Override
	public User login(String usercode, String password) {
		StudentMapper studentMapper = (StudentMapper) mapper;
		Condition condition = new Condition(Student.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("code", usercode);
		criteria.andEqualTo("password", password);
		List<Student> students = studentMapper.selectByCondition(condition);
		if (students.size() == 1) {
			return students.get(0);
		} else if (students.size() == 0) {
			throw new ServiceException("用户名或密码错误！");
		} else {
			throw new TooManyResultsException();
		}
	}

	@Override
	public List<Student> findByClass(Integer classId) {
		StudentMapper studentMapper = (StudentMapper) mapper;

		Condition condition = new Condition(Student.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("clsId", classId);
		List<Student> students = null;
		try {
			students = studentMapper.selectByCondition(condition);
			LOG.debug("查询到班级ID[{}]内学生人数：{}", classId, students.size());
		} catch (Exception e) {
			LOG.error("查询班级学生失败！班级ID[{}]", classId);
			students = Collections.emptyList();
		}
		return students;
	}

}
