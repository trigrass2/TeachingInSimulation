package com.cas.sim.tis.view.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.vo.LibraryRecordInfo;

@Component
public class LibraryRecordAction {
	@Resource
	@Qualifier("libraryRecordServiceFactory")
	private RmiProxyFactoryBean libraryRecordServiceFactory;

	/**
	 * 查询考核记录下的学生答题记录
	 * @param pid
	 * @return
	 */
	public List<LibraryRecordInfo> findPublishForTeacher(int pid) {
		LibraryRecordService service = (LibraryRecordService) libraryRecordServiceFactory.getObject();
		return service.findRecordByPublishId(pid);
	}
	
}
