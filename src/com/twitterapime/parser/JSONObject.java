/*
 * JSONObject.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.util.Enumeration;

/**
 * <p>
 * This class defines an unordered collection of name/value pairs of a JSON 
 * document.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public interface JSONObject {
	/**
	 * <p>
	 * Get the value object associated with a key.
	 * </p>
	 * @param key Key.
	 * @return Object.
	 */
	public Object get(String key);
	
	/**
	 * <p>
	 * Get the boolean value associated with a key.
	 * </p>
	 * @param key Key.
	 * @return Boolean.
	 */
	public boolean getBoolean(String key);
	
	/**
	 * <p>
	 * Get the int value associated with a key.
	 * </p>
	 * @param key Key.
	 * @return Int.
	 */
	public int getInt(String key);
	
	/**
	 * <p>
	 * Get the JSONArray value associated with a key.
	 * </p>
	 * @param key Key.
	 * @return JSONArray.
	 */
	public JSONArray getJSONArray(String key);
	
	/**
	 * <p>
	 * Get the JSONObject value associated with a key.
	 * </p>
	 * @param key Key.
	 * @return JSONObject.
	 */
	public JSONObject getJSONObject(String key);
	
	/**
	 * <p>
	 * Get the long value associated with a key.
	 * </p>
	 * @param key Key.
	 * @return Long.
	 */
	public long getLong(String key);
	
	/**
	 * <p>
	 * Get the string associated with a key.
	 * </p>
	 * @param key Key.
	 * @return String.
	 */
	public String getString(String key);
	
	/**
	 * <p>
	 * Determine if the JSONObject contains a specific key.
	 * </p>
	 * @param key Key.
	 * @return Has (true).
	 */
	public boolean has(String key);
	
	/**
	 * <p>
	 * Determine if the value associated with the key is null or if there is no value.
	 * </p>
	 * @param key Key.
	 * @return Null (true).
	 */
	public boolean isNull(String key);
	
	/**
	 * <p>
	 * Get an enumeration of the keys of the JSONObject.
	 * </p>
	 * @return Keys.
	 */
	public Enumeration keys();
	
	/**
	 * <p>
	 * Get the number of keys stored in the JSONObject.
	 * </p>
	 * @return Length.
	 */
	public int length();
}
