package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.ExamLibraryRecord;
import com.cas.sim.tis.vo.LibraryRecordInfo;

@Mapper
public interface ExamLibraryRecordMapper extends IMapper<ExamLibraryRecord> {

	List<LibraryRecordInfo> findRecordByPublishId(@Param("pid") int pid, @Param("type") int type);

	float getRecordScoresSumByPublishId(@Param("pid") int pid, @Param("type") int type);

}
