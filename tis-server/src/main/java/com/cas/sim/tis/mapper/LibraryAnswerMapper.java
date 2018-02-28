package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.LibraryAnswer;

@Mapper
public interface LibraryAnswerMapper extends IMapper<LibraryAnswer> {

	List<LibraryAnswer> findAnswersByPublish(int pid);

	@MapKey("state")
	int statisticsByQuestionId(@Param("pid") int pid, @Param("qid") int qid,@Param("type") int type);

}
