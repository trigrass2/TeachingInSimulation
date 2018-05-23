package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.mapper.ClassMapper;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class ClassServiceImpl implements ClassService {

	@Resource
	private ClassMapper mapper;
	@Resource
	private UserMapper userMapper;

	@Override
	public ResponseEntity findClassById(RequestEntity entity) {
		int id = entity.getInt("id");
		Class clazz = mapper.selectByPrimaryKey(id);
		return ResponseEntity.success(clazz);
	}

	@Override
	public ResponseEntity findClasses(RequestEntity entity) {
		int pageNum = entity.pageNum;
		int pageSize = entity.pageSize;
		PageHelper.startPage(pageNum, pageSize);
		List<Class> result = mapper.findClasses();
		PageInfo<Class> page = new PageInfo<>(result);
		log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", page.getTotal(), pageNum, pageSize, page.getPages());
		return ResponseEntity.success(page);
	}

	@Override
	public ResponseEntity findClassesByTeacherId(RequestEntity entity) {
		int teacherId = entity.getInt("teacherId");

		Condition condition = new Condition(Class.class);
		condition.orderBy("createDate").desc();
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("teacherId", teacherId);
		criteria.andEqualTo("del", 0);

		List<Class> classes = mapper.selectByCondition(condition);

		return ResponseEntity.success(classes);
	}

	@Override
	public void saveClasses(RequestEntity entity) {
		List<ClassInfo> infos = entity.getList("infos", ClassInfo.class);
		int creator = entity.getInt("creator");
		mapper.insertClasses(infos, creator);
	}

	@Override
	public void addClass(RequestEntity entity) {
		Class clazz = entity.getObject("clazz", Class.class);
		mapper.insert(clazz);
	}

	@Override
	@Transactional
	public void modifyClass(RequestEntity entity) {
		Class clazz = entity.getObject("clazz", Class.class);
		mapper.updateByPrimaryKeySelective(clazz);
		userMapper.updateTeacherIdByClassId(clazz.getId(), clazz.getTeacherId());
	}

}
