package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.vo.ExamPreparationPublish;
import com.cas.sim.tis.vo.SubmitInfo;
@Mapper
public interface ExamPreparationPublishMapper extends IMapper<ExamPreparationPublish> {

	ExamPreparationPublish findPublishById(Integer id);
	
	List<SubmitInfo> findSubmitStateById(Integer id);

}
