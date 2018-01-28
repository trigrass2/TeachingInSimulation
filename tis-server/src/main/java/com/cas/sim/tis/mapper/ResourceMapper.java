package com.cas.sim.tis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.vo.ResourceInfo;

@Mapper
public interface ResourceMapper extends IMapper<Resource> {

	ResourceInfo selectResourceInfoByID(int id);

	void increaseBrowse(Integer id);

	void increaseCollection(Integer id);
	
	void decreaseCollection(Integer id);
}
