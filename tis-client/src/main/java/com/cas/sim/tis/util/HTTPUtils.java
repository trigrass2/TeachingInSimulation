package com.cas.sim.tis.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HTTPUtils {
	private static String host;
	private static Integer port;
	static {
		host = AppPropertiesUtil.getStringValue("server.httpd.address");
		port = AppPropertiesUtil.getIntValue("server.httpd.port");
	}

	public static URL getUrl(@Nonnull String path) {
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
				log.warn("资源不存在:{}", url.toString());
			}
		} catch (IOException e) {
			String msg = "无效的地址:" + path;
			log.warn(msg, e);
		}
		return null;
	}

	@Nullable
	public static String getFullPath(String path) {
		URL url = getUrl(path);
		if (url != null) {
			return url.toString();
		}
		return null;
	}

}
