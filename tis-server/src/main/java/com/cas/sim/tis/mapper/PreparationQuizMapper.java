package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.vo.PreparationInfo;

@Mapper
public interface PreparationQuizMapper extends IMapper<PreparationQuiz> {

	List<PreparationInfo> findQuizsByPreparationId(Integer pid);

}
