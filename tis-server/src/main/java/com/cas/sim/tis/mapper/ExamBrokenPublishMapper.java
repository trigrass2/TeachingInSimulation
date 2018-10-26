package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.vo.ExamBrokenPublish;
import com.cas.sim.tis.vo.SubmitInfo;

@Mapper
public interface ExamBrokenPublishMapper extends IMapper<ExamBrokenPublish> {

	ExamBrokenPublish findPublishById(Integer id);

	List<SubmitInfo> findSubmitStateById(Integer id);

}
