package com.cas.sim.tis.util;

public class HTTPUtils {
	private String host;

	private Integer port;

	public String getHttpUrl(String path) {
		path = path.replaceAll("//", "/");
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		return "http://" + host + ":" + port + "/" + path;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
