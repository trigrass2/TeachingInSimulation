package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.vo.SubmitInfo;
@Mapper
public interface PreparationPublishMapper extends IMapper<PreparationPublish> {

	PreparationPublish findPublishById(Integer id);
	
	List<SubmitInfo> findSubmitStateById(Integer id);

}
