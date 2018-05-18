package com.cas.sim.tis.service.impl;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.service.ClassService;
import com.cas.sim.tis.thrift.ThriftEntity;

@Service
public class ClassServiceImpl implements ClassService.Iface {

	@Override
	public ThriftEntity findClassAll(int pageIndex, int pageSize) throws TException {
		return new ThriftEntity();
	}

	@Override
	public ThriftEntity findClassesByTeacher(int teacherId) throws TException {
		return new ThriftEntity();
	}

	@Override
	public ThriftEntity saveClasses(String infos, int creator) throws TException {
		return new ThriftEntity();
	}

	@Override
	public ThriftEntity modifyClass(String info) throws TException {
		return new ThriftEntity();
	}
}
