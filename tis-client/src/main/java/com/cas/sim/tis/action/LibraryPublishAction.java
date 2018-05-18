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
	 * 查询考核记录
	 * @param pageIndex 列表当前页
	 * @param pageSize 列表一页记录条数
	 * @param creator 记录创建人（教师、学生）
	 * @return
	 */
	public PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		req.set("creator", creator).end();
		ResponseEntity resp = service.findPublishForTeacher(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<LibraryPublishForTeacher>>() {});
	}

	public PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		req.set("type", type).set("creator", creator).end();
		ResponseEntity resp = service.findPublishForStudent(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<LibraryPublishForStudent>>() {});
	}

	/**
	 * 根据发布编号查询
	 * @param pid
	 * @return
	 */
	public LibraryPublish findPublishById(int id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		ResponseEntity resp = service.findPublishById(req);
		return JSON.parseObject(resp.data, LibraryPublish.class);
	}

	public List<SubmitInfo> findSubmitStateById(int id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		ResponseEntity resp = service.findSubmitStateById(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}

	/**
	 * 教师发布考核
	 * @param rid 题库编号
	 * @param cid 班级编号
	 * @return
	 */
	public Integer publishLibraryToClass(Integer rid, Integer cid) {
		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setClassId(cid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.EXAM.getType());
		
		RequestEntity req = new RequestEntity();
		req.set("publish", publish).end();
		
		ResponseEntity resp = service.publishLibraryToClass(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	public int practiceLibraryByStudent(int rid) {

		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.PRACTICE.getType());
		
		RequestEntity req = new RequestEntity();
		req.set("publish", publish).end();
		
		ResponseEntity resp = service.practiceLibraryByStudent(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

}
