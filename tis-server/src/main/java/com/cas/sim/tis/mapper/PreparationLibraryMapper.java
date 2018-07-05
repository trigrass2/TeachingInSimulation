package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.PreparationLibrary;

@Mapper
public interface PreparationLibraryMapper extends IMapper<PreparationLibrary> {

	List<PreparationLibrary> findPreparationLibraryByPreparationId(Integer pid);

}
