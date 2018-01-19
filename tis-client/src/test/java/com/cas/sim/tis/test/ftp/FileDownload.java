package com.cas.sim.tis.test.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.util.FTPUtils;

public class FileDownload {
	@Test
	public void testConcurrent() throws Exception {

		ExecutorService pool = Executors.newFixedThreadPool(50);
		List<Future<?>> tasks = new ArrayList<>(50);
		StopWatch w = new StopWatch();
		w.start("开始下载");
		for (int i = 0; i < 10; i++) {
			final int index = i;
			Future<?> task = pool.submit(() -> {
				FTPUtils u = new FTPUtils();
				
				InputStream stream = u.downloadFile("192.168.1.19", "/assets/Model/", "11.j3o");
				FileOutputStream out;
				try {
					out = new FileOutputStream(new File("model-file-" + index));
					byte b[] = new byte[1024];
					while (stream.read(b) != -1) {
						out.write(b, 0, b.length);
					}
					out.close();
					System.out.println(index + "--- "+w.getTotalTimeMillis());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			tasks.add(task);
		}
		for (int i = 0; i < tasks.size(); i++) {
			Future<?> t = tasks.get(i);
			t.get();
		}
		w.stop();
	}
}
