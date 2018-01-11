package com.cas.sim.tis.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Section;
import com.cas.sim.tis.services.SectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SectionTest {
	@Resource
	private SectionService sectionService;

	@Test
	public void testName() throws Exception {
		System.err.println(sectionService.findSections(Section.LVL_0_SUBJECT, null));
		System.err.println(sectionService.findSections(Section.LVL_1_PROJECT, 1));
		System.err.println(sectionService.findSections(Section.LVL_2_TASK, 4));
		System.err.println(sectionService.findSections(Section.LVL_3_KNOWLEDGE, 6));
	}
}
