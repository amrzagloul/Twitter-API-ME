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
	 * Create an instance of List class.
	 * </p>
	 * @param id Id.
	 * @param name Name.
	 * @param isPublic Mode: Public (true) or private (false).
	 * @param description Description.
	 */
	public List(String id, String name, boolean isPublic, String description) {
		Hashtable t = new Hashtable(4);
		if (!StringUtil.isEmpty(id)) {
			t.put(MetadataSet.LIST_ID, id);
		}
		if (!StringUtil.isEmpty(name)) {
			t.put(MetadataSet.LIST_NAME, name);
		}
		t.put(MetadataSet.LIST_MODE, isPublic ? "public" : "private");
		if (!StringUtil.isEmpty(description)) {
			t.put(MetadataSet.LIST_DESCRIPTION, description.trim());
		}
		//
		setData(t);
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
