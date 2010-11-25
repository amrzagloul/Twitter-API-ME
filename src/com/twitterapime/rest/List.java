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

/**
 * <p>
 * This class defines an entity that represents a List.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.6
 */
public class List extends DefaultEntity {
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
	 * Get the user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getUserAccount() {
		return (UserAccount)data.get(MetadataSet.LIST_USER_ACCOUNT);
	}
}
