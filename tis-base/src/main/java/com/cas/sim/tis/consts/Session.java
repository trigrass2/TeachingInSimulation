package com.cas.sim.tis.consts;

import java.util.HashMap;
import java.util.Map;

public enum Session {
	/**
	 * 用户账号
	 */
	KEY_LOGIN_ACCOUNT,

	/**
	 * 用户ID
	 */
	KEY_LOGIN_ID,
	/**
	 * 用户角色
	 */
	KEY_LOGIN_ROLE,
	/**
	 * 登录学生所属班级
	 */
	KEY_LOGIN_CLASSID,
	/**
	 * 当前正在进行的考核发布记录编号
	 */
	KEY_LIBRARY_PUBLISH_ID,
	/**
	 * 当前正在进行的备课试题测试编号
	 */
	KEY_PREPARATION_PUBLISH_ID,

	KEY_OBJECT;

	private final static Map<Session, Object> DATAS = new HashMap<>();

	public static <T> void set(Session key, T value) {
		DATAS.put(key, value);
	}

	public static <T> T get(Session key) {
		return get(key, null);
	}

	public static <T> T get(Session key, T def) {
		@SuppressWarnings("unchecked")
		T value = (T) DATAS.get(key);
		if (value != null) {
			return value;
		}
		return def;
	}

	public static void clear() {
		DATAS.clear();
	}

}
