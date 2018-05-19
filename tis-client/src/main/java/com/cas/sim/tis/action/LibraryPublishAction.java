package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.LibraryPublish.LibraryPublishType;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryPublishAction extends BaseAction {
	@Resource
	private LibraryPublishService service;

	/**
	 * 根据教师编号获得该用户发布的试题考核
	 * @param pageIndex 当前页
	 * @param pageSize 查询条数
	 * @param creator 教师编号
	 * @return 教师查看的分页集合
	 */
	public PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findPublishForTeacher(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<LibraryPublishForTeacher>>() {});
	}

	/**
	 * 根据学生编号获得该用户发布的试题练习/参与的试题考核
	 * @param pageIndex 当前页
	 * @param pageSize 查询条数
	 * @param type 试题发布类型：PublishType
	 * @param creator 学生编号
	 * @return 学生查看的分页集合
	 */
	public PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.set("type", type)//
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findPublishForStudent(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<LibraryPublishForStudent>>() {});
	}

	/**
	 * 根据试题考核发布编号查询试题考核发布对象
	 * @param id 试题考核发布编号
	 * @return 试题考核发布对象
	 */
	public LibraryPublish findPublishById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPublishById(req);
		return JSON.parseObject(resp.data, LibraryPublish.class);
	}

	/**
	 * 根据试题考核发布编号获得学生提交状态
	 * @param id 试题考核发布编号
	 * @return 学生提交状态集合
	 */
	public List<SubmitInfo> findSubmitStateById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findSubmitStateById(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}

	/**
	 * 教师发布考核，并向考核班级所有学生广播考核开始
	 * @param rid 试题库编号
	 * @param cid 班级编号
	 * @return
	 */
	public Integer publishLibraryToClass(Integer rid, Integer cid) {
		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setClassId(cid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.EXAM.getType());

		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();

		ResponseEntity resp = service.publishLibraryToClass(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	/**
	 * 学生发布练习
	 * @param rid 试题库编号
	 * @return
	 */
	public int practiceLibraryByStudent(int rid) {

		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.PRACTICE.getType());

		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();

		ResponseEntity resp = service.practiceLibraryByStudent(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

}
