package com.cas.sim.tis.services.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.BrowseHistory;
import com.cas.sim.tis.services.BrowseHistoryService;

@Service("browseHistoryService")
public class BrowseHistoryServiceImpl extends AbstractService<BrowseHistory> implements BrowseHistoryService {

	@Override
	public void addBrowseHistory(BrowseHistory browseHistory) {
		browseHistory.setCreateDate(new Date());
		save(browseHistory);
	}

}
