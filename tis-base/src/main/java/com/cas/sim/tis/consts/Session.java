package com.cas.sim.tis.consts;

import java.util.HashMap;
import java.util.Map;

public enum Session {
	/**
	 * 用户对象
	 */
	KEY_LOGIN_USER,

	/**
	 * 用户ID
	 */
	KEY_LOGIN_USER_ID;

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
