package com.cas.sim.tis.socket.message.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.socket.message.ResourcesMessage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jme3.network.HostedConnection;
import com.jme3.network.message.DisconnectMessage;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component
public class ResourcesMessageHandler implements ServerHandler<ResourcesMessage> {

	@Resource
	private ResourceService resourceService;

	@Override
	public void execute(HostedConnection source, ResourcesMessage m) throws Exception {
//		获取资源类型
		List<Integer> resTypeList = m.getResTypeList();

//		当前页码
		int pagination = m.getPagination();
//		当前页码
		int pageSize = m.getPageSize();

		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", resTypeList);
//		条件2、只能查找管理员或自己上传的资源。
//		获取当前登陆者身份信息
		User user = source.getAttribute(Session.KEY_LOGIN_USER);
		if (user == null) {
			LOG.error("用户未登陆{}", source.getAddress());
//			终止对于该客户端的服务, 并且踢出去
			source.close(DisconnectMessage.KICK);
			return;
		}
//		属于自己的资源
		criteria.andEqualTo("creatorId", user.getId());
//		或者是公开的资源
		criteria.orEqualTo("open", true);

//		开始分页查询
		PageHelper.startPage(pagination, pageSize);
		List<com.cas.sim.tis.entity.Resource> result = resourceService.findByCondition(condition);
		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(result);
//		查到的总记录数
//		解释一下：这个page.getTotal()，是所有符合条件的记录数。
//		result.size()：是当前页中的数据量 <= pageSize
		m.setRecords((int) page.getTotal());
		LOG.info("用户{}成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", user.getCode(), result.size(), pagination, pageSize, page.getPages());
//		将查找到的资源返回给用户
		m.setResourceList(result);
		source.send(m);
	}

}
