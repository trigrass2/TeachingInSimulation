package com.cas.sim.tis.message;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 考核相关消息接受
 * @功能 ExamMessage.java
 * @作者 Caowj
 * @创建日期 2018年2月10日
 * @修改人 Caowj
 */
@Serializable
public class ExamMessage extends AbstractMessage {

	/**
	 * 服务器发起考核
	 */
	public static final int EXAM_START = 1;
	/**
	 * 服务器结束考核
	 */
	public static final int EXAM_OVER = 2;
	/**
	 * 学生提交考核
	 */
	public static final int EXAM_SUBMIT = 3;
	/**
	 * 题库编号
	 */
	private int pid;
	/**
	 * 学生编号
	 */
	private int sid;
	/**
	 * 信息类型
	 */
	private int type;
	/**
	 * 学生答题结果<br>
	 * Map<试题编号,学生答案><br>
	 * JSONArray.toString()
	 */
	private String result;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
