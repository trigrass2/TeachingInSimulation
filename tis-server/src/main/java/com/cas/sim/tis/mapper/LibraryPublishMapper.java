package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;

@Mapper
public interface LibraryPublishMapper extends IMapper<LibraryPublish> {

	List<LibraryPublishForTeacher> findPublishForTeacher(@Param("creator") int creator);

	List<LibraryPublishForStudent> findPublishForStudent(@Param("type") int type, @Param("creator") int creator);
}
