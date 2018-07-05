package com.cas.sim.tis.message.handler;

import java.util.List;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.LibraryPublishAction;
import com.cas.sim.tis.action.PreparationPublishAction;
import com.cas.sim.tis.action.QuestionAction;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.ExamView;
import com.cas.sim.tis.view.PreparationExamView;
import com.cas.sim.tis.view.controller.ExamController;
import com.cas.sim.tis.view.controller.PreparationExamController;
import com.jme3.network.Client;

import javafx.application.Platform;

public class ExamMessageHandler implements ClientHandler<ExamMessage> {

	@Override
	public void execute(Client client, ExamMessage m) throws Exception {
		int examType = m.getExamType();
		if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
			libraryExam(m);
		} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
			preparationExam(m);
		}
	}

	private void libraryExam(ExamMessage m) {
		int messageType = m.getMessageType();
		if (ExamMessage.MESSAGE_TYPE_START == messageType) {
			LibraryPublish publish = SpringUtil.getBean(LibraryPublishAction.class).findPublishById(m.getPid());
			Platform.runLater(() -> {
				Application.showView(ExamView.class);

				ExamController controller = SpringUtil.getBean(ExamController.class);
				controller.initialize(publish);
			});
		} else if (ExamMessage.MESSAGE_TYPE_OVER == messageType) {
			Platform.runLater(() -> {
				ExamController controller = SpringUtil.getBean(ExamController.class);
				if (m.getPid() == controller.getLibraryPublish().getId()) {
					controller.submit(true);
				}
			});
		}
	}

	private void preparationExam(ExamMessage m) {
		int messageType = m.getMessageType();
		if (ExamMessage.MESSAGE_TYPE_START == messageType) {
			PreparationPublish publish = SpringUtil.getBean(PreparationPublishAction.class).findPublishById(m.getPid());
			List<Question> questions = SpringUtil.getBean(QuestionAction.class).findQuestionsByQuestionIds(publish.getLibrary().getQuestionIds());

			Platform.runLater(() -> {
				Application.showView(PreparationExamView.class);

				PreparationExamController controller = SpringUtil.getBean(PreparationExamController.class);
				controller.initialize(publish, questions);
			});
		} else if (ExamMessage.MESSAGE_TYPE_OVER == messageType) {
			Platform.runLater(() -> {
				PreparationExamController controller = SpringUtil.getBean(PreparationExamController.class);
				if (m.getPid() == controller.getPreparationPublish().getId()) {
					controller.submit(true);
				}
			});
		}
	}
}
