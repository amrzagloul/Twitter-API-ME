/*
 * HttpConnection.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface HttpConnection {
	/**
	 * 
	 */
	public static final String GET = "GET";
	
	/**
	 * 
	 */
	public static final String POST = "POST";

	/**
	 * 
	 */
	public static final int HTTP_OK = 200;
	
	/**
	 * 
	 */
	public static final int HTTP_FORBIDDEN = 403;

	/**
	 * 
	 */
	public static final int HTTP_UNAVAILABLE = 503;
	
	/**
	 * 
	 */
	public static final int HTTP_NOT_MODIFIED = 304;

	/**
	 * 
	 */
	public static final int HTTP_BAD_REQUEST = 400;
	
	/**
	 * 
	 */
	public static final int HTTP_UNAUTHORIZED = 401;

	/**
	 * 
	 */
	public static final int HTTP_NOT_FOUND = 404;
	
	/**
	 * 
	 */
	public static final int HTTP_NOT_ACCEPTABLE = 406;

	/**
	 * 
	 */
	public static final int HTTP_INTERNAL_ERROR = 500;

	/**
	 * 
	 */
	public static final int HTTP_BAD_GATEWAY  = 502;

	/**
	 * @param url
	 * @throws IOException
	 */
	public void open(String url) throws IOException;
	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * @return
	 * @throws IOException
	 */
	public int getResponseCode() throws IOException;

	/**
	 * @return
	 * @throws IOException
	 */
	public InputStream openInputStream() throws IOException;

	/**
	 * @return
	 * @throws IOException
	 */
	public OutputStream openOutputStream() throws IOException;

	/**
	 * @param method
	 * @throws IOException
	 */
	public void setRequestMethod(String method) throws IOException;

	/**
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setRequestProperty(String key, String value) throws IOException;
	
	/**
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public String getHeaderField(String name) throws IOException;
}