/*
 * ErrorMessageParser.java
 * 03/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class ErrorMessageParser {
	/**
	 * 
	 */
	protected String error;

	/**
	 * 
	 */
	protected String request;

	/**
	 * @return
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * @return
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * 
	 * @param stream
	 * @exception ParserException
	 * @exception IOException
	 */
	public abstract void parse(InputStream stream) throws IOException,
		ParserException;
}