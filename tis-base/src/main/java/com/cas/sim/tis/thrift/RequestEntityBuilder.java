package com.cas.sim.tis.thrift;

public class RequestEntityBuilder {

	private RequestEntity entity = new RequestEntity();

	public RequestEntityBuilder set(String key, Object value) {
		entity.set(key, value);
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
		return entity;
	}

}
