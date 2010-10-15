/*
 * JSONHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This interface defines a JSON document handler.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public interface JSONHandler extends Handler {
	/**
	 * <p>
	 * Callback method called to notify to start document parsing.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @throws ParserException If a parser error occurs.
	 */
	public void handle(JSONObject jsonObj) throws ParserException;
	
	/**
	 * <p>
	 * Check whether the given JSONObject contains a member value.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @param key Key.
	 * @return Member (true).
	 * @throws ParserException If a parser error occurs.
	 */
	public boolean isMember(JSONObject jsonObj, String key)
		throws ParserException;
	
	/**
	 * <p>
	 * Check whether the given JSONObject contains an array.
	 * </p>
	 * @param jsonObj JSONObject.
	 * @param key Key.
	 * @return Array (true).
	 * @throws ParserException If a parser error occurs.
	 */
	public boolean isArray(JSONObject jsonObj, String key)
		throws ParserException;
}
