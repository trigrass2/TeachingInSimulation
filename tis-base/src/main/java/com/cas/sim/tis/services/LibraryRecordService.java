package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.vo.LibraryRecordInfo;

public interface LibraryRecordService extends BaseService<LibraryRecord> {

	List<LibraryRecordInfo> findRecordByPublishId(int pid);

	void addRecord(LibraryRecord record, List<LibraryAnswer> answers);

}
