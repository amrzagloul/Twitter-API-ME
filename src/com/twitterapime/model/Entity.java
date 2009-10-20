/*
 * Entity.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

import java.util.Date;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface Entity {
	/**
	 * @param key
	 * @return
	 */
	public Object[] getArray(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Date getDate(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key);
}