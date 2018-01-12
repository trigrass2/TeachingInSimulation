package com.cas.sim.tis.socket.message;

import java.util.List;

import com.cas.sim.tis.entity.Resource;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 客户端请求服务器资源 服务器响应请求
 * @author Administrator
 */
@Serializable()
public class ResourcesMessage extends AbstractMessage {
//	资源类型（Resource-> static）
	private List<Integer> resTypeList;

//	当前页码
	private int pagination;
//	每一页的数量
	private int pageSize;

//	服务器返回的资源列表
	private List<Resource> resourceList;

//	资源总量
	private int records;

	public List<Integer> getResTypeList() {
		return resTypeList;
	}

	public void setResTypeList(List<Integer> resTypeList) {
		this.resTypeList = resTypeList;
	}

	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}
}
