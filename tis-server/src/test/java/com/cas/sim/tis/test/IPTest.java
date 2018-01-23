package com.cas.sim.tis.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.junit.Test;

public class IPTest {
//	@Test
//	public void testName() throws Exception {
//		System.out.println("IPTest.testName()" + (99>>2));
//		int a = 'A'/'a'> 1 ? 4: 5;
//		a+='A';
//		System.out.println( ((int)'a'));
//	}
	
	@Test
	public void testNb() throws Exception {
		String ip = null;
		String host = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			host = ia.getHostName();// 获取计算机名字
			ip = ia.getHostAddress();// 获取IP
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(host);
		System.out.println(ip);

//		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//		InetAddress ip = null;
//		while (allNetInterfaces.hasMoreElements()) {
//			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//			// System.out.println(netInterface.getName());
//			Enumeration addresses = netInterface.getInetAddresses();
//			while (addresses.hasMoreElements()) {
//				ip = (InetAddress) addresses.nextElement();
//				if (ip != null && ip instanceof Inet4Address) {
//					System.out.println("本机的IP = " + ip.getHostAddress());
//				}
//			}
//		}
	}

	// // 获取真实IP的方法()
	// public String getIpAddr() {
	// String ip = request.getHeader("x-forwarded-for");
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getHeader("Proxy-Client-IP");
	// }
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getHeader("WL-Proxy-Client-IP");
	// }
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getHeader("CLIENTIP");
	// }
	// if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	// ip = request.getRemoteAddr();
	// }
	// // System.out.println(request.getRemoteHost());
	// return ip;
	// }

	/**
	 * 多IP处理，可以得到最终ip
	 * 
	 * @return
	 */
	@Test
	public void testGetIP() {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;// 是否找到外网IP
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						netip = ip.getHostAddress();
						System.out.println(netip);
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
						localip = ip.getHostAddress();
						System.out.println(localip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
