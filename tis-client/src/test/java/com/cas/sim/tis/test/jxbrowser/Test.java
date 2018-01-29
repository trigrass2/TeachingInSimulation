package com.cas.sim.tis.test.jxbrowser;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Test {
	private static final byte[] a = { 120, -7, -64, -10, -27, 104, 124, -25, 102, 30, -19, 18, 71, 99, 64, -9, 66, 83, -126, 115, -75, 22, 106, -32, 48, -61, -39, 104, -119, -116, 5, -57, -57, -18, -46, -111, -14, -40, 8, 120, -82, 121, 18, 24, -83, 68, 60, Byte.MIN_VALUE, 116, 116 };

	private static void a(byte[] paramArrayOfByte) {
		int i = 0;
		int j = paramArrayOfByte.length;
		while (i < j) {
			int tmp12_11 = 0;
			paramArrayOfByte[tmp12_11] = ((byte) (paramArrayOfByte[tmp12_11] ^ a[(i % 50)]));
			i++;
		}
	}

	public static String d(String paramString) {
		byte[] param = new BigInteger(paramString,36).toByteArray();
		byte[] param2;
		a(param2 = param);
		try {
			return paramString = new String(param2, "UTF-8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
//			throw f((paramString = localUnsupportedEncodingException).getMessage());
		}
		return null;
	}

//	protected static RuntimeException f(String paramString) {
//		(paramString = new RuntimeException(paramString)).setStackTrace(new StackTraceElement[0]);
//		return paramString;
//	}
	
	public static void main(String[] args) {
//		System.err.println(d("106h6jqri8vfn4xt2wa9sbhgl5rnhh3du04mc0w680mjnte4i53d33ecsbskh6ao"));
//		System.err.println(d("azgyw1uv8z3saevtpfuscjdn1ovhfuu8n3jcmkf01xm2gtfd40infk"));
//		System.err.println(d("-4njllqpr2n2m62h303cst4lers4j13jyuqjklo6u2i743"));
//		System.err.println(d("ecjgpw1257bg77iav"));
		System.err.println(d("-b2w6my0doos5746solpr8k2bhp4gwbele0"));
		System.err.println(d("-5fz9u9b1d9n77sjezuuai80ktm4k8yjirbsplio9m6yt0"));
		System.err.println(d("-6bspffqi914xs2ut3d0ieleutxmzj0t4zx"));
		System.err.println(d("ws9f9lj0luj1n2woe9gkorn"));
		System.err.println(d("hj0uzzlazqhhkmle7r"));
		System.err.println(d("4edjhtgxnj7kqwlxzq1v1u"));
		System.err.println(d("-8dwi3nrcrep8rf"));
//		System.err.println(d("-cdvdo6flt8qlce"));
//		System.err.println(d("4nerc"));
		System.err.println(d("-asu0f8zb2yey"));
		System.err.println(d("-b2w6my0doos5746solprzp7wassdwm0xc9"));
		
		System.err.println(System.getProperty("java.io.tmpdir"));
		System.err.println(System.getProperty("user.home"));
	}
}
