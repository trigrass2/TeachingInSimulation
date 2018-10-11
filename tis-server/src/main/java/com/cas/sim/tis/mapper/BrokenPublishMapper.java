package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.BrokenPublish;
import com.cas.sim.tis.vo.SubmitInfo;

@Mapper
public interface BrokenPublishMapper extends IMapper<BrokenPublish> {

	BrokenPublish findPublishById(Integer id);

	List<SubmitInfo> findSubmitStateById(Integer id);

}
