package com.cas.sim.tis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @功能 FTP服务器工具类
 * @作者 ScOrPiO
 * @创建日期 2016年1月26日
 * @修改人 ScOrPiO
 */
public class FTPUtils {
	private static final Logger LOG = LoggerFactory.getLogger(FTPUtils.class);
	private FTPClient ftpClient;

	private FtpAttr attr;

	/**
	 * 连接（配置通用连接属性）至服务器
	 * @param serverName 服务器名称
	 * @param remotePath 当前访问目录
	 * @return <b>true</b>：连接成功 <br/>
	 *         <b>false</b>：连接失败
	 * @throws Exception
	 */
	public boolean connect(String remotePath) throws Exception {
		// 连接至服务器，端口默认为21时，可直接通过URL连接
		try {
			ftpClient.connect(attr.getHost(), attr.getPort());
		} catch (Exception e) {
			LOG.error("无法连接到FTP服务器{}:{}", attr.getHost(), attr.getPort());
			throw e;
		}
		// 登录服务器
		boolean login = ftpClient.login(attr.getUsername(), attr.getPassword());
		if (!login) {
			LOG.error("无法登录到FTP服务器{},{}", attr.getUsername(), attr.getPassword());
			return false;
		}
		// 判断返回码是否合法
		if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			// 不合法时断开连接
			LOG.warn("不合法的连接，服务器返回码{}", ftpClient.getReplyCode());
			disconnect();
			// 结束程序
			return false;
		}
		// 切换到对应的文件目录
		boolean exist = ftpClient.changeWorkingDirectory(remotePath);
		if (!exist) {
			LOG.warn("目录不存在{}, 准备创建", remotePath);
			ftpClient.cwd("/");

			boolean created = mkDir(remotePath);
			if (!created) {
				LOG.warn("创建目录【{}】失败", remotePath);
			}
		}
		// 设置文件类型，二进制
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
		}
		// 设置缓冲区大小
		ftpClient.setBufferSize(2048); // 2K
		// 设置字符编码
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.enterLocalPassiveMode();
		return true;
	}

	/**
	 * 列出当前目录下的所有文件夹
	 * @return
	 */
	public List<String> listDirs() {
		List<String> dirs = new ArrayList<>();
		try {
			FTPFile[] firs = ftpClient.listDirectories();
			for (FTPFile ftpFile : firs) {
				dirs.add(ftpFile.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dirs;
	}

	public boolean mkDir(String directory) {
		LOG.info("准备创建目录:{}", directory);
//		格式化路径
		directory = directory.replaceAll("//", "/");
		if (directory.startsWith("/")) {
			directory = directory.substring(1);
		}
		if (directory.endsWith("/")) {
			directory = directory.substring(0, directory.length() - 1);
		}
//		从当前目录逐级创建文件夹
		try {
			String[] str = directory.split("/");
			String parent = "";
			for (int i = 0; i < str.length; i++) {

				String dirName = new String(str[i].getBytes("UTF-8"), "ISO-8859-1");

				if (!isExistDir(str[i])) {
//					目录不存在，则创建
					boolean success = ftpClient.makeDirectory(dirName);
					if (success) {
						LOG.info("成功创建目录【{}】", str[i]);
					} else {
						LOG.warn("创建目录【{}】失败", str[i]);
					}
				} else {
					LOG.info("目录【{}】已存在", str[i]);
				}
//				进入子目录中
				boolean enter = ftpClient.changeWorkingDirectory(dirName);
				if (!enter) {
					LOG.warn("进入目录【{}】失败", str[i]);
					throw new RuntimeException(String.format("进入目录【{}】失败", str[i]));
				}
				parent += "../";
			}
//			创建完成后回到之前的目录中
			if (str.length >= 1) {
				LOG.info("创建完成后回到之前的目录中");
				ftpClient.changeWorkingDirectory(parent);
			}
			return true;
		} catch (IOException e) {
			LOG.warn("创建目录时出现了一个错误", e);
			return false;
		}
	}

	/**
	 * 当前目录下是否存在指定的文件夹名称
	 */
	public boolean isExistDir(String dirName) {
		List<String> list = listDirs();
		if (list.contains(dirName)) {
			return true;
		}
		return false;
	}

	/**
	 * 上传文件至FTP服务器
	 * @param serverName 服务器名称
	 * @param storePath 上传文件存储路径
	 * @param fileName 上传文件存储名称
	 * @param is 上传文件输入流
	 * @return <b>true</b>：上传成功 <br/>
	 *         <b>false</b>：上传失败
	 */
	public boolean uploadFile(String storePath, File file, String storedName) {
		LOG.info("准备上传文件【{}】至【{}】目录中，并且命名为【{}】", file.getPath(), storePath, storedName);
		boolean result = false;
		try (InputStream ins = new FileInputStream(file)) {
			if (ins.available() == 0) {
				return false;
			}
			// 连接至服务器
			connect(storePath);
			// 上传文件
			result = ftpClient.storeFile(storedName, ins);
			if (result) {
				LOG.info("文件上传成功");
			} else {
				LOG.warn("文件上传失败");
			}
		} catch (Exception e) {
			LOG.warn("上传文件时出现了一个错误", e);
		} finally {
			// 登出服务器并断开连接
			logout();
		}
		return result;
	}

	/**
	 * 下载FTP服务器文件至本地<br/>
	 * 操作完成后需调用logout方法与服务器断开连接
	 * @param serverName 服务器名称
	 * @param remotePath 下载文件存储路径
	 * @param fileName 下载文件存储名称
	 * @return <b>InputStream</b>：文件输入流
	 */
	public InputStream downloadFile(String remotePath, String fileName) {
		try {
			// 连接至服务器
			connect(remotePath);
			// 获取文件输入流
			return ftpClient.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (Exception e) {
			LOG.warn("下载文件时出现了一个错误", e);
		}
		return null;
	}

	/**
	 * 下载FTP服务器文件至本地<br/>
	 * 操作完成后需调用logout方法与服务器断开连接
	 * @param serverName 服务器名称
	 * @param remotePath 下载文件存储路径
	 * @param fileName 下载文件存储名称
	 * @return <b>InputStream</b>：文件输入流
	 */
	public InputStream retrieveFile(String remotePath, String fileName) {
		try {
			// 连接至服务器
			connect(remotePath);
			// 获取文件输入流
			boolean result = ftpClient.retrieveFile(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"), new FileOutputStream("C://name"));
			if (result) {
				LOG.info("下载文件{}成功", fileName);
			} else {
				LOG.warn("下载文件{}失败", fileName);
			}
		} catch (Exception e) {
			LOG.warn("下载文件时出现了一个错误", e);
		}
		return null;
	}

	/**
	 * 删除FTP服务器文件
	 * @param serverName 服务器名称
	 * @param remotePath 当前访问目录
	 * @param fileName 文件存储名称
	 * @return <b>true</b>：删除成功 <br/>
	 *         <b>false</b>：删除失败
	 */
	public boolean deleteFile(String remotePath, String fileName) {
		try {

			// 连接至服务器
			connect(remotePath);
			boolean result = ftpClient.deleteFile(fileName);
			if (result) {
				LOG.info("删除文件{}成功", fileName);
			} else {
				LOG.warn("删除文件{}失败", fileName);
			}
		} catch (Exception e) {
			LOG.warn("删除文件时出现了一个错误", e);
		} finally {
			// 登出服务器并断开连接
			logout();
		}
		return false;
	}

	/**
	 * 检测FTP服务器文件是否存在
	 * @param serverName 服务器名称
	 * @param remotePath 检测文件存储路径
	 * @param fileName 检测文件存储名称
	 * @return <b>true</b>：文件存在 <br/>
	 *         <b>false</b>：文件不存在
	 */
	public boolean checkFile(String remotePath, String fileName) {
		boolean result = false;
		try {
			// 连接至服务器
			connect(remotePath);
			// 获取文件操作目录下所有文件名称
			String[] remoteNames = ftpClient.listNames();
			// 返回值为空
			if (remoteNames == null) {
				return false;
			}
			// 循环比对文件名称，判断是否含有当前要下载的文件名
			for (String remoteName : remoteNames) {
				if (fileName.equals(remoteName)) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 登出服务器并断开连接
			logout();
		}
		return result;
	}

	/**
	 * 登出服务器并断开连接
	 * @param ftp FTPClient对象实例
	 * @return <b>true</b>：操作成功 <br/>
	 *         <b>false</b>：操作失败
	 */
	public boolean logout() {
		boolean result = false;
		if (null != ftpClient) {
			try {
				// 登出服务器
				result = ftpClient.logout();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}
		}
		return result;
	}

	public void disconnect() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public void setAttr(FtpAttr attr) {
		this.attr = attr;
	}

}
