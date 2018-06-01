package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.mapper.LibraryPublishMapper;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryPublishServiceImpl implements LibraryPublishService {

	@Resource
	private LibraryPublishMapper mapper;

	@Resource
	private ServerConfig serverConfig;

	@Override
	public ResponseEntity findPublishById(RequestEntity entity) {
		LibraryPublish result = mapper.findPublishById(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findPublishForTeacher(RequestEntity entity) {
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForTeacher> result = mapper.findPublishForTeacher(entity.getInt("creator"));
		PageInfo<LibraryPublishForTeacher> page = new PageInfo<>(result);
		log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(page);
	}

	@Override
	public ResponseEntity findPublishForStudent(RequestEntity entity) {
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForStudent> result = mapper.findPublishForStudent(entity.getInt("type"), entity.getInt("creator"));
		PageInfo<LibraryPublishForStudent> page = new PageInfo<>(result);
		log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(page);
	}

	@Override
	public ResponseEntity findSubmitStateById(RequestEntity entity) {
		List<SubmitInfo> result = mapper.findSubmitStateById(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity publishLibraryToClass(RequestEntity entity) {
		LibraryPublish publish = entity.getObject("publish", LibraryPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);

		// 广播开始考核
		ExamMessage message = new ExamMessage();
		message.setPid(publish.getId());
		message.setType(ExamMessage.EXAM_START);

		// 筛选当前登陆人员
		List<HostedConnection> collection = new ArrayList<>();
		for (HostedConnection hostedConnection : serverConfig.getClients()) {
			if (publish.getClassId().equals(hostedConnection.getAttribute(Session.KEY_LOGIN_CLASSID.name()))) {
				collection.add(hostedConnection);
			}
		}
		serverConfig.getServer().broadcast(Filters.in(collection), message);

		return ResponseEntity.success(publish.getId());
	}

	@Override
	public ResponseEntity practiceLibraryByStudent(RequestEntity entity) {
		LibraryPublish publish = entity.getObject("publish", LibraryPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);
		return ResponseEntity.success(publish.getId());
	}

	@Override
	public ResponseEntity updatePublishLibrary(RequestEntity entity) {
		LibraryPublish libraryPublish = new LibraryPublish();
		libraryPublish.setId(entity.getInt("id"));
		libraryPublish.setState(true);
		// 记录考核发布记录
		mapper.updateByPrimaryKeySelective(libraryPublish);

//		将修改后的实体返回
		libraryPublish = mapper.selectByPrimaryKey(entity.getInt("id"));
		return ResponseEntity.success(libraryPublish);
	}

}
