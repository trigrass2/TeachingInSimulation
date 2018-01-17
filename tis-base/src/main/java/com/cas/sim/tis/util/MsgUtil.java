package com.cas.sim.tis.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

@Component
public class MsgUtil implements MessageSourceAware {
	private static MessageSource messageSource; // 自动注入对象

	public static String getMessage(String key, String... args) {
		return getMessage(key, args, Locale.getDefault());
	}

	public static String getMessage(String key, String[] args, Locale locale) {
		return messageSource.getMessage(key, args, Locale.getDefault());
	}

	public void setMessageSource(MessageSource messageSource) {
		MsgUtil.messageSource = messageSource;
	}

}
