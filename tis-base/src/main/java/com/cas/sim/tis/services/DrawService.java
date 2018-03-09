package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Draw;

public interface DrawService extends BaseService<Draw> {

	List<Draw> findBySystem();

	List<Draw> findByMine(int id);

}
