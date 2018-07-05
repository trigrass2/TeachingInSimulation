package com.cas.sim.tis.view.control.imp.preparation;

import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.question.TeacherQuestionPaper;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PublishLibraryFeedback extends DialogPane<Void> {


	/**
	 * 实例化Dialog面板
	 * @param pid PreparationPublish.id
	 */
	public PublishLibraryFeedback(Integer publishId, Integer preparationLibraryId) {
		TeacherQuestionPaper paper = new TeacherQuestionPaper(publishId, preparationLibraryId, LibraryRecordType.PREPARATION);
		this.getChildren().addAll(paper.getContent());
		VBox.setVgrow(paper, Priority.ALWAYS);
	}
}
