package com.cas.sim.tis.thrift;

import com.alibaba.fastjson.JSONObject;

public class RequestEntityBuilder {
	private RequestEntity entity = new RequestEntity();

	private JSONObject json = new JSONObject();

	public RequestEntityBuilder set(String key, Object value) {
		json.put(key, value);
		return this;
	}

	public RequestEntityBuilder pageNum(int pageNum) {
		entity.pageNum = pageNum;
		return this;
	}

	public RequestEntityBuilder pageSize(int pageSize) {
		entity.pageSize = pageSize;
		return this;
	}

	public RequestEntity build() {
		entity.data = json.toString();
		return entity;
	}

}
