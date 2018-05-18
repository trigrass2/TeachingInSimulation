package com.cas.sim.tis.thrift;

import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ThriftStruct
@ToString
@EqualsAndHashCode
public class RequestEntity {
//	发送的数据
	@ThriftField(1)
	public String data;

//	其他通用参数：
//	当前页
//	-1 表示不分页
	@ThriftField(2)
	public int pageNum = -1;
//	每页的数量
	@ThriftField(3)
	public int pageSize = 10;
}
