package com.cas.sim.tis.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.sim.tis.util.FTPUtils;
import com.cas.util.FileUtil;

import mslinks.ShellLink;
import mslinks.ShellLinkException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class LnkParser2 {
	private Logger LOG = LoggerFactory.getLogger(LnkParser2.class);
	@Autowired
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactory;

	public void load(File file) throws Exception {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				load(child);
			}
		} else {
			File uploadFile = parser(file);
			String filePath = uploadFile.getAbsolutePath();
			String fileName = FileUtil.getFileName(filePath);
			upload(uploadFile, fileName, null);
		}
	}

	private void upload(File uploadFile, String fileName, String keyword) throws Exception {
		String filePath = uploadFile.getAbsolutePath();
		String ext = FileUtil.getFileExt(filePath);
		// 重命名
		String rename = UUID.randomUUID() + "." + ext;
		// 上传文件到FTP
		FTPUtils.connect().cd(ResourceConsts.FTP_RES_PATH).uploadFile( uploadFile, rename).disconnect();
		// 封装资源记录
		int type = ResourceType.parseType(ext);
		Resource resource = new Resource();
		resource.setPath(rename);
		resource.setName(fileName);
		resource.setCreator(1);
		resource.setKeyword(keyword);
		try {
			resource.setType(type);
		} catch (Exception e) {
			LOG.warn("解析文件后缀名出现错误", e);
			throw e;
		}
		// 记录到数据库
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		Integer id = service.addResource(resource);
		if (id != null) {
			LOG.info("{}录入数据库成功！", filePath);
		} else {
			LOG.error("{}录入数据库失败！", filePath);
		}
	}

	private File parser(File file) {
		if (isLnkFile(file)) {
			try {
				ShellLink link = new ShellLink(file.getAbsolutePath());
				String ext = FileUtil.getFileExt(link.getLinkInfo().getLocalBasePath());
				String realPath = link.getWorkingDir() + File.separator + file.getName().replaceAll("lnk", ext);
				return new File(realPath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ShellLinkException e) {
				e.printStackTrace();
			}
			System.err.println(String.format("未找到快捷方式：%s的真实路径", file.getAbsolutePath()));
			return null;
		} else {
			return file;
		}
	}

	public static boolean isLnkFile(File file) {
		BufferedInputStream bis = null;
		InputStream is = null;
		int firstInt = 0;
		try {
			is = new FileInputStream(file);
			bis = new BufferedInputStream(is);
			firstInt = bis.read();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (firstInt == 0x4c) {// 76,L,代表lnk文件格式
			return true;
		}
		return false;
	}

	@Test
	public void test() throws Exception {
		load(new File("C:\\Users\\Administrator\\Desktop\\3D电工仿真软件 - 副本"));
	}

	@Test
	public void excelImportTest() throws Exception {
		Object[][] results = ExcelUtil.readExcelSheet("", "Sheet1");
		for (Object[] result : results) {
			String fileName = String.valueOf(result[0]);
			String path = String.valueOf(result[1]);
			if(StringUtils.isEmpty(fileName)) {
				fileName = FileUtil.getFileName(path);
			}
			String keyword = String.valueOf(result[2]);
			upload(parser(new File(path)), fileName, keyword);
		}
	}
}