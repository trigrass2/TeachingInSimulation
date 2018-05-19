package com.cas.sim.tis.test;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.mapper.data.ReserchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MapperTest {
	@Resource
	private ResourceMapper mapper;

	@Test
	public void testCountResource() throws Exception {
		List<ReserchResult> result = mapper.countResourceByTypes(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6 }), null, 1);
		result.forEach(System.out::println);
	}

	@Test
	public void testCountBrowseResourceByTypes() throws Exception {
		List<ReserchResult> result = mapper
				.countBrowseResourceByTypes(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6 }), null, 1);
		result.forEach(System.out::println);
	}

	@Test
	public void testCountCollectionResourceByTypes() throws Exception {
		List<ReserchResult> result = mapper
				.countCollectionResourceByTypes(Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6 }), null, 1);
		result.forEach(System.out::println);
	}
}
