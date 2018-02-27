package com.cas.sim.tis.message.handler;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.ExamView;
import com.cas.sim.tis.view.controller.ExamController;
import com.jme3.network.Client;

import javafx.application.Platform;

public class ExamMessageHandler implements ClientHandler<ExamMessage> {

	@Override
	public void execute(Client client, ExamMessage m) throws Exception {
		int type = m.getType();
		if (ExamMessage.EXAM_START == type) {
			LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(m.getPid());
			Platform.runLater(() -> {
				Application.showView(ExamView.class);

				ExamController controller = SpringUtil.getBean(ExamController.class);
				controller.initialize(publish);
			});
		} else if (ExamMessage.EXAM_OVER == type) {
			Platform.runLater(() -> {
				ExamController controller = SpringUtil.getBean(ExamController.class);
				if (m.getPid() == controller.getLibraryPublish().getId()) {
					controller.submit(true);
				}
			});
		}
	}
}
