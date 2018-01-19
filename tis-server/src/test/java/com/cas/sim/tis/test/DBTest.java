package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.mapper.CatalogMapper;
import com.cas.sim.tis.mapper.ClassMapper;
import com.cas.sim.tis.mapper.ExaminationAnswerMapper;
import com.cas.sim.tis.mapper.ExaminationMapper;
import com.cas.sim.tis.mapper.IMapper;
import com.cas.sim.tis.mapper.LessonMapper;
import com.cas.sim.tis.mapper.LessonResourceMapper;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.mapper.QuestionMapper;
import com.cas.sim.tis.mapper.RCircuitryMapper;
import com.cas.sim.tis.mapper.RCognitionMapper;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.mapper.SchematicMapper;
import com.cas.sim.tis.mapper.StudentMapper;
import com.cas.sim.tis.mapper.TeacherMapper;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DBTest {
	@Resource
	private ClassMapper classMapper;
	@Resource
	private ExaminationMapper examinationMapper;
	@Resource
	private ExaminationAnswerMapper examinationAnswerMapper;
	@Resource
	private LessonMapper lessonMapper;
	@Resource
	private LessonResourceMapper lessonResourceMapper;
	@Resource
	private LibraryMapper libraryMapper;
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
	private CatalogMapper sectionMapper;
	@Resource
	private StudentMapper studentMapper;
	@Resource
	private TeacherMapper teacherMapper;

	@Test
	public void testLogin() throws Exception {
		Condition condition = new Condition(Student.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("code", "admin");
		criteria.andEqualTo("password", "123456");
//		criteria.andEqualTo("role", 3);

//		teacherMapper.selectAll();
		
		try {
			List<Teacher> teachers = teacherMapper.selectByCondition(condition);
			teachers.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testShow() {
		List<IMapper<?>> mappers = new ArrayList<>();
//		mappers.add(classMapper);
//		mappers.add(examinationMapper);
//		mappers.add(examinationAnswerMapper);
//		mappers.add(lessonMapper);
//		mappers.add(lessonResourceMapper);
//		mappers.add(libraryMapper);
//		mappers.add(questionMapper);
//
//		mappers.add(resourceMapper);
//		mappers.add(schematicMapper);
//		mappers.add(sectionMapper);
		mappers.add(studentMapper);
		mappers.add(teacherMapper);
		mappers.stream().forEach(IMapper::selectAll);
	}
}
