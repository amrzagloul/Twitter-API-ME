/*
 * HttpResponse.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class HttpResponse {
	/**
	 * 
	 */
	private int code;
	
	/**
	 * 
	 */
	private String body;
	
	/**
	 * 
	 */
	private InputStream stream;

	/**
	 * @param conn
	 * @throws IOException
	 */
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		stream = conn.openInputStream();
	}

	/**
	 * @return
	 */
	public boolean wasSuccessful() {
		return code >= 200 && code < 400;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getBodyContent() throws IOException {
		return body != null ? body : (body = parseBody(stream));
	}
	
	/**
	 * @return
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String parseBody(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		//
		for (int n; (n = in.read(buffer)) > 0;) {
			out.write(buffer, 0, n);
		}
		//
		try {
			return new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e.getMessage());
		}
	}
}