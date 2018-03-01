package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;
import com.github.pagehelper.PageInfo;

public interface LibraryPublishService extends BaseService<LibraryPublish> {

	LibraryPublish findPublishById(int id);

	PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator);

	PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator);

	List<SubmitInfo> findSubmitStateById(int id);

	Integer publishLibraryToClass(LibraryPublish publish);

	int practiceLibraryByStudent(LibraryPublish publish);
}
