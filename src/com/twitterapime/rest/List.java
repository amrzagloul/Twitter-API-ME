/*
 * List.java
 * 23/11/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines an entity that represents a List.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.6
 */
public final class List extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of List class.
	 * </p>
	 */
	public List() {
	}

	/**
	 * <p>
	 * Create an instance of List class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public List(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Create an instance of List class. Use this constructor when you intend
	 * to create a new list.
	 * </p>
	 * @param name Name.
	 * @param isPublic Mode: Public (true) or private (false).
	 * @param description Description.
	 * @throws IllegalArgumentException If name is null/empty.
	 */
	public List(String name, boolean isPublic, String description) {
		Hashtable t = new Hashtable(4);
		if (StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("Name must not be null/empty.");
		}
		//
		t.put(MetadataSet.LIST_NAME, name);
		t.put(MetadataSet.LIST_MODE, isPublic ? "public" : "private");
		if (!StringUtil.isEmpty(description)) {
			t.put(MetadataSet.LIST_DESCRIPTION, description.trim());
		}
		//
		setData(t);
	}
	
	/**
	 * <p>
	 * Create an instance of List class. Use this constructor when you intend
	 * to update an existent list.
	 * </p>
	 * @param name Name.
	 * @param isPublic Mode: Public (true) or private (false).
	 * @param description Description.
	 * @throws IllegalArgumentException If id/name is null/empty.
	 */
	public List(String id, String name, boolean isPublic, String description) {
		this(name, isPublic, description);
		//
		if (StringUtil.isEmpty(id)) {
			throw new IllegalArgumentException("ID must not be null/empty.");
		}
		//
		data.put(MetadataSet.LIST_ID, id);
	}

	/**
	 * <p>
	 * Get the user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getUserAccount() {
		return (UserAccount)data.get(MetadataSet.LIST_USER_ACCOUNT);
	}
}
