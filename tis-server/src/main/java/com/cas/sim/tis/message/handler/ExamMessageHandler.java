package com.cas.sim.tis.message.handler;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.message.ExamMessage;
import com.jme3.network.HostedConnection;

@Component
public class ExamMessageHandler implements ServerHandler<ExamMessage> {

	@Override
	public void execute(HostedConnection source, ExamMessage m) throws Exception {
		
	}

}
