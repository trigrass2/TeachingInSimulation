package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.mapper.LibraryAnswerMapper;
import com.cas.sim.tis.services.LibraryAnswerService;

@Service
public class LibraryAnswerServiceImpl extends AbstractService<LibraryAnswer> implements LibraryAnswerService {

	@Override
	public List<LibraryAnswer> findAnswersByPublish(int pid) {
		LibraryAnswerMapper answerMapper = (LibraryAnswerMapper) mapper;
		return answerMapper.findAnswersByPublish(pid);
	}

}
