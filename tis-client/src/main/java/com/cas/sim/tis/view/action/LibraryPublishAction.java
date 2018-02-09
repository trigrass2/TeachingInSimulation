package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
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
}
