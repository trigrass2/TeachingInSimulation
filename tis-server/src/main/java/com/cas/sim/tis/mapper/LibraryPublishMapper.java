package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;

@Mapper
public interface LibraryPublishMapper extends IMapper<LibraryPublish> {

	LibraryPublish findPublishById(int id);

	List<LibraryPublishForTeacher> findPublishForTeacher(@Param("creator") int creator);

	List<LibraryPublishForStudent> findPublishForStudent(@Param("ptype") int ptype, @Param("creator") int creator);

	List<SubmitInfo> findSubmitStateById(int id);
}
