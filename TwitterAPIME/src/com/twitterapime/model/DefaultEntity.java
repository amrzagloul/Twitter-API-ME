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

import com.twitterapime.search.Tweet;

/**
 * <p>
 * This class provides a basic implementation of Entity interface.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see Tweet
 */
public class DefaultEntity implements Entity {
	/**
	 * <p>
	 * Hashtable object that stores the attributes/values.
	 * </p>
	 */
	protected Hashtable data;

	/**
	 * <p>
	 * Create an instance of DefaultEntity class.
	 * </p>
	 */
	public DefaultEntity() {
	}

	/**
	 * <p>
	 * Create an instance of DefaultEntity class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public DefaultEntity(Hashtable data) {
		setData(data);
	}

	/**
	 * <p>
	 * Set a new pack of attributes/values.
	 * </p>
	 * @param data Pack of attributes/values.
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
	public Object[] getArray(String attr) {
		return (Object[])getValue(attr, new Object[]{""}.getClass());
	}
	
	/**
	 * @see com.twitterapime.model.Entity#getDate(java.lang.String)
	 */
	public Date getDate(String attr) {
		return (Date)getValue(attr, new Date().getClass());
	}

	/**
	 * @see com.twitterapime.model.Entity#getInt(java.lang.String)
	 */
	public int getInt(String attr) {
		Object v = (Integer)getValue(attr, new Integer(0).getClass());
		return v != null ? ((Integer)v).intValue() : Integer.MIN_VALUE;
	}

	/**
	 * @see com.twitterapime.model.Entity#getLong(java.lang.String)
	 */
	public long getLong(String attr) {
		Object v = (Long)getValue(attr, new Long(0).getClass());
		return v != null ? ((Long)v).longValue() : Long.MIN_VALUE;
	}

	/**
	 * @see com.twitterapime.model.Entity#getObject(java.lang.String)
	 */
	public Object getObject(String attr) {
		return data.get(attr);
	}

	/**
	 * @see com.twitterapime.model.Entity#getString(java.lang.String)
	 */
	public String getString(String attr) {
		Object v = data.get(attr);
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
	 * <p>
	 * Return the value of a given attribute according to the type.
	 * </p>
	 * @param attr The attribute.
	 * @param type The attribute's type.
	 * @throws ClassCastException If the given attribute is of a different type.
	 */
	private Object getValue(String attr, Class type) {
		Object v = data.get(attr);
		if (v != null && !type.isInstance(v)) {
			throw new ClassCastException(
				"Invalid type value: " + v.getClass().getName());
		}
		//
		return v;
	}
}