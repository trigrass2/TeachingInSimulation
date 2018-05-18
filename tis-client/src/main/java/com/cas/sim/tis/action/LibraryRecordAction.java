package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.vo.LibraryRecordInfo;

@Component
public class LibraryRecordAction extends BaseAction {
	@Resource
	private LibraryRecordService service;

	/**
	 * 查询考核记录下的学生答题记录
	 * @param pid
	 * @return
	 */
	public List<LibraryRecordInfo> findPublishForTeacher(int pid) {
		return service.findRecordByPublishId(pid);
	}

	public void addRecord(LibraryRecord record, List<LibraryAnswer> answers) {
		service.addRecord(record, answers);
	}

}
