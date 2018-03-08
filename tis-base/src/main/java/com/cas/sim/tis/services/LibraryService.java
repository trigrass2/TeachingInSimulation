package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Library;
import com.github.pagehelper.PageInfo;

public interface LibraryService extends BaseService<Library> {

	PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type);

	List<Library> findLibraryByType(int type, String key);

}
