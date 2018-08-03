package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationLibrary;
import com.cas.sim.tis.services.PreparationLibraryService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class PreparationLibraryAction {
	
	@Resource
	private PreparationLibraryService service;

	/**
	 * 通过备课编号获得备课试题集合
	 * @param pid 备课编号
	 * @return List PreparationLibrary集合
	 */
	public List<PreparationLibrary> findPreparationLibraryByPreparationId(Integer pid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))
				.build();
		ResponseEntity resp = service.findPreparationLibraryByPreparationId(req);
		return JSON.parseArray(resp.data, PreparationLibrary.class);
	}
	
	public void addPreparationLibrary(Integer pid, PreparationLibrary library) {
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("library", library)//
				.build();
		service.addPreparationLibrary(req);
	}

	public void updatePreparationLibrary(PreparationLibrary library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("library", library)//
				.build();
		service.updatePreparationLibrary(req);
		
	}

	public void deletePreparationLibrary(PreparationLibrary library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("library", library)//
				.build();
		service.deletePreparationLibrary(req);
	}
}
