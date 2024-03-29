package com.cas.sim.tis.util.download;

import java.io.IOException;
import java.io.RandomAccessFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 存储文件工具类
 * @author wds
 */
@Slf4j
public class FileUtil {
	private RandomAccessFile file;
	private long startPos; // 文件存储的起始位置

	public FileUtil(String fileName, long startPos) throws IOException {
		file = new RandomAccessFile(fileName, "rw");
		this.startPos = startPos;
		file.seek(startPos);
	}

	public synchronized int write(byte[] data, int start, int len) {
		int res = -1;
		try {
			file.write(data, start, len);
			res = len;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}
}
