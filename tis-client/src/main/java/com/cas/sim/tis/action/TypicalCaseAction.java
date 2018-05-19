package com.cas.sim.tis.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class TypicalCaseAction extends BaseAction {
	@Resource
	private TypicalCaseService service;

	public TypicalCase findTypicalCaseById(Integer id) {
		RequestEntity entity = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findTypicalCasesById(entity);
		return JSON.parseObject(resp.data, TypicalCase.class);
	}

	public List<TypicalCase> getTypicalCasesByCreator(Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findTypicalCasesByCreatorId(req);
		return JSON.parseArray(resp.data, TypicalCase.class);
	}

	public void save(TypicalCase typicalCase, Archive archive) {
//		1、 将存档对象转换为XML文件
		String xmlContent = JaxbUtil.convertToXml(archive, "utf-8");
		LOG.debug("用户的案例存档内容：{}", xmlContent);
//		将存档内容保存到临时目录中。
		Path path = null;
		try {
			path = Files.createTempFile("tis-archive-", ".xml");
//			标记这个临时文件在系统退出后删除
			path.toFile().deleteOnExit();
//			将存档信息写入临时文件中
			Files.write(path, xmlContent.getBytes());
			LOG.debug("存档成功:{}", path);
		} catch (IOException e) {
			LOG.error("存档失败", e);
			throw new ServiceException("存档失败", e);
		}

//		2、将文件上传到FTP服务器
		// 服务器路径目录：/archives/登录者ID
		Integer userId = Session.get(Session.KEY_LOGIN_ID);
		String remotePath = String.format("/archives/%s", String.valueOf(userId));

		try {
			FTPUtils.connect().cd(remotePath).uploadFile(//
					path.toFile(), // 本地要上传的文件
					path.toFile().getName()).disconnect();
		} catch (Exception e) {
			throw new ServiceException("存档上传失败", e);
		} // 文件存储名称
//		2.5、删除原有的存档
		LOG.debug("文件{}已上传", path.toFile());
		if (typicalCase.getArchivePath() != null) {
			try {
				FTPUtils.connect().cd(remotePath).deleteFile(typicalCase.getArchivePath()).disconnect();
			} catch (Exception e) {
				LOG.warn("删除文件失败", e);
			}
		}
//		3、设置新的存档文件路径
		typicalCase.setArchivePath(String.format("%s/%s", remotePath, path.getFileName().toString()));

//		4、将案例的记录保存到数据库中
		if (typicalCase.getId() == null) {
			typicalCase.setCreator(Session.get(Session.KEY_LOGIN_ID));
//			FIXME 修改当前案例的主键ID，原因：前端根据ID判断是新增还是修改。

			RequestEntity req = new RequestEntityBuilder()//
					.set("typicalCase", typicalCase)//
					.build();

			ResponseEntity resp = service.saveTypicalCase(req);
//			小细节， 将服务器返回的新的对象属性拷贝到原来的对象中
			TypicalCase ret = JSON.parseObject(resp.data, TypicalCase.class);
			try {
				BeanUtils.copyProperties(typicalCase, ret);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
			RequestEntity req = new RequestEntity()//
					.set("typicalCase", typicalCase)//
					.end();
			service.updateTypicalCase(req);
		}
	}

//	public void modify(TypicalCase typicalCase) {
//		typicalCase.setUpdater(Session.get(Session.KEY_LOGIN_ID));
//		service.update(typicalCase);
//	}

	public void delete(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		service.deleteTypicalCaseById(req);
//		不用删除服务器中的文件
//		try {
//			FTPUtils.connect().cd("/archives").deleteFile(typicalCase.getArchivePath()).disconnect();
//		} catch (Exception e) {
//			LOG.warn("删除文件失败", e);
//		}
	}

}
