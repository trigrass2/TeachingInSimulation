package com.cas.sim.tis.socket.message.handler;

import java.util.List;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.socket.message.ResourcesMessage;
import com.jme3.network.Client;

import javafx.application.Platform;

public class ResourcesMessageHandler implements ClientHandler<ResourcesMessage> {

	@Override
	public void execute(Client client, ResourcesMessage m) throws Exception {
//		获取服务器返回的资源列表
		List<Resource> resources = m.getResourceList();
		int sum = m.getRecords();
		int pages = sum % m.getPageSize() == 0 ? sum / m.getPageSize() : (sum / m.getPageSize() + 1);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", resources.size(), m.getPagination(), m.getPageSize(), pages);
//		TODO 交给前台显示
		Platform.runLater(() -> {
			
		});
	}

}
