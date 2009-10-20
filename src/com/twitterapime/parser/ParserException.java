/*
 * ParserException.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * </p>
 * 
 * @since 1.0
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @created 14-Aug-2009 9:36:00 PM
 */
public class ParserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ParserException() {

	}

	/**
	 * @param msg
	 */
	public ParserException(String msg) {
		super(msg);
	}
}