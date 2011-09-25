/*
 * Friendship.java
 * 16/05/2011
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
 * This class defines an entity that represents the details of a friendship
 * between two users.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.7
 */
public final class Friendship extends DefaultEntity {
//#ifdef PP_ANDROID
//@	/**
//@	 * <p>
//@	 * Serial UID.
//@	 * </p>
//@	 */
//@	private static final long serialVersionUID = 585342660722380914L;
//#endif

	/**
	 * <p>
	 * Create an instance of Friendship class.
	 * </p>
	 */
	public Friendship() {
	}

	/**
	 * <p>
	 * Create an instance of Friendship class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public Friendship(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Get the source relationship.
	 * </p>
	 * @return Friendship.
	 */
	public Friendship getSource() {
		return (Friendship)getObject(MetadataSet.FRIENDSHIP_SOURCE);
	}

	/**
	 * <p>
	 * Get the target relationship.
	 * </p>
	 * @return Friendship.
	 */
	public Friendship getTarget() {
		return (Friendship)getObject(MetadataSet.FRIENDSHIP_TARGET);
	}
}
