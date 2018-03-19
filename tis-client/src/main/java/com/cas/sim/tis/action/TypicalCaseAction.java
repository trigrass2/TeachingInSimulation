package com.cas.sim.tis.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class TypicalCaseAction extends BaseAction<TypicalCaseService> {
	@Resource
	@Qualifier("typicalCaseServiceFactory")
	private RmiProxyFactoryBean typicalCaseServiceFactory;

	public TypicalCase findTypicalCaseById(Integer id) {
		return getService().findById(id);
	}

	public List<TypicalCase> getTypicalCaseList() {
		return getService().findTypicalCases();
	}

	public void save(TypicalCase typicalCase, Archive archive) {
//		持久化
		String xmlContent = JaxbUtil.convertToXml(archive, "utf-8");
		LOG.info("用户的案例存档内容：{}", xmlContent);
//		将存档内容保存到临时目录中。
		Path path;
		try {
			path = Files.createTempFile("tis-archive-", ".xml");
			Files.write(path, xmlContent.getBytes());
			LOG.info("存档成功:{}", path);
		} catch (IOException e) {
			LOG.error("存档失败", e);
			throw new RuntimeException(e);
		}
		boolean success = false;
		if (typicalCase.getId() == null) {
//			待新增
			success = SpringUtil.getBean(FTPUtils.class).uploadFile("/archives", path.toFile(), path.getFileName().toString());
//			
			typicalCase.setCreator(Session.get(Session.KEY_LOGIN_ID));
			typicalCase.setArchivePath(MessageFormat.format("/archives/{0}", path.getFileName().toString()));

			getService().save(typicalCase);
		} else {
			success = SpringUtil.getBean(FTPUtils.class).uploadFile("/archives", path.toFile(), typicalCase.getArchivePath());
			typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
			getService().update(typicalCase);
		}
		if (success) {
			LOG.info("存档{}已保存到服务器", path);
		}

	}

	public void modify(TypicalCase typicalCase) {
		typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		getService().update(typicalCase);
	}

	public void delete(Integer id) {
		TypicalCase typicalCase = getService().findById(id);
		SpringUtil.getBean(FTPUtils.class).deleteFile("/archives", typicalCase.getArchivePath());
		typicalCase.setDel(true);
		typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		getService().update(typicalCase);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return typicalCaseServiceFactory;
	}

}
