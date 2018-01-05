package com.cas.sim.tis.socket;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileServer implements IServer {
	private static final Logger LOG = LoggerFactory.getLogger(FileServer.class);

	private static final FileServer ins = new FileServer();
	private FtpServer server;

	public static FileServer getIns() {
		return ins;
	}

	private FileServer() {
//		Apache Ftp Server 默认的编码是"UTF-8"
		FtpServerFactory serverFactory = new FtpServerFactory();
//		注册用户
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("users.properties"));
		serverFactory.setUserManager(userManagerFactory.createUserManager());

		server = serverFactory.createServer();
	}

	@Override
	public void start() {
		try {
			server.start();
			LOG.info("文件服务器已启动..");
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		server.stop();
		LOG.info("文件服务器已关闭！");
	}

}
