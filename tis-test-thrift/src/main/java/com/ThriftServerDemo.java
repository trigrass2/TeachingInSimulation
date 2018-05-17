package com;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.thrift.TestQry;

public class ThriftServerDemo {
	private final static int DEFAULT_PORT = 1101;
	private static TServer server = null;

	public static void main(String[] args) {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(DEFAULT_PORT);
			TestQry.Processor processor = new TestQry.Processor(new QueryImp());
			TNonblockingServer.Args arg = new TNonblockingServer.Args(socket);
			arg.protocolFactory(new TBinaryProtocol.Factory());
			arg.transportFactory(new TFramedTransport.Factory());
			arg.processorFactory(new TProcessorFactory(processor));
			server = new TNonblockingServer(arg);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}