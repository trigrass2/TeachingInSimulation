package com.cas.sim.tis.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LnkParser {

	public LnkParser(File f) throws Exception {
		parse(f);
	}

	private boolean is_dir;

	public boolean isDirectory() {
		return is_dir;
	}

	private String real_file;

	public String getRealFilename() {
		return real_file;
	}

	public void parse(File f) throws Exception {

		// read the entire file into a byte buffer
		byte[] link = parseLink(f);

		if (!isLnkFile(link)) {
			return;
		}

		// get the flags byte
		byte flags = link[0x14];

		// get the file attributes byte
		byte fileatts = link[0x18];
		byte is_dir_mask = (byte) 0x10;
		// 0000 xxxx xxxx xxxx & 0000 0000 0001 0000(如果第4为1表示为文件夹)
		if ((fileatts & is_dir_mask) > 0) {
			is_dir = true;
		} else {
			is_dir = false;
		}

		// if the shell settings are present, skip them
		int shell_len = 0;
		// 0000 0000 xxxx xxxx & 0000 0000 0000 0001(判断是否存在shell段)
		if ((flags & 0x1) > 0) {
			// the plus 2 accounts for the length marker itself
			shell_len = bytes2short(link, 0x4c) + 2;
		}

		// get to the file settings
		int file_start = 0x4c + shell_len;

		// get the local volume and local system values
		int local_sys_off = link[file_start + 0x10] + file_start;
		real_file = getNullDelimitedString(link, local_sys_off);
		p("real filename = " + real_file);
	}

	private boolean isLnkFile(byte[] link) {
		if (link[0x00] == 0x4c) {// 76,L,代表lnk文件格式
			return true;
		}
		return false;
	}

	private byte[] parseLink(File f) throws FileNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(f);
		byte[] link = new byte[(int) f.length()];
		fin.read(link); // 读取文件中的内容到link[]数组
		fin.close();
		return link;
	}

	/**
	 * 获得从偏移位置off到以‘0’为结尾分割字符串
	 * @param bytes 源数组
	 * @param off 偏移位置
	 * @return 字符串
	 */
	static String getNullDelimitedString(byte[] bytes, int off) {
		int len = 0;
		// count bytes until the null character (0)
		while (true) {
			if (bytes[off + len] == 0) {
				break;
			}
			len++;
		}
		byte[] results = new byte[len];
		for (int i = off, j = 0; i < off + len; i++, j++) {
			results[j] = bytes[i];
		}
		try {
			return new String(bytes, off, len, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将两个字节转换为short<br>
	 * 注意，因为仅限英特尔操作系统，所以这是小端字节<br>
	 */
	static int bytes2short(byte[] bytes, int off) {
		return bytes[off] | (bytes[off + 1] << 8);
	}

	public static void p(String str) {
		System.out.println(str);
	}

	public static void main(String[] args) {
		try {
			new LnkParser(new File("C:\\Users\\Administrator\\Desktop\\3D电工仿真软件 - 副本\\ZHE 是一个WPS 文件.lnk"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}