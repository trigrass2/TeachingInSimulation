package com.cas.sim.tis.consts;

import com.cas.sim.tis.util.MsgUtil;

public enum QuestionType {
	CHOICE(0, MsgUtil.getMessage("question.choice"), "选择题"), JUDGMENT(1, MsgUtil.getMessage("question.judgment"), "判断题"), BLANK(2, MsgUtil.getMessage("question.blank"), "填空题"), SUBJECTIVE(3, MsgUtil.getMessage("question.subjective"), "主观题");

	private int type;
	private String title;
	private String sheetName;

	private QuestionType(int type, String title, String sheetName) {
		this.type = type;
		this.title = title;
		this.sheetName = sheetName;
	}

	public int getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}
	
	public String getSheetName() {
		return sheetName;
	}

	public static QuestionType getQuestionType(Integer type) {
		for (QuestionType questionType : values()) {
			if (questionType.type == type) {
				return questionType;
			}
		}
		throw new RuntimeException("不支持的资源类型" + type);
	}
}
