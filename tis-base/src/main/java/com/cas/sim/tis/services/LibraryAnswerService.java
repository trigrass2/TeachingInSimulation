package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.LibraryAnswer;

public interface LibraryAnswerService extends BaseService<LibraryAnswer> {

	List<LibraryAnswer> findAnswersByPublish(int pid);

}
