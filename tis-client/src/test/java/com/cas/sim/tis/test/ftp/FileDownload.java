package com.cas.sim.tis.test.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.Util;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.util.FTPUtils;

public class FileDownload {

	private FTPUtils getUtil(String user, String password) {
		FTPUtils util = new FTPUtils();

		FTPClient client = new FTPClient();
		util.setFtpClient(client);

		util.setHost("192.168.1.123");
		util.setPort(21);
		util.setUsername(user);
		util.setPassword(password);
		return util;
	}

	@Test
	public void testDirectory() throws Exception {
		FTPUtils util = getUtil("anonymous", "");

		boolean success = util.connect("Test001/Test002");
		Assert.assertFalse(success);
	}

	@Test
	public void testUpload2() throws Exception {
//		管理员可以上传文件
		FTPUtils util = getUtil("admin", "admin");
		boolean result = util.uploadFile("/Test", new File("G:\\jfxrt.jar"), System.currentTimeMillis() + ".jar");
		Assert.assertTrue(result);
	}

	@Test
	public void testUpload() throws Exception {
//		管理员可以上传文件
		FTPUtils util = getUtil("admin", "admin");
		boolean result = util.uploadFile("/assets/Model", new File("G:\\jfxrt.jar"), System.currentTimeMillis() + ".jar");
		Assert.assertTrue(result);

//		匿名用户不允许上传
		util = getUtil("anonymous", "");
		result = util.uploadFile("/assets/Model", new File("G:\\jfxrt.jar"), System.currentTimeMillis() + ".jar");
		Assert.assertFalse(result);
	}

	@Test
	public void testConcurrent() throws Exception {
		FTPUtils util = getUtil("anonymous", "");

		StopWatch w = new StopWatch();
		w.start("开始下载");

		InputStream stream = util.downloadFile("/assets/Model/", "11.j3o");

		FileOutputStream out;
		try {
			File file;
			out = new FileOutputStream(file = new File("model-file-" + 0));
			Util.copyStream(stream, out);
			out.close();

			Assert.assertEquals(3506624, file.length()); // 服务器中11.j3o文件大小是3,506,624

			w.stop();
			System.out.println(w.getTotalTimeMillis());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDownload() throws Exception {
		FTPUtils ftpUtils = getUtil("admin", "admin");
		List<String> pathArray = new ArrayList<>();
		boolean f = ftpUtils.connect("/assets");
		System.out.println(f);
		ftpUtils.getPath("/assets", pathArray);

		pathArray.forEach(System.out::println);
		ftpUtils.download(pathArray, "./");

		ftpUtils.disconnect();
	}
}
