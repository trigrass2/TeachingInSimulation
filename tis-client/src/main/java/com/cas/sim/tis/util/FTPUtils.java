package com.cas.sim.tis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.jetbrains.annotations.NotNull;

import lombok.extern.slf4j.Slf4j;

/**
 * @功能 FTP服务器工具类
 * @作者 ScOrPiO
 * @创建日期 2016年1月26日
 * @修改人 ScOrPiO
 */
@Slf4j
public class FTPUtils {
	private static final String FTP_ENCODE = "ISO-8859-1";
	private FTPClient ftpClient;

//	当前目录
	private String remotePath;

	public static class FtpFile {
		public String name;
		public File file;
	}

	/**
	 * 连接（配置通用连接属性）至服务器
	 * @param remotePath 服务器名称
	 * @return <b>true</b>：连接成功 <br/>
	 *         <b>false</b>：连接失败
	 * @throws Exception
	 */
	public static FTPUtils connect() {
		FTPUtils util = new FTPUtils();

		FTPClient ftpClient = new FTPClient();
		util.setFtpClient(ftpClient);

		String serverIp = AppPropertiesUtil.getStringValue("server.ftp.address");
		Integer serverPort = AppPropertiesUtil.getIntValue("server.ftp.port");

//		util.setHost(cfg.getFtpAddress());
//		util.setPort(cfg.getFtpPort());
//		util.setUsername("admin");
//		util.setPassword("admin");

		// 连接至服务器，端口默认为21时，可直接通过URL连接
		try {
			ftpClient.connect(serverIp, serverPort);
			// 登录服务器
			ftpClient.login("admin", "admin");
			ftpClient.enterLocalPassiveMode();

			// 设置文件类型，二进制
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// 设置缓冲区大小
			ftpClient.setBufferSize(2048); // 2K
			// 设置字符编码
			// 下面两行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件
			ftpClient.setControlEncoding(FTP_ENCODE);// 注意编码格式
			new FTPClientConfig(FTPClientConfig.SYST_UNIX).setServerLanguageCode("zh");// 中文

			// 判断返回码是否合法
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				// 不合法时断开连接
				String errMsg = String.format("不合法的连接，服务器返回码%s", ftpClient.getReplyCode());
				log.warn(errMsg);
				// 结束程序
				util.disconnect();
				throw new RuntimeException(errMsg);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return util;
	}

	public FTPUtils cd(String pathname) throws IOException {
		boolean result = ftpClient.changeWorkingDirectory(pathname);
		if (!result) {
			log.warn("进入目录{}失败", pathname);
			mkDir(pathname);
			result = ftpClient.changeWorkingDirectory(pathname);
		}
		remotePath = ftpClient.printWorkingDirectory();
		return this;
	}

	/**
	 * 列出当前目录下的所有文件夹
	 * @return
	 */
	@NotNull public List<String> listDirs() {
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

	/**
	 * 列出指定目录下，递归寻找所有文件夹
	 * @param path
	 * @param pathArray
	 * @throws IOException
	 */
	public void listDirs(String path, List<String> pathArray) throws IOException {
		ftpClient.changeWorkingDirectory(path);// 改变当前路径
		pathArray.add(path);

		FTPFile[] files = ftpClient.listFiles();
		for (FTPFile ftpFile : files) {
			if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {
				continue;
			}
			if (ftpFile.isDirectory()) {// 如果是目录，则递归调用，查找里面所有文件
				path += "/" + ftpFile.getName();
				ftpClient.changeWorkingDirectory(path);// 改变当前路径
				listDirs(path, pathArray);// 递归调用
				path = path.substring(0, path.lastIndexOf("/"));// 避免对之后的同目录下的路径构造作出干扰，
			}
		}
	}

	public boolean mkDir(String directory) {
		log.info("准备创建目录:{}", directory);
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

				String dirName = convertToFtpEncode(str[i]);

				if (!isExistDir(str[i])) {
//					目录不存在，则创建
					boolean success = ftpClient.makeDirectory(dirName);
					if (success) {
						log.info("成功创建目录【{}】", str[i]);
					} else {
						log.warn("创建目录【{}】失败", str[i]);
						throw new RuntimeException(String.format("创建目录【%1$s】失败", str[i]));
					}
				} else {
					log.info("目录【{}】已存在", str[i]);
				}
//				进入子目录中
				boolean enter = ftpClient.changeWorkingDirectory(dirName);
				if (enter) {
					parent += "../";
				} else {
					log.warn("进入目录【{}】失败", str[i]);
					throw new RuntimeException(String.format("进入目录【%1$s】失败", str[i]));
				}
			}
//			创建完成后回到之前的目录中
			if (str.length >= 1) {
				log.info("创建完成后回到之前的目录中");
				ftpClient.changeWorkingDirectory(parent);
			}
			return true;
		} catch (IOException e) {
			log.warn("创建目录时出现了一个错误", e);
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
	 * 上传单个文件至FTP服务器
	 * @param storePath 文件存储路径
	 * @param file 上传文件
	 * @param storedName 文件存储名称
	 * @throws Exception
	 */
	public FTPUtils uploadFile(File file, String storedName) throws Exception {
		try (InputStream ins = new FileInputStream(file)) {
			// 上传文件
			boolean result = ftpClient.storeFile(convertToFtpEncode(storedName), ins);
			if (result) {
				log.info("本地文件【{}】已上传至服务器目录【{}】中，并且命名为【{}】", file.getPath(), remotePath, storedName);
			} else {
				log.error("文件【{}】上传失败", file.getPath());
			}
		}
		return this;
	}

	/**
	 * 批量上传文件到当前目录中
	 * @param fileList
	 * @return
	 */
	public FTPUtils uploadFileBatch(@Nonnull List<FtpFile> fileList) {
//		连接FTP服务器
		fileList.forEach(file -> {
			try {
				uploadFile(file.file, file.name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return this;
	}

	/**
	 * 下载给定目录下的所有文件（包括子文件夹内的文件）
	 * @param pathArray
	 * @param localRootPath
	 * @return
	 * @throws IOException
	 */
	public FTPUtils download(List<String> pathArray, String localRootPath) throws IOException {
		return download(pathArray, localRootPath, null);
	}

	public FTPUtils download(List<String> pathArray, String localRootPath, TaskListener listener) throws IOException {
		for (String string : pathArray) {
			String localPath = localRootPath + string;
			File localFile = new File(localPath);
			if (!localFile.exists()) {
				localFile.mkdirs();
			}
		}
		for (String dir : pathArray) {
			String localPath = localRootPath + dir;// 构造本地路径
			ftpClient.changeWorkingDirectory(dir);
			FTPFile[] file = ftpClient.listFiles();
			for (FTPFile ftpFile : file) {
				if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) continue;
				File localFile = new File(localPath);
				if (!ftpFile.isDirectory()) {

					File local = new File(String.format("%s/%s", localFile, convertToLocalEncode(ftpFile.getName())));
					if (local.exists()) {
						if (listener != null) listener.skip(file.length, local);
						continue;
					}
					OutputStream out = new FileOutputStream(local);
					boolean flg = ftpClient.retrieveFile(ftpFile.getName(), out);
					if (flg) {
						log.info("与服务器同步文件成功：{}", local);
					} else {
						log.info("与服务器同步文件失败：{}", local);
					}
					out.close();
					if (listener != null) listener.finished(file.length, local);
				}
			}
		}

		return this;
	}

//	/**
//	 * 下载FTP服务器文件至本地<br/>
//	 * 操作完成后需调用logout方法与服务器断开连接
//	 * @param serverName 服务器名称
//	 * @param remotePath 下载文件存储路径
//	 * @param fileName 下载文件存储名称
//	 */
//	public boolean retrieveFile(String remotePath, String localPath, String fileName) {
//		boolean result = false;
//		try {
//			// 连接至服务器
//			connect(remotePath);
//			// 获取文件输入流
//			result = ftpClient.retrieveFile(fileName, new FileOutputStream(localPath));
//		} catch (Exception e) {
//			log.warn("下载文件时出现了一个错误", e);
//		}
//		if (result) {
//			log.info("下载文件{}成功", fileName);
//		} else {
//			log.warn("下载文件{}失败", fileName);
//		}
//		return result;
//	}

	/**
	 * 删除FTP服务器文件
	 * @param serverName 服务器名称
	 * @param remotePath 当前访问目录
	 * @param fileName 文件存储名称
	 * @return <b>true</b>：删除成功 <br/>
	 *         <b>false</b>：删除失败
	 * @throws IOException
	 */
	public FTPUtils deleteFile(String fileName) throws IOException {
		ftpClient.deleteFile(fileName);
		log.info("删除文件{}成功", fileName);
		return this;
	}

	/**
	 * 检测FTP服务器文件是否存在
	 * @param serverName 服务器名称
	 * @param remotePath 检测文件存储路径
	 * @param fileName 检测文件存储名称
	 * @return <b>true</b>：文件存在 <br/>
	 *         <b>false</b>：文件不存在
	 * @throws IOException
	 */
	public boolean checkFile(String fileName) throws IOException {
		boolean result = false;
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
		return result;
	}

	public void disconnect() {
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
				ftpClient = null;
				log.info("主动与服务器断开连接");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public String getUrl(String path) {
//		path = path.replaceAll("//", "/");
//		if (path.startsWith("/")) {
//			path = path.substring(1);
//		}
//		return "ftp://" + host + ":" + port + "/" + path;
//	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public static interface TaskListener {
		void finished(int size, File local);

		void skip(int size, File local);
	}

	private String convertToFtpEncode(String text) {
		try {
			return new String(text.getBytes("UTF-8"), FTP_ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}

	private String convertToLocalEncode(String text) {
		try {
			return new String(text.getBytes(FTP_ENCODE), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}

}
