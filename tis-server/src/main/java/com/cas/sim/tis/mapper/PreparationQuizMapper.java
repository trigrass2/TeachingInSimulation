package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.vo.PreparationInfo;
import com.cas.sim.tis.vo.PreparationQuizInfo;

@Mapper
public interface PreparationQuizMapper extends IMapper<PreparationQuiz> {

	List<PreparationQuizInfo> findQuestionQuizsByPreparationId(Integer pid);

	List<PreparationInfo> findOtherQuizsByPreparationId(Integer pid);

}
