package com.cas.sim.tis.thrift;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

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

	public Boolean getBoolean(String key) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getBoolean(key);
	}

	public <T> T getObject(String key, Class<T> clazz) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getObject(key, clazz);
	}

	public <T> List<T> getList(String key, Class<T> clazz) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getJSONArray(key).toJavaList(clazz);
	}

	public <K, V> Map<K, V> getMap(String key, Class<K> mapKey, Class<V> mapValue) {
		if (json == null) {
			json = JSON.parseObject(data);
		}
		return json.getJSONArray(key).toJavaObject(new TypeReference<HashMap<K, V>>() {});
	}
}
