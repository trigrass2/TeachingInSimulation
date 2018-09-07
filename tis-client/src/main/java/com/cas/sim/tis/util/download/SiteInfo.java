package com.cas.sim.tis.util.download;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 要抓取文件的信息
 * @author wds
 */
@Data
@AllArgsConstructor
public class SiteInfo {
	private String url; // 文件所在站点的url
	private String filePath; // 文件保存的路径

//	http://172.16.30.2:8082/resources/6e9fd759-718c-40f5-a3f3-5f053bb738d3.mp4
	public String getFileName() {
		return url.substring(url.lastIndexOf('/') + 1);
	}
}
