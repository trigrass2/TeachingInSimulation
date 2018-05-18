package com.cas.sim.tis.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.cas.sim.tis.service.ClassService;
import com.cas.sim.tis.service.UserService;

import lombok.Getter;
import lombok.Setter;

public class ThriftClient {
	@Setter
	private String host;
	@Setter
	private int port;

	private TBinaryProtocol protocol;
	private TSocket transport;

	@Getter
	private UserService.Client userService;
	@Getter
	private ClassService.Client classService;

	public void init() {
		transport = new TSocket(host, port);
		protocol = new TBinaryProtocol(transport);
//		
		userService = new UserService.Client(new TMultiplexedProtocol(protocol, UserService.class.getSimpleName()));
		classService = new ClassService.Client(new TMultiplexedProtocol(protocol, ClassService.class.getSimpleName()));
	}

	public void open() throws TTransportException {
		transport.open();
	}

	public void close() {
		transport.close();
	}
}
