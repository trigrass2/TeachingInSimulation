package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class LibraryPublishServiceImpl implements LibraryPublishService {
	private static final Logger LOG = LoggerFactory.getLogger(LibraryPublishServiceImpl.class);

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
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForTeacher> result = publishMapper.findPublishForTeacher(entity.getInt("creator"));
		PageInfo<LibraryPublishForTeacher> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findPublishForStudent(RequestEntity entity) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForStudent> result = publishMapper.findPublishForStudent(entity.getInt("type"), entity.getInt("creator"));
		PageInfo<LibraryPublishForStudent> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findSubmitStateById(RequestEntity entity) {
		List<SubmitInfo> result = mapper.findSubmitStateById(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity publishLibraryToClass(RequestEntity entity) {
		LibraryPublish publish = JSON.parseObject(entity.data, LibraryPublish.class);
		// 记录考核发布记录
		mapper.insert(publish);

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
		LibraryPublish publish = JSON.parseObject(entity.data, LibraryPublish.class);
		// 记录考核发布记录
		mapper.insert(publish);
		return ResponseEntity.success(publish.getId());
	}

}
