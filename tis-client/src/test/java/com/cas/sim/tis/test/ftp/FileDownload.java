package com.cas.sim.tis.test.ftp;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cas.sim.tis.util.FTPUtils;

public class FileDownload {
	@Test
	public void testDirectory() throws Exception {
		FTPUtils.connect().cd("Test001/Test002").disconnect();
	}

	@Test
	public void testUpload2() throws Exception {
//		管理员可以上传文件
		FTPUtils.connect().cd("/Test").uploadFile(new File("G:\\jfxrt.jar"), System.currentTimeMillis() + "中文.jar").disconnect();
	}

	@Test
	public void testDownload() throws Exception {
		List<String> pathArray = new ArrayList<>();
		FTPUtils ftpUtil = FTPUtils.connect().cd("Test");
		ftpUtil.listDirs("/Test", pathArray);
		pathArray.forEach(System.out::println);
		ftpUtil.download(pathArray, "./Test01");
		ftpUtil.disconnect();
//		ftpUtil.cd("/Test").download(pathArray, "./").disconnect();
	}

	@Test
	public void testSync() throws Exception {
		FTPUtils ftpUtils = FTPUtils.connect().cd("/assets");

		List<String> pathArray = new ArrayList<>();
		ftpUtils.listDirs("/assets", pathArray);
		ftpUtils.cd("/assets").download(pathArray, "./");

		ftpUtils.disconnect();
	}
}
