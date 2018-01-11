package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Section;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.mapper.ClassMapper;
import com.cas.sim.tis.mapper.IMapper;
import com.cas.sim.tis.mapper.LessonMapper;
import com.cas.sim.tis.mapper.LessonResourceMapper;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.mapper.QChoiceMapper;
import com.cas.sim.tis.mapper.QCompletionMapper;
import com.cas.sim.tis.mapper.QJudgmentMapper;
import com.cas.sim.tis.mapper.QuestionMapper;
import com.cas.sim.tis.mapper.RCircuitryMapper;
import com.cas.sim.tis.mapper.RCognitionMapper;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.mapper.SchematicMapper;
import com.cas.sim.tis.mapper.SectionMapper;
import com.cas.sim.tis.mapper.StudentMapper;
import com.cas.sim.tis.mapper.TeacherMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DBTest {
	@Resource
	private ClassMapper classMapper;
	@Resource
	private LessonMapper lessonMapper;
	@Resource
	private LessonResourceMapper lessonResourceMapper;
	@Resource
	private LibraryMapper libraryMapper;
	@Resource
	private QChoiceMapper choiceMapper;
	@Resource
	private QCompletionMapper completionMapper;
	@Resource
	private QJudgmentMapper judgmentMapper;
	@Resource
	private QuestionMapper questionMapper;
	@Resource
	private RCircuitryMapper circuitryMapper;
	@Resource
	private RCognitionMapper cognitionMapper;
	@Resource
	private ResourceMapper resourceMapper;
	@Resource
	private SchematicMapper schematicMapper;
	@Resource
	private SectionMapper sectionMapper;
	@Resource
	private StudentMapper studentMapper;
	@Resource
	private TeacherMapper teacherMapper;

	@Test
	public void testShow() {
		List<IMapper<?>> mappers = new ArrayList<>();
		mappers.add(classMapper);
		mappers.add(lessonMapper);
		mappers.add(lessonResourceMapper);
		mappers.add(libraryMapper);
		mappers.add(choiceMapper);
		mappers.add(completionMapper);
		mappers.add(judgmentMapper);
		mappers.add(questionMapper);

		mappers.add(circuitryMapper);
		mappers.add(cognitionMapper);

		mappers.add(resourceMapper);
		mappers.add(schematicMapper);
		mappers.add(sectionMapper);
		mappers.add(studentMapper);
		mappers.add(teacherMapper);
		mappers.stream().forEach(IMapper::selectAll);
	}

	@Test
	public void testInsertTeacher() {
		List<Teacher> teacherList = new ArrayList<>();
		for (int i = 1; i < 2; i++) {
			Teacher teacher = new Teacher();
			teacher.setCode("1113");
			teacher.setName("老师-" + i);
			teacher.setCreateDate(new Date());
			teacher.setPassword("123456");
			teacher.setDel(0);
			teacherList.add(teacher);
		}
		teacherMapper.insertList(teacherList);
	}

	@Test
	public void testInsertResource() {
		List<com.cas.sim.tis.entity.Resource> resourceList = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			com.cas.sim.tis.entity.Resource resource = new com.cas.sim.tis.entity.Resource();
			resource.setCreatorId(5);
			resource.setName("资源-" + i);
			resource.setDesc("资源描述-" + i);
			resource.setKeyword("关键字-" + i);
			resource.setPath("资源路径-" + i);
			resource.setType(i);
			resource.setCreateDate(new Date());
			resource.setDel(0);
			resourceList.add(resource);
		}
		resourceMapper.insertList(resourceList);
	}

	@Test
	public void testInsertStudent() {
		List<Teacher> teacherList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Teacher teacher = new Teacher();
			teacher.setCode("00" + i);
			teacher.setName("老师-" + i);
			teacher.setCreateDate(new Date());
			teacher.setPassword("123456");
			teacher.setDel(0);
			teacherList.add(teacher);
		}
		teacherMapper.insertList(teacherList);
	}

//		
	@Test
	public void testInsertSection() {
		List<Section> sectionList = new ArrayList<>();
		Section section = new Section();
		section.setLvl(Section.LVL_0_SUBJECT);
		section.setUpperId(0);
		section.setName("课程1");
		section.setCreateDate(new Date());
		section.setCreatorId(5);
		section.setDel(0);
		sectionList.add(section);
		for (int i = 0; i < 3; i++) {
			Section teacher = new Section();
			teacher.setLvl(Section.LVL_1_PROJECT);
			teacher.setName("项目-" + i);
			teacher.setUpperId(0);
			teacher.setCreateDate(new Date());
			teacher.setCreatorId(5);
			teacher.setSort(i);
			teacher.setDel(0);
			sectionList.add(teacher);
		}
		for (int i = 0; i < 3; i++) {
			Section teacher = new Section();
			teacher.setLvl(Section.LVL_2_TASK);
			teacher.setName("任务-" + i);
			teacher.setUpperId(4);
			teacher.setCreateDate(new Date());
			teacher.setCreatorId(5);
			teacher.setSort(i);
			teacher.setDel(0);
			sectionList.add(teacher);
		}
		for (int i = 0; i < 3; i++) {
			Section teacher = new Section();
			teacher.setLvl(Section.LVL_3_KNOWLEDGE);
			teacher.setName("知识点-" + i);
			teacher.setUpperId(7);
			teacher.setCreateDate(new Date());
			teacher.setCreatorId(7);
			teacher.setSort(i);
			teacher.setDel(0);
			sectionList.add(teacher);
		}
		sectionMapper.insertList(sectionList);
	}

}
