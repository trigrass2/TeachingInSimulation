package com.cas.sim.tis.test.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.util.FtpAttr;

public class FileDownload {
	@Test
	public void testConcurrent() throws Exception {
		FTPUtils util = new FTPUtils();

		FTPClient client = new FTPClient();
		util.setFtpClient(client);

		FtpAttr attr = new FtpAttr();
		attr.setHost("192.168.1.19");
		attr.setPort(21);
		attr.setUsername("anonymous");
		attr.setPassword("");
		util.setAttr(attr);

//		ExecutorService pool = Executors.newFixedThreadPool(50);
//		List<Future<?>> tasks = new ArrayList<>(50);
		StopWatch w = new StopWatch();
		w.start("开始下载");
//		for (int i = 0; i < 10; i++) {
//			final int index = i;
//			Future<?> task = pool.submit(() -> {
				
				InputStream stream = util.downloadFile("/assets/Model/", "11.j3o");
				FileOutputStream out;
				try {
					out = new FileOutputStream(new File("model-file-" + 0));
					byte b[] = new byte[1024];
					while (stream.read(b) != -1) {
						out.write(b, 0, b.length);
					}
					out.close();
					System.out.println(w.getTotalTimeMillis());
				} catch (IOException e) {
					e.printStackTrace();
				}
//			});
//			tasks.add(task);
//		}
//		for (int i = 0; i < tasks.size(); i++) {
//			Future<?> t = tasks.get(i);
//			t.get();
//		}
		w.stop();
	}
}
