/*
 * HttpConnector.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpConnector {
	/**
	 * 
	 * @param url
	 */
	public static HttpConnection open(String url) throws IOException {
		if (url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("URL must not be null/empty.");
		}
		//
		final String JAVA_ME_HTTP_IMPL_CLASS =
			"impl.javame.com.twitterapime.io.HttpConnectionImpl";
		final String JAVA_ME_HTTP_USER_AGENT =
			"Twitter API ME/1.0 (compatible; Java ME; MIDP-2.0; CLDC-1.0)";
		//
		PlatformProvider p = PlatformProviderSelector.getCurrentProvider();
		HttpConnection conn = null;
		String userAgent = null;
		try {
			if (p.getID() == PlatformProvider.PPID_JAVA_ME) {
				conn = (HttpConnection)
					Class.forName(JAVA_ME_HTTP_IMPL_CLASS).newInstance();
				userAgent = JAVA_ME_HTTP_USER_AGENT;
			} else {
				throw new IllegalArgumentException(
					"Unknown platform ID: " + p.getID());
			}
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		}
		//
		System.out.println(url);
		conn.open(url);
		conn.setRequestProperty("User-Agent", userAgent);
		//
		return conn;
	}
	
	/**
	 * @param url
	 * @return
	 */
	public static String encodeURL(String url) {
		if (url == null) {
			return null;
		}
		//
		StringBuffer eURL = new StringBuffer(url.length());
		char[] urlChars = url.toCharArray();
		//
		for (int i = 0; i < urlChars.length; i++) {
			switch (urlChars[i]) {
			case '!':
				eURL.append("%21");
				break;
			case '*':
				eURL.append("%2A");
				break;
			case '"':
				eURL.append("%22");
				break;
			case '\'':
				eURL.append("%27");
				break;
			case '(':
				eURL.append("%28");
				break;
			case ')':
				eURL.append("%29");
				break;
			case ';':
				eURL.append("%3B");
				break;
//			case ':':
//				eURL.append("%3A");
//				break;
			case '@':
				eURL.append("%40");
				break;
//			case '&':
//				eURL.append("%26");
//				break;
//			case '=':
//				eURL.append("%3D");
//				break;
			case '+':
				eURL.append("%2B");
				break;
			case '$':
				eURL.append("%24");
				break;
			case ',':
				eURL.append("%2C");
				break;
//			case '/':
//				eURL.append("%2F");
//				break;
//			case '?':
//				eURL.append("%3F");
//				break;
			case '%':
				eURL.append("%25");
				break;
			case '#':
				eURL.append("%23");
				break;
			case '[':
				eURL.append("%5B");
				break;
			case ']':
				eURL.append("%5D");
				break;
			case ' ':
				eURL.append("%20");
				break;
			default:
				eURL.append(urlChars[i]);
				break;
			}
		}
		//
		return eURL.toString();
	}

	/**
	 * 
	 */
	HttpConnector() {
	}
}