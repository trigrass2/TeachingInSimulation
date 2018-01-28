package com.cas.sim.tis.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.MessageFormat;

import org.slf4j.LoggerFactory;

public class SocketTest {
	public static void test(String host, Integer port) throws RuntimeException {
		try (Socket socket = new Socket()) {
			socket.setReceiveBufferSize(1024);
			socket.setSoTimeout(1000);// socket.setSoTimeout(2000);
			SocketAddress address = new InetSocketAddress(host, port);
			socket.connect(address, port);// 1.判断ip、端口是否可连接
		} catch (Exception e) {
			String msg = MessageFormat.format("无法连接到服务器{0}:{1}", host, port);

			LoggerFactory.getLogger(SocketTest.class).error(msg);
			throw new RuntimeException(msg);
		}

	}
}
