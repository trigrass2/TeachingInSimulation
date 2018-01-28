package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.BrowseHistory;

public interface BrowseHistoryService extends BaseService<BrowseHistory> {

	void addBrowseHistory(BrowseHistory browseHistory);
}
