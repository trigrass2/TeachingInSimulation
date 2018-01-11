package com.cas.circuit.xml.adapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.cas.circuit.vo.Terminal;

public class MapAdapter extends XmlAdapter<Terminal[], Map<String, Terminal>> {

//	/**
//	 * 功能:xml->Map<String,Object>
//	 * @param adapterMap
//	 * @return
//	 * @throws Exception
//	 */
//	@Override
//	public Map<String, Object> unmarshal(AdapterMap adapterMap) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		for (Entry<String, Object> en : adapterMap.entry) {
//			map.put(en.getKey(), en.getValue());
//		}
//		return map;
//	}
//
//	/**
//	 * 功能:map<String,Object> -> xml
//	 */
//	@Override
//	public AdapterMap marshal(Map<String, Object> map) throws Exception {
//		AdapterMap adapterMap = new AdapterMap();
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			Entry entry2 = new Entry();
//			entry2.setKey(entry.getKey());
//			entry2.setValue(entry.getValue());
//			adapterMap.entry.add(entry2);
//		}
//		return adapterMap;
//	}

	@Override
	public Map<String, Terminal> unmarshal(Terminal[] dataList) throws Exception {
		if (dataList == null) {
			return null;
		}
		Map<String, Terminal> map = new HashMap<>();
		for (int i = 0; i < dataList.length; i++) {
			map.put(dataList[i].getId(), dataList[i]);
		}

		return map;
	}

	@Override
	public Terminal[] marshal(Map<String, Terminal> map) throws Exception {
		if (map == null) {
			return null;
		}
		Terminal[] dataList = new Terminal[map.size()];
		Collection<Terminal> values = map.values();
		int index = 0;
		Iterator<Terminal> itor = values.iterator();
		while (itor.hasNext()) {
			dataList[index++] = itor.next();
		}
		return dataList;
	}
}
