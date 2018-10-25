package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.ExamLibraryRecord;
import com.cas.sim.tis.services.ExamLibraryRecordService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;

@Component
public class LibraryRecordAction extends BaseAction {
	@Resource
	private ExamLibraryRecordService service;

	/**
	 * 新增答题记录
	 * @param record 考核/练习记录
	 * @param answers 考核/练习详情记录
	 */
	public void addRecord(ExamLibraryRecord record, List<ExamLibraryAnswer> answers) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("record", record)//
				.set("answers", answers)//
				.build();

		service.addRecord(req);
	}

}
