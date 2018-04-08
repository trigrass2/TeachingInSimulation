package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.TypicalCase;

public interface TypicalCaseService extends BaseService<TypicalCase> {

	List<TypicalCase> findTypicalCasesByCreator(Integer creator);

	int saveRetId(TypicalCase typicalCase);

}
