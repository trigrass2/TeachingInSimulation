package com.cas.sim.tis.util.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于分段传输 使用HTTP协议的首部字段实现
 * @author wds
 */
@Slf4j
@Getter
public class FileSplitFetch implements Runnable {

	private String url; // 文件所在url
	
	protected long startPos; // 分段传输的开始位置
	protected long endPos; // 结束位置
	private int threadID; // 线程编号
	
	private boolean downOver = false; // 下载完成标志
	private boolean stop = false; // 当前分段结束标志
	private FileUtil fileUtil = null; // 文件工具

	public FileSplitFetch(String url, long startPos, long endPos, int threadID, String fileName) throws IOException {
		super();
		this.url = url;
		this.startPos = startPos;
		this.endPos = endPos;
		this.threadID = threadID;
		fileUtil = new FileUtil(fileName, startPos);
	}

	@Override
	public void run() {
		while (startPos < endPos && !stop) {
			try {
				URL ourl = new URL(url);
				HttpURLConnection httpConnection = (HttpURLConnection) ourl.openConnection();
				String prop = "bytes=" + startPos + "-";
				httpConnection.setRequestProperty("RANGE", prop); // 设置请求首部字段 RANGE

				log.info(prop);

				InputStream input = httpConnection.getInputStream();
				byte[] b = new byte[1024];
				int bytes = 0;
				while ((bytes = input.read(b)) > 0 && startPos < endPos && !stop) {
					startPos += fileUtil.write(b, 0, bytes);
				}

				log.info("Thread {} is done", threadID);
				downOver = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 打印响应的头部信息
	 * @param conn
	 */
	public void printResponseHeader(HttpURLConnection conn) {
		for (int i = 0;; i++) {
			String fieldsName = conn.getHeaderFieldKey(i);
			if (fieldsName != null) {
				log.info("{}:{}", fieldsName, conn.getHeaderField(fieldsName));
			} else {
				break;
			}
		}
	}

	/**
	 * 停止分段传输
	 */
	public void setSplitTransStop() {
		stop = true;
	}

}
