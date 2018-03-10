package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.vo.PreparationInfo;

@Mapper
public interface PreparationResourceMapper extends IMapper<PreparationResource> {

	List<PreparationInfo> findResourcesByPreparationId(Integer pid);

}
