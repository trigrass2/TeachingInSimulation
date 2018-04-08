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
import com.cas.sim.tis.services.exception.ServiceException;
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

	public List<TypicalCase> getTypicalCasesByCreator(Integer creator) {
		return getService().findTypicalCasesByCreator(creator);
	}

	public void save(TypicalCase typicalCase, Archive archive) {
//		1、 将存档对象转换为XML文件
		String xmlContent = JaxbUtil.convertToXml(archive, "utf-8");
		LOG.debug("用户的案例存档内容：{}", xmlContent);
//		将存档内容保存到临时目录中。
		Path path = null;
		try {
			path = Files.createTempFile("tis-archive-", ".xml");
			Files.write(path, xmlContent.getBytes());
			LOG.debug("存档成功:{}", path);
		} catch (IOException e) {
			LOG.error("存档失败", e);
			throw new ServiceException("存档失败");
		}

//		2、将案例的记录保存到数据库中
		if (typicalCase.getId() == null) {
//			待新增
			typicalCase.setCreator(Session.get(Session.KEY_LOGIN_ID));
			typicalCase.setArchivePath(MessageFormat.format("/archives/{0}", path.getFileName().toString()));
//			FIXME 修改当前案例的主键ID，原因：前端根据ID判断是新增还是修改。
			int id = getService().saveRetId(typicalCase);
//			！由于是使用RMI，不能实现保存对象就能将ID自动填充的效果，所以保存后，手动设置ID！
			typicalCase.setId(id);
		} else {
			typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
			getService().update(typicalCase);
		}

//		3、将文件上传到FTP服务器
		try {
			SpringUtil.getBean(FTPUtils.class).uploadFile("/archives", path.toFile(), typicalCase.getArchivePath());
		} catch (Exception e) {
			LOG.error("文件{}上传失败！会在下次运行程序时上传", path.toFile());
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
