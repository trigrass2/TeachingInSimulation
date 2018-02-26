package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.services.ResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PageHelpTest {
	@Resource
	private ResourceService resourceService;

	@Test
	public void testPage() throws Exception {
		Condition condition = new Condition(com.cas.sim.tis.entity.Resource.class);
		Criteria criteria = condition.createCriteria();
//		条件1、查找用户指定的几种资源类型
		criteria.andIn("type", new ArrayList<>(Arrays.asList(1, 4)));
//		条件2、只能查找管理员或自己上传的资源。
//		属于自己的资源
//		criteria.andEqualTo("creatorId", 2);
//		或者是公开的资源
		criteria.orEqualTo("share", true);

		PageHelper.startPage(0, 2);
		List<com.cas.sim.tis.entity.Resource> resources = resourceService.findByCondition(condition);

		PageInfo<com.cas.sim.tis.entity.Resource> page = new PageInfo<com.cas.sim.tis.entity.Resource>(resources);
		System.out.println(page.getTotal());

		Assert.assertEquals(4, resources.size());

		resources.stream().forEach(System.out::println);
	}
}
