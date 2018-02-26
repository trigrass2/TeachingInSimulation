package com.cas.sim.tis.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nullable;

import org.slf4j.LoggerFactory;

public class HTTPUtils {
	private String host;

	private Integer port;

	public URL getUrl(String path) {
		path = path.replaceAll("//", "/");
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		try {
			URL url = new URL("http://" + host + ":" + port + "/" + path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
				return url;
			} else if (HttpURLConnection.HTTP_NOT_FOUND == conn.getResponseCode()) {
				LoggerFactory.getLogger(HTTPUtils.class).warn("资源不存在:{}", url.toString());
			}
		} catch (IOException e) {
			String msg = "无效的地址:" + path;
			LoggerFactory.getLogger(HTTPUtils.class).warn(msg, e);
//			throw new RuntimeException(msg, e);
		}
		return null;
	}

	@Nullable
	public String getFullPath(String path) {
		URL url = getUrl(path);
		if (url != null) {
			return url.toString();
		}
		return null;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
