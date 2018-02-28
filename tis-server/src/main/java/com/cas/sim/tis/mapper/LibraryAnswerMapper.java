package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.LibraryAnswer;

@Mapper
public interface LibraryAnswerMapper extends IMapper<LibraryAnswer> {

	List<LibraryAnswer> findAnswersByPublish(int pid);

}
