/*
 * DefaultEntity.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DefaultEntity implements Entity {
	/**
	 * 
	 */
	protected Hashtable data;

	/**
	 * 
	 */
	public DefaultEntity() {
	}

	/**
	 * 
	 * @param data
	 */
	public DefaultEntity(Hashtable data) {
		setData(data);
	}

	/**
	 * 
	 * @param data
	 * @throws NullPointerException Data cannot be null.
	 */
	public void setData(Hashtable data) {
		if (data == null) {
			data = new Hashtable();
		}
		this.data = data;
	}

	/**
	 * @see com.twitterapime.model.Entity#getArray(java.lang.String)
	 */
	public Object[] getArray(String key) {
		return (Object[])getValue(key, new Object[]{""}.getClass());
	}
	
	/**
	 * @see com.twitterapime.model.Entity#getDate(java.lang.String)
	 */
	public Date getDate(String key) {
		return (Date)getValue(key, new Date().getClass());
	}

	/**
	 * @see com.twitterapime.model.Entity#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		Object v = (Integer)getValue(key, new Integer(0).getClass());
		return v != null ? ((Integer)v).intValue() : Integer.MIN_VALUE;
	}

	/**
	 * @see com.twitterapime.model.Entity#getLong(java.lang.String)
	 */
	public long getLong(String key) {
		Object v = (Long)getValue(key, new Long(0).getClass());
		return v != null ? ((Long)v).longValue() : Long.MIN_VALUE;
	}

	/**
	 * @see com.twitterapime.model.Entity#getObject(java.lang.String)
	 */
	public Object getObject(String key) {
		return data.get(key);
	}

	/**
	 * @see com.twitterapime.model.Entity#getString(java.lang.String)
	 */
	public String getString(String key) {
		Object v = data.get(key);
		return v != null ? v.toString() : null;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (data == null) {
			return null;
		}
		//
		StringBuffer s = new StringBuffer();
		Enumeration keys = data.keys();
		String key;
		//
		while (keys.hasMoreElements()) {
			key = keys.nextElement().toString();
			s.append(key + ": " + data.get(key) + "\n");
		}
		//
		return s.toString();
	}

	/**
	 * @param key
	 * @param type
	 * @throws ClassCastException Invalid type value.
	 */
	private Object getValue(String key, Class type) {
		Object v = data.get(key);
		if (v != null && !type.isInstance(v)) {
			throw new ClassCastException(
				"Invalid type value: " + v.getClass().getName());
		}
		//
		return v;
	}
}