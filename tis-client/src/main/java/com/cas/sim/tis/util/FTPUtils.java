package com.cas.sim.tis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @功能 FTP服务器工具类
 * @作者 ScOrPiO
 * @创建日期 2016年1月26日
 * @修改人 ScOrPiO
 */
public class FTPUtils {
	private FTPClient ftpClient;

	private FtpAttr attr;

	/**
	 * 连接（配置通用连接属性）至服务器
	 * @param serverName 服务器名称
	 * @param remotePath 当前访问目录
	 * @return <b>true</b>：连接成功 <br/>
	 *         <b>false</b>：连接失败
	 */
	public boolean connect(String remotePath) {
		// 定义返回值
		boolean result = false;
		try {
			// 连接至服务器，端口默认为21时，可直接通过URL连接
			ftpClient.connect(attr.getHost(), attr.getPort());
			// 登录服务器
			ftpClient.login(attr.getUsername(), attr.getPassword());
			// 判断返回码是否合法
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				// 不合法时断开连接
				ftpClient.disconnect();
				// 结束程序
				return false;
			}
			// 切换到对应的文件目录
			result = ftpClient.changeWorkingDirectory(remotePath);
			// 设置文件类型，二进制
			result = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 设置缓冲区大小
			ftpClient.setBufferSize(2048); // 2K
			// 设置字符编码
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
	public boolean uploadFile(String storePath, File file) {
		boolean result = false;
		try (InputStream ins = new FileInputStream(file)){
			if (ins.available() == 0) {
				return result;
			}
			// 连接至服务器
			result = connect(storePath);
			// 判断服务器是否连接成功
			if (result) {
				// 上传文件
				result = ftpClient.storeFile(file.getName(), ins);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
			boolean result = connect(remotePath);
			// 判断服务器是否连接成功
			if (result) {
				// 获取文件输入流
				return ftpClient.retrieveFileStream(fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	public boolean deleteFile(String serverName, String remotePath, String fileName) {
		boolean result = false;
		// 连接至服务器
		result = connect(remotePath);
		// 判断服务器是否连接成功
		if (result) {
			try {
				// 删除文件
				result = ftpClient.deleteFile(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 登出服务器并断开连接
				logout();
			}
		}
		return result;
	}

	/**
	 * 检测FTP服务器文件是否存在
	 * @param serverName 服务器名称
	 * @param remotePath 检测文件存储路径
	 * @param fileName 检测文件存储名称
	 * @return <b>true</b>：文件存在 <br/>
	 *         <b>false</b>：文件不存在
	 */
	public boolean checkFile(String serverName, String remotePath, String fileName) {
		boolean result = false;
		try {
			// 连接至服务器
			result = connect(remotePath);
			// 判断服务器是否连接成功
			if (result) {
				// 默认文件不存在
				result = false;
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
			}
		} catch (IOException e) {
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
