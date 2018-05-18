package com.cas.sim.tis.service.impl;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.service.UserService;
import com.cas.sim.tis.thrift.ThriftEntity;

@Service
public class UserServiceImpl implements UserService.Iface {

	@Override
	public ThriftEntity login(String usercode, String password) throws TException {
		return new ThriftEntity(1, "msg-1", "success");
	}

	@Override
	public ThriftEntity findTeacherAll() throws TException {
		return new ThriftEntity(2, "msg-1", "success");
	}

	@Override
	public ThriftEntity findTeachersForPage(int pageIndex, int pageSize) throws TException {
		return new ThriftEntity(1, "msg-1", "success");
	}

	@Override
	public ThriftEntity findStudentsForPageByTeacherId(int pageIndex, int pageSize, int classId) throws TException {
		return new ThriftEntity(1, "msg-1", "success");
	}

}
