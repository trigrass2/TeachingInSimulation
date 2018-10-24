package com.cas.sim.tis.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.circuit.util.JaxbUtil;
import com.cas.circuit.vo.Archive;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.entity.BrokenRecord;
import com.cas.sim.tis.services.BrokenRecordService;
import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.FTPUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BrokenRecordAction {

	@Resource
	private BrokenRecordService service;

	public void save(BrokenRecord record, Archive archive) {
//		1、 将存档对象转换为XML文件
		String xmlContent = JaxbUtil.convertToXml(archive, "utf-8");
		log.debug("用户的案例存档内容：{}", xmlContent);
//		将存档内容保存到临时目录中。
		Path path = null;
		try {
			path = Files.createTempFile("tis-archive-", ".xml");
//			标记这个临时文件在系统退出后删除
			path.toFile().deleteOnExit();
//			将存档信息写入临时文件中
			Files.write(path, xmlContent.getBytes());
			log.debug("存档成功:{}", path);
		} catch (IOException e) {
			log.error("存档失败", e);
			throw new ServiceException("存档失败", e);
		}

//		2、将文件上传到FTP服务器
		// 服务器路径目录：/archives/登录者ID
		Integer userId = Session.get(Session.KEY_LOGIN_ID);
		String remotePath = String.format("/archives/broken/%s", String.valueOf(userId));

		try {
			FTPUtils.connect().cd(remotePath).uploadFile(//
					path.toFile(), // 本地要上传的文件
					path.toFile().getName()).disconnect();
		} catch (Exception e) {
			throw new ServiceException("存档上传失败", e);
		} // 文件存储名称
//		3、设置新的存档文件路径
		record.setArchivePath(String.format("%s/%s", remotePath, path.getFileName().toString()));
//		4、将案例的记录保存到数据库中
		record.setCreator(Session.get(Session.KEY_LOGIN_ID));
//		FIXME 修改当前案例的主键ID，原因：前端根据ID判断是新增还是修改。

		RequestEntity req = new RequestEntityBuilder()//
				.set("record", record)//
				.build();

		ResponseEntity resp = service.saveBrokenRecord(req);
//		小细节， 将服务器返回的新的对象属性拷贝到原来的对象中
		ArchiveCase ret = JSON.parseObject(resp.data, ArchiveCase.class);
		try {
			BeanUtils.copyProperty(record, "id", ret.getId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
