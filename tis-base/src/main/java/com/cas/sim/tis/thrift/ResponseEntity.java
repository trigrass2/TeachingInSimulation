package com.cas.sim.tis.thrift;

import com.alibaba.fastjson.JSON;

import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ThriftStruct
@EqualsAndHashCode
@ToString
public class ResponseEntity {
//	正常返回200
	public static final int SUCCESS = 200;
//	服务器出现任何异常，返回500
	public static final int FAILURE = 500;

	@ThriftField(1)
	/**
	 * 返回码, 1成功，0失败
	 */
	public int code;
	@ThriftField(2)
	/**
	 * 响应信息
	 */
	public String msg;
	@ThriftField(3)
	/**
	 * 返回数据
	 */
	public String data;

	public static ResponseEntity success(Object data) {
		ResponseEntity entity = new ResponseEntity();
		entity.code = ResponseEntity.SUCCESS;
		entity.msg = "OK";
		entity.data = JSON.toJSONString(data);
		return entity;
	}

	public static ResponseEntity failure(String msg) {
		ResponseEntity entity = new ResponseEntity();
		entity.code = ResponseEntity.FAILURE;
		entity.msg = msg;
		entity.data = "";
		return entity;
	}
}
