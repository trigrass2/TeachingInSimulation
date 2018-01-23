package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.cas.sim.tis.consts.ResourceType;

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
}
