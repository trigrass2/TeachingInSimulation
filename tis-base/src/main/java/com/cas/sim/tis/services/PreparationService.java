package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.Preparation;

public interface PreparationService extends BaseService<Preparation> {

	Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator);

	Preparation addPreparation(Preparation preparation);

}
