package com.cas.sim.tis.thrift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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

	private JSONObject json;

	public RequestEntity set(String key, Object value) {
		if (json == null) {
			json = new JSONObject();
		}
		json.put(key, value);
		return this;
	}

	public void end() {
		this.data = json.toJSONString();
	}

	public Integer getInt(String key) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getInteger(key);
	}

	public String getString(String key) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getString(key);
	}

	public Object getObject(String key) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.get(key);
	}

}
