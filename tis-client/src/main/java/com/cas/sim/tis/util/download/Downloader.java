package com.cas.sim.tis.util.download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Downloader {
	private static final int NOACCESS = -2; // 文件不可访问

	private SiteInfo siteInfo; // 文件信息
	private long[] startPos; // 开始位置
	private long[] endPos; // 结束位置
	private FileSplitFetch[] fileSplitFetchs; // 多线程分段传输的线程集合
	private long fileLen; // 文件长度
	private boolean firstDown = true; // 是否第一次下载文件
	private boolean stop = false; // 停止标志
	private File infoFile; // 保存文件信息的临时文件

	public Downloader(SiteInfo siteInfo) {
		this.siteInfo = siteInfo;
		infoFile = new File(System.getProperty("java.io.tmpdir") + File.separator + siteInfo.getFileName() + ".tmp");
		if (infoFile.exists()) {
			firstDown = false;
			readInfo();
		} else {
			startPos = new long[2];
			endPos = new long[2];
		}
	}

	/**
	 * 开始下载文件 1. 获取文件长度 2. 分割文件 3. 实例化分段下载子线程 4. 启动子线程 5. 等待子线程的返回
	 * @throws IOException
	 */
	public void execute(Consumer<Integer> progress) {
		if (firstDown) {
			fileLen = getFileSize();
			if (fileLen == -1) {
				return;
			} else if (fileLen == -2) {
				return;
			} else {
				// 设置每次分段下载的开始位置
				for (int i = 0; i < startPos.length; i++) {
					startPos[i] = i * (fileLen / startPos.length);
				}

				// 设置每次分段下载的结束位置
				for (int i = 0; i < endPos.length - 1; i++) {
					endPos[i] = startPos[i + 1];
				}
				endPos[endPos.length - 1] = fileLen;
			}
		}

		// 启动分段下载子线程
		try {
			fileSplitFetchs = new FileSplitFetch[startPos.length];
			for (int i = 0; i < startPos.length; i++) {
				System.out.println(startPos[i] + " " + endPos[i]);
				fileSplitFetchs[i] = new FileSplitFetch(siteInfo.getUrl(), startPos[i], endPos[i], i, siteInfo.getFilePath() + File.separator + siteInfo.getFileName());
				log.info("Threa " + i + ", start= " + startPos[i] + ",  end= " + endPos[i]);
				new Thread(fileSplitFetchs[i]).start();
			}

			// 保存文件下载信息
			saveInfo();
			// 循环判断所有文件是否下载完毕
			boolean breakWhile = false;
			while (!stop) {
				Thread.sleep(1000);
				breakWhile = true;

				double down = 0;
				for (int i = 0; i < startPos.length; i++) {
					down += fileSplitFetchs[i].startPos - startPos[i];

					if (!fileSplitFetchs[i].isDownOver()) {
						breakWhile = false; // 还存在未下载完成的线程
//						break;
					}
				}
				if (progress != null) {
					progress.accept((int) ((down / fileLen) * 100));
				}
				if (breakWhile) break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存文件下载信息
	 */
	private void saveInfo() {
		try {
			DataOutputStream output = new DataOutputStream(new FileOutputStream(infoFile));
			output.writeInt(startPos.length);
			for (int i = 0; i < startPos.length; i++) {
				output.writeLong(fileSplitFetchs[i].startPos);
				output.writeLong(fileSplitFetchs[i].endPos);
			}
			output.writeLong(fileLen);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件下载保存的信息
	 */
	private void readInfo() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(infoFile));
			int count = input.readInt();
			startPos = new long[count];
			endPos = new long[count];
			for (int i = 0; i < count; i++) {
				startPos[i] = input.readLong();
				endPos[i] = input.readLong();
			}
			fileLen = input.readLong();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件的大小
	 * @return
	 */
	private long getFileSize() {
		int len = -1;
		try {
			URL url = new URL(siteInfo.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "custom");

			int respCode = connection.getResponseCode();
			if (respCode >= 400) {
				log.error("Error Code : {}", respCode);
				return NOACCESS; // 代表文件不可访问
			}

			String header = null;
			for (int i = 1;; i++) {
				header = connection.getHeaderFieldKey(i);
				if (header != null) {
					if ("Content-Length".equals(header)) {
						len = Integer.parseInt(connection.getHeaderField(header));
						break;
					}
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return len;
	}

	/**
	 * 停止下载
	 */
	public void setStop() {
		stop = true;
		for (int i = 0; i < startPos.length; i++) {
			fileSplitFetchs[i].setSplitTransStop();
		}
	}
}
