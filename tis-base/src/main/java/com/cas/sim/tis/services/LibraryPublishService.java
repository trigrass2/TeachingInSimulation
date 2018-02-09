package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.github.pagehelper.PageInfo;

public interface LibraryPublishService extends BaseService<LibraryPublish> {

	PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator);

	PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator);

}
