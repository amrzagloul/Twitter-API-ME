/*
 * JSONArray.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This class defines an ordered sequence of values in a JSON document.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public interface JSONArray {
	/**
	 * <p>
	 *  Get the object value associated with an index.
	 * </p>
	 * @param index Index.
	 * @return Object.
	 */
	public Object get(int index);
	
	/**
	 * <p>
	 * Get the boolean value associated with an index.
	 * </p>
	 * @param index Index.
	 * @return Boolean.
	 */
	public boolean getBoolean(int index);
	
	/**
	 * <p>
	 * Get the JSONArray associated with an index.
	 * </p>
	 * @param index Index.
	 * @return JSONArray.
	 */
	public JSONArray getJSONArray(int index);
	
	/**
	 * <p>
	 * Get the JSONObject associated with an index.
	 * </p>
	 * @param index Index.
	 * @return JSONObject.
	 */
	public JSONObject getJSONObject(int index);
	
	/**
	 * <p>
	 * Get the string associated with an index.
	 * </p>
	 * @param index Index.
	 * @return String.
	 */
	public String getString(int index);
	
	/**
	 * <p>
	 * Determine if the value is null.
	 * </p>
	 * @param index Index.
	 * @return Null (true).
	 */
	public boolean isNull(int index);
	
	/**
	 * <p>
	 * Get the number of elements in the JSONArray, included nulls.
	 * </p>
	 * @return Length.
	 */
	public int length();
}
