/*
 * ErrorMessageParser.java
 * 03/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This class defines the parser responsible for parsing the error messages from
 * Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class ErrorMessageParser implements Parser {
	/**
	 * <p>
	 * Parsed error message.
	 * </p>
	 */
	protected String error;

	/**
	 * <p>
	 * Parsed request message.
	 * </p>
	 */
	protected String request;

	/**
	 * <p>
	 * Get the parsed error message.
	 * </p>
	 * @return The error message.
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * <p>
	 * Get the parsed request message.
	 * </p>
	 * @return The request message.
	 */
	public String getRequest() {
		return request;
	}
}