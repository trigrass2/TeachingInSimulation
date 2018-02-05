package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.Library;
import com.github.pagehelper.PageInfo;

public interface LibraryService extends BaseService<Library> {

	PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type);

	void addLibrary(Library library);

	void modifyLibrary(Library library);

}
