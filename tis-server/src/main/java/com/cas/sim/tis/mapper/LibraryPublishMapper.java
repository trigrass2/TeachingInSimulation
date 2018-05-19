package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;

@Mapper
public interface LibraryPublishMapper extends IMapper<LibraryPublish> {
	/**
	 * 根据试题考核发布编号查询试题考核发布对象
	 * @param id 试题考核发布编号
	 * @return 试题考核发布对象
	 */
	LibraryPublish findPublishById(int id);

	/**
	 * 根据教师编号获得该用户发布的试题考核
	 * @param creator 教师编号
	 * @return 教师查看的分页集合
	 */
	List<LibraryPublishForTeacher> findPublishForTeacher(@Param("creator") int creator);

	/**
	 * 根据学生编号获得该用户发布的试题练习/参与的试题考核
	 * @param ptype 试题发布类型：PublishType
	 * @param creator 学生编号
	 * @return 学生查看的分页集合
	 */
	List<LibraryPublishForStudent> findPublishForStudent(@Param("ptype") int ptype, @Param("creator") int creator);

	/**
	 * 根据试题考核发布编号获得学生提交状态
	 * @param id 试题考核发布编号
	 * @return 学生提交状态集合
	 */
	List<SubmitInfo> findSubmitStateById(int id);
}
