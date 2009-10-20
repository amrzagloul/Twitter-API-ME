/*
 * LimitExceededException.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class LimitExceededException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LimitExceededException() {}

	/**
	 * 
	 * @param msg
	 */
	public LimitExceededException(String msg) {
		super(msg);
	}
}