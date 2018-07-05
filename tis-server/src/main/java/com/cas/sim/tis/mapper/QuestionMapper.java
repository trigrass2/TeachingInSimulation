package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Question;

@Mapper
public interface QuestionMapper extends IMapper<Question> {

	List<Question> findQuestionsByLibraryPublish(@Param("pid")int pid, @Param("mostWrong")boolean mostWrong);
	
	List<Question> findQuestionsByPreparationPublish(@Param("pid")int pid, @Param("mostWrong")boolean mostWrong, @Param("questionIds")List<Integer> questionIds);

}
