package com.cas.sim.tis.thrift;

import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ThriftStruct
@EqualsAndHashCode
@ToString
public class ResponseEntity {
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
}