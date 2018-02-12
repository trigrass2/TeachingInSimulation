package com.cas.sim.tis.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.xml.util.JaxbUtil;

public class BasicTest {
	@Test
	public void testList() throws Exception {
		List<ResourceType> resourceTypes = new ArrayList<>();
		resourceTypes.add(ResourceType.EXCEL);
		resourceTypes.add(ResourceType.IMAGE);
		resourceTypes.add(ResourceType.SWF);
		resourceTypes.add(ResourceType.VIDEO);
		List<Integer> orders = resourceTypes.stream().map(ResourceType::getType).collect(Collectors.toList());
		orders.forEach(System.out::println);
	}

	@Test
	public void testParseCfgLocal() throws Exception {
		URL url = new URL("http://192.168.1.19:8082/configurations/Accontactor/CJX2-1210.xml");
//		URL url = new URL("file:///C:/Users/Administrator/Documents/New%20folder/Accontactor/CJX2-1210.xml");
		ElecCompDef eleccomp = JaxbUtil.converyToJavaBean(url, ElecCompDef.class);
		System.out.println(eleccomp);
	}
}
