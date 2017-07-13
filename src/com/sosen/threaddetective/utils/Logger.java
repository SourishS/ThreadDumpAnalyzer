package com.sosen.threaddetective.utils;

public class Logger {
	public static void log(Class<?> clazz, String msg, Object... args) {
		System.out.println(clazz.getSimpleName() + ":" + String.format(msg, args));
	}

	public static void log(Class<?> clazz, String msg, Throwable t) {
		System.out.println(clazz.getSimpleName() + ":" + msg);
		t.printStackTrace();
	}
}
