package com.cas.sim.tis.utils;

import java.io.File;

import javax.annotation.Resource;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.jodconverter.JodConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.LocalOfficeUtils;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.ResourceConsts;

@Component
public class OfficeConverter {
	private static final Logger LOG = LoggerFactory.getLogger(OfficeConverter.class);

	@Value(value = "${open.office.path}")
	private String path;
	@Value(value = "${open.office.port}")
	private Integer port;
	@Resource
	private UserManager userManager;

	public void resourceConverter(String src) {
		try {
			String ftpHome = userManager.getUserByName(userManager.getAdminName()).getHomeDirectory();
			String des = src.substring(0, src.lastIndexOf(".")) + ".pdf";
			converter(ftpHome + File.separator + ResourceConsts.FTP_RES_PATH + src, ftpHome + File.separator + ResourceConsts.FTP_CONVERT_PATH + des);
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

	private void converter(String srcPath, String desPath) {
		// 源文件目录
		File inputFile = new File(srcPath);
		if (!inputFile.exists()) {
			LOG.warn("源文件不存在！");
			return;
		}
		// 输出文件目录
		File outputFile = new File(desPath);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().exists();
		}
		// 连接openoffice服务
		OfficeManager officeManager = LocalOfficeManager.builder().officeHome(path).portNumbers(port).install().build();
		try {
			officeManager.start();
			// 转换文档到pdf
			long time = System.currentTimeMillis();
			JodConverter.convert(inputFile).to(outputFile).execute();
			LOG.info("文件：{}转换PDF：{}完成，用时{}毫秒！", srcPath, desPath, System.currentTimeMillis() - time);
		} catch (OfficeException e) {
			e.printStackTrace();
			LOG.warn("文件：{}转换PDF：{}失败！", srcPath, desPath);
		} finally {
			// 关闭连接
			LocalOfficeUtils.stopQuietly(officeManager);
		}
	}
}
