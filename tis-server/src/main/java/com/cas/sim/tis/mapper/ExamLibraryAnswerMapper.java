package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.ExamLibraryAnswer;

@Mapper
public interface ExamLibraryAnswerMapper extends IMapper<ExamLibraryAnswer> {
	/**
	 * 根据条件查询试题答题结果
	 * @param pid 试题库发布编号
	 * @param onlyWrong 是否只查错题
	 * @return 答题结果集合
	 */
	List<ExamLibraryAnswer> findAnswersByPublish(@Param("pid") int pid, @Param("recordType") int recordType, @Param("onlyWrong") boolean onlyWrong);

	/**
	 * 考核统计指定考核pid中指定试题qid的对应答题状态type的人数
	 * @param pid 试题库发布编号
	 * @param qid 试题编号
	 * @param type 答题状态：AnswerState
	 * @return 返回统计人数
	 */
	@MapKey("state")
	int statisticsByQuestionId(@Param("pid") int pid, @Param("recordType") int recordType, @Param("qid") int qid, @Param("type") int type);

}
