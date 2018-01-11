package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Student;

public interface StudentService extends BaseService<Student> {

	/**
	 * 根据班级编号查找班内所有学生
	 * @param classId
	 * @return
	 */
	List<Student> findByClass(Integer classId);
}
