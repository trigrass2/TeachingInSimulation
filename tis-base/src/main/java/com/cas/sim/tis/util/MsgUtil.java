package com.cas.sim.tis.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
public class MsgUtil implements MessageSourceAware {
	private static MessageSource messageSource; // 自动注入对象

	public static String getMessage(String key, Object... args) {
		return getMessage(key, args, Locale.getDefault());
	}

	public static String getMessage(String key, Object[] args, Locale locale) {
		return messageSource.getMessage(key, args, Locale.getDefault());
	}

	public void setMessageSource(MessageSource messageSource) {
		setMessageSource0(messageSource);
	}

	public static void setMessageSource0(MessageSource messageSource) {
		MsgUtil.messageSource = messageSource;
	}
}
