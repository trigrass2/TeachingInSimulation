package com.cas.sim.tis.test;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.LibraryAction;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ActionTest {
	@Resource
	private LibraryAction action;
	@Resource
	private ResourceAction resourceAction;

	@Test
	public void testFindLibraryByType() throws Exception {
		PageInfo<Library> result = action.findLibraryByType(1, 10, 0);
		System.out.println(result);
	}
	
	@Test
	public void testResourceAction() throws Exception {
		System.out.println("ServiceTest.testUserService()" + System.currentTimeMillis());
		Map<Integer, Integer> resp = resourceAction.countResourceByType(
				ResourceMenuType.ADMIN_SYS, Arrays.asList(new Integer[] {0,1,2,3,4,5,6}), null, 1);
				
		System.out.println(resp);
		System.out.println("ServiceTest.testUserService()" + System.currentTimeMillis());
	}
}
