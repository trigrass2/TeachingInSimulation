package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.vo.ResourceInfo;

@Mapper
public interface ResourceMapper extends IMapper<Resource> {

	ResourceInfo selectResourceInfoByID(int id);

	void increaseBrowse(Integer id);

	void increaseCollection(Integer id);

	void decreaseCollection(Integer id);

	List<Resource> findResourcesByBrowseHistory(//
			@Param("types") List<Integer> resourceTypes, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	List<Resource> findResourcesByCollection(//
			@Param("types") List<Integer> resourceTypes, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	int countBrowseResourceByType(//
			@Param("type") int type, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	int countCollectionResourceByType(//
			@Param("type") int type, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	List<String> countResourceByTypes(//
			@Param("types") List<Integer> typeList, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	List<String> countBrowseResourceByTypes(//
			@Param("types") List<Integer> typeList, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);

	List<String> countCollectionResourceByTypes(//
			@Param("types") List<Integer> typeList, //
			@Param("keyword") String keyword, //
			@Param("creator") Integer creator);
}
