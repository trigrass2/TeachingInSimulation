package com.cas.sim.tis.thrift;

import javax.annotation.Resource;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.service.ClassService;
import com.cas.sim.tis.service.UserService;
import com.cas.sim.tis.service.impl.ClassServiceImpl;
import com.cas.sim.tis.service.impl.UserServiceImpl;

@Component
public class ThriftServer {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${thrift.port}")
	private int port;
	@Value("${thrift.minWorkerThreads}")
	private int minThreads;
	@Value("${thrift.maxWorkerThreads}")
	private int maxThreads;

	private TBinaryProtocol.Factory protocolFactory;
	private TTransportFactory transportFactory;

	@Resource
	private UserServiceImpl userService;
	@Resource
	private ClassServiceImpl classService;

	public void init() {
		protocolFactory = new TBinaryProtocol.Factory();
		transportFactory = new TTransportFactory();
	}

	public void start() {
		TMultiplexedProcessor processor = new TMultiplexedProcessor();
		processor.registerProcessor(UserService.class.getSimpleName(), new UserService.Processor<>(userService));
		processor.registerProcessor(ClassService.class.getSimpleName(), new ClassService.Processor<>(classService));

		init();
		try {
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
			tArgs.processor(processor);
			tArgs.protocolFactory(protocolFactory);
			tArgs.transportFactory(transportFactory);
			tArgs.minWorkerThreads(minThreads);
			tArgs.maxWorkerThreads(maxThreads);

			TServer server = new TThreadPoolServer(tArgs);
			// TServer server = new TSimpleServer(tArgs);
			logger.info("thrift服务启动成功, 端口={}", port);
			server.serve();
		} catch (Exception e) {
			logger.error("thrift服务启动失败", e);
		}

	}
}