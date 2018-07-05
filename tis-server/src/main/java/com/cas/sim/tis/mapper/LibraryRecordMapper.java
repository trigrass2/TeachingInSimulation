package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.vo.LibraryRecordInfo;

@Mapper
public interface LibraryRecordMapper extends IMapper<LibraryRecord> {

	List<LibraryRecordInfo> findRecordByPublishId(@Param("pid") int pid, @Param("type") int type);

	float getRecordScoresSumByPublishId(@Param("pid") int pid, @Param("type") int type);

}
