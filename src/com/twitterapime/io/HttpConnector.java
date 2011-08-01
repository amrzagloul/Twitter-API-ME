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
 * This is factory class for creating new HttpConnection objects.
 * </p>
 * <p>
 * The creation of HttpConnection is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * <p>
 * The parameter string that describes the target should conform to the Http URL
 * format as described in RFC 1738.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.6
 * @since 1.0
 * @see HttpConnection
 */
public final class HttpConnector {
	/**
	 * <p>
	 * Create and open a HttpConnection.
	 * </p>
	 * 
	 * @param url The URL for the connection.
	 * @return A new HttpConnection object.
	 * @throws IOException If an I/O error occurs.
	 * @throws IllegalArgumentException If url is null or empty.
	 */
	public static HttpConnection open(String url) throws IOException {
		if (url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("URL must not be null/empty.");
		}
		//
		final long PPID = PlatformProviderSelector.getCurrentProvider().getID();
		//
		HttpConnection conn = null;
		String userAgent = null;
		//
		//#ifdef PP_JAVA_ME
		if (PPID == PlatformProvider.PPID_JAVA_ME) {
			conn = new impl.javame.com.twitterapime.io.HttpConnectionImpl();
			userAgent =
				"Twitter API ME/1.8 (compatible; Java ME; MIDP-2.0; CLDC-1.0)";
		}
		//#else
//@		//
		//#ifdef PP_RIM
//@		if (PPID == PlatformProvider.PPID_RIM) {
//@			conn = new impl.rim.com.twitterapime.io.HttpConnectionImpl();
//@			userAgent =
//@				"Twitter API ME/1.8 " +
//@				"(compatible; Java ME; MIDP-2.0; CLDC-1.0; RIM OS 4.6)";
//@		}
		//#else
//@		//
		//#ifdef PP_ANDROID
//@		if (PPID == PlatformProvider.PPID_ANDROID) {
//@			conn = new impl.android.com.twitterapime.io.HttpConnectionImpl();
//@			userAgent = "Twitter API ME/1.8 (compatible; Android 1.5)";
//@		}
		//#endif
		//#endif		
		//#endif
		//
		if (conn == null) {
			throw new IllegalArgumentException("Unknown platform ID: " + PPID);
		}
		//
		conn.open(url);
		conn.setRequestProperty("User-Agent", userAgent);
		//
		return conn;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private HttpConnector() {
	}
}
