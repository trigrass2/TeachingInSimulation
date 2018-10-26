package com.cas.sim.tis.web.services;

import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.entity.ExamPublish;

public interface ExamPublishService extends IWebService<ExamPublish> {
	ExamPublish findExamingByCreator(int creator);
	
	ExamPublish findExamingByClassId(int classId);
	
	/**
	 * 根据发布编号与学生编号查询是否存在提交记录数
	 * @param publishId 发布编号
	 * @param studentId 学生编号
	 * @param type 考核记录类型
	 * @return 存在为1不存在为0
	 */
	int conutLibraryRecordByPidAndSid(Integer publishId, Integer studentId, LibraryRecordType type);

	int conutBrokenRecordByPidAndSid(Integer publishId, Integer studentId);
}
