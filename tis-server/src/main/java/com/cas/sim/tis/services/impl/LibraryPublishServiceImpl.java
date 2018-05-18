package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.mapper.LibraryPublishMapper;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Service
public class LibraryPublishServiceImpl extends AbstractService<LibraryPublish> implements LibraryPublishService {

	@Resource
	private ServerConfig serverConfig;

	@Override
	public LibraryPublish findPublishById(int id) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		return publishMapper.findPublishById(id);
	}

	@Override
	public PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(pageIndex, pageSize);
		List<LibraryPublishForTeacher> result = publishMapper.findPublishForTeacher(creator);
		PageInfo<LibraryPublishForTeacher> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(pageIndex, pageSize);
		List<LibraryPublishForStudent> result = publishMapper.findPublishForStudent(type, creator);
		PageInfo<LibraryPublishForStudent> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public List<SubmitInfo> findSubmitStateById(int id) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		return publishMapper.findSubmitStateById(id);
	}

	@Override
	public Integer publishLibraryToClass(LibraryPublish publish) {
		// 记录考核发布记录
		saveUseGeneratedKeys(publish);

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
		return publish.getId();
	}

	@Override
	public int practiceLibraryByStudent(LibraryPublish publish) {
		// 记录考核发布记录
		saveUseGeneratedKeys(publish);
		return publish.getId();
	}

}
