package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.LibraryPublish.LibraryPublishType;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.cas.sim.tis.vo.SubmitInfo;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryPublishAction {
	@Resource
	@Qualifier("libraryPublishServiceFactory")
	private RmiProxyFactoryBean libraryPublishServiceFactory;

	/**
	 * 查询考核记录
	 * @param pageIndex 列表当前页
	 * @param pageSize 列表一页记录条数
	 * @param creator 记录创建人（教师、学生）
	 * @return
	 */
	public PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		return service.findPublishForTeacher(pageIndex, pageSize, creator);
	}

	public PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		return service.findPublishForStudent(pageIndex, pageSize, type, creator);
	}

	/**
	 * 根据发布编号查询
	 * @param pid
	 * @return
	 */
	public LibraryPublish findPublishById(int id) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		return service.findPublishById(id);
	}

	public List<SubmitInfo> findSubmitStateById(int id) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		return service.findSubmitStateById(id);
	}

	/**
	 * 教师发布考核
	 * @param rid 题库编号
	 * @param cid 班级编号
	 * @return 
	 */
	public Integer publishLibraryToClass(Integer rid, Integer cid) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setClassId(cid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.EXAM.getType());
		return service.publishLibraryToClass(publish);
	}

	public int practiceLibraryByStudent(int rid) {
		LibraryPublishService service = (LibraryPublishService) libraryPublishServiceFactory.getObject();
		LibraryPublish publish = new LibraryPublish();
		publish.setLibraryId(rid);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		publish.setType(LibraryPublishType.PRACTICE.getType());
		return service.practiceLibraryByStudent(publish);
	}
}
