package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.mapper.ExamPublishMapper;
import com.cas.sim.tis.mapper.ExamLibraryPublishMapper;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.ExamLibraryPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ExamLibraryPublish;
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
public class ExamLibraryPublishServiceImpl implements ExamLibraryPublishService {

	@Resource
	private ExamPublishMapper mapper;
	
	@Resource
	private ExamLibraryPublishMapper libraryPublishMapper;

	@Resource
	private ServerConfig serverConfig;

	@Override
	public ResponseEntity findPublishById(RequestEntity entity) {
		ExamLibraryPublish result = libraryPublishMapper.findPublishById(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findPublishForTeacher(RequestEntity entity) {
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForTeacher> result = libraryPublishMapper.findPublishForTeacher(entity.getInt("creator"));
		PageInfo<LibraryPublishForTeacher> page = new PageInfo<>(result);
		log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(page);
	}

	@Override
	public ResponseEntity findPublishForStudent(RequestEntity entity) {
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<LibraryPublishForStudent> result = libraryPublishMapper.findPublishForStudent(entity.getInt("type"), entity.getInt("creator"));
		PageInfo<LibraryPublishForStudent> page = new PageInfo<>(result);
		log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(page);
	}

	@Override
	public ResponseEntity findSubmitStateById(RequestEntity entity) {
		List<SubmitInfo> result = libraryPublishMapper.findSubmitStateById(entity.getInt("id"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity publishLibraryToClass(RequestEntity entity) {
		ExamLibraryPublish publish = entity.getObject("publish", ExamLibraryPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);

		// 广播开始考核
		ExamMessage message = new ExamMessage();
		message.setPid(publish.getId());
		message.setMessageType(ExamMessage.MESSAGE_TYPE_START);
		message.setExamType(ExamMessage.EXAM_TYPE_LIBRARY);

		// 筛选当前登陆人员
		List<HostedConnection> collection = new ArrayList<>();
		for (HostedConnection hostedConnection : serverConfig.getClients()) {
			if (publish.getClassId().equals(hostedConnection.getAttribute(Session.KEY_LOGIN_CLASSID.name()))) {
				collection.add(hostedConnection);
			}
		}
		Session.set(Session.KEY_LIBRARY_PUBLISH_ID, publish.getId());
		serverConfig.getServer().broadcast(Filters.in(collection), message);

		Session.set(Session.KEY_LIBRARY_PUBLISH_ID, publish.getId());
		return ResponseEntity.success(publish.getId());
	}

	@Override
	public ResponseEntity practiceLibraryByStudent(RequestEntity entity) {
		ExamLibraryPublish publish = entity.getObject("publish", ExamLibraryPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);
		return ResponseEntity.success(publish.getId());
	}

	@Override
	public ResponseEntity updatePublishLibrary(RequestEntity entity) {
		ExamPublish publish = new ExamPublish();
		publish.setId(entity.getInt("id"));
		publish.setState(true);
		// 记录考核发布记录
		mapper.updateByPrimaryKeySelective(publish);

//		将修改后的实体返回
		publish = mapper.selectByPrimaryKey(entity.getInt("id"));
		return ResponseEntity.success(publish);
	}

}
