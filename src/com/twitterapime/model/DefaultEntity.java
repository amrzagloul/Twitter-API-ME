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

import com.twitterapime.rest.Credential;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.List;
import com.twitterapime.rest.RateLimitStatus;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Topic;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class provides a basic implementation of Entity interface.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.5
 * @since 1.0
 * @see Tweet
 * @see Credential
 * @see RateLimitStatus
 * @see UserAccount
 * @see GeoLocation
 * @see Topic
 * @see TweetEntity
 * @see List
 */
//#ifdef PP_ANDROID
//@public class DefaultEntity implements Entity, java.io.Serializable {
//@	/**
//@	 * <p>
//@	 * Serial UID.
//@	 * </p>
//@	 */
//@	private static final long serialVersionUID = 5303650046011041495L;
//#else
	public class DefaultEntity implements Entity {
//#endif
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
		setData((Hashtable)null);
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
	public final void setData(Hashtable data) {
		if (data == null) {
			data = new Hashtable();
		}
		this.data = data;
	}
	
	/**
	 * <p>
	 * Set the given entity's data to the object.
	 * </p>
	 * @param e Entity.
	 */
	public final void setData(DefaultEntity e) {
		setData(e.data);
	}
	
	/**
	 * @see com.twitterapime.model.Entity#getArray(java.lang.String)
	 */
	public final Object[] getArray(String attr) {
		return (Object[])getValue(attr, new Object[]{""}.getClass());
	}
	
	/**
	 * @see com.twitterapime.model.Entity#getDate(java.lang.String)
	 */
	public final Date getDate(String attr) {
		return (Date)getValue(attr, new Date().getClass());
	}

	/**
	 * @see com.twitterapime.model.Entity#getInt(java.lang.String)
	 */
	public final int getInt(String attr) {
		Object v = (Integer)getValue(attr, new Integer(0).getClass());
		return v != null ? ((Integer)v).intValue() : Integer.MIN_VALUE;
	}

	/**
	 * @see com.twitterapime.model.Entity#getLong(java.lang.String)
	 */
	public final long getLong(String attr) {
		Object v = getObject(attr);
		if (v instanceof Date) {
			return ((Date)v).getTime();
		} else {
			v = (Long)getValue(attr, new Long(0).getClass());
			return v != null ? ((Long)v).longValue() : Long.MIN_VALUE;
		}
	}

	/**
	 * @see com.twitterapime.model.Entity#getObject(java.lang.String)
	 */
	public final Object getObject(String attr) {
		return data.get(attr);
	}

	/**
	 * @see com.twitterapime.model.Entity#getString(java.lang.String)
	 */
	public final String getString(String attr) {
		Object v = data.get(attr);
		return v != null ? v.toString() : null;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof DefaultEntity)) {
			return false;
		} else {
			return areEquals(data, ((DefaultEntity)o).data);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode() {
		return toString().hashCode(); //TODO: find a better way to generate this hash code.
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
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
	 * Return the number of entries in the entity.
	 * @return Number.
	 */
	public final int size() {
		return data.size();
	}
	
	/**
	 * <p>
	 * Checks if the value associated to the key is null or empty string.
	 * </p>
	 * @param key Key.
	 * @return Empty (true).
	 */
	public boolean isEmpty(String key) {
		Object v = data.get(key);
		//
		return v == null || StringUtil.isEmpty(v.toString());
	}

	/**
	 * <p>
	 * Checks if the value associated to the key is null.
	 * </p>
	 * @param key Key.
	 * @return Null (true).
	 */
	public boolean isNull(String key) {
		return data.get(key) == null;
	}
	
	/**
	 * <p>
	 * Checks if the value associated to the is null or empty.
	 * </p>
	 * @param key Key.
	 * @throws IllegalArgumentException If key's value is empty.
	 */
	public void checkEmpty(String key) {
		if (isEmpty(key)) {
			throw new IllegalArgumentException(key + " must not be empty.");
		}
	}
	
	/**
	 * Check whether the given hashtables contain the same pair of keys/values.
	 * @param h1 Hashtable 1.
	 * @param h2 Hashtable 2.
	 * @return Equals (true).
	 */
	private boolean areEquals(Hashtable h1, Hashtable h2) {
		if (h1.size() != h2.size()) {
			return false;
		}
		//
		Enumeration keys = h1.keys();
		Object key;
		Object val1;
		Object val2;
		//
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			//
			val1 = h1.get(key);
			val2 = h2.get(key);
			//
			if (val1 instanceof Object[] && val2 instanceof Object[]) {
				Object[] aval1 = (Object[])val1;
				Object[] aval2 = (Object[])val2;
				//
				if (aval1.length != aval2.length) {
					return false;
				} else {
					for (int i = 0; i < aval1.length; i++) {
						if (!aval1[i].equals(aval2[i])) {
							return false;
						}
					}
				}
			} else if (!h1.get(key).equals(h2.get(key))) {
				return false;
			}
		}
		//
		return true;
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
