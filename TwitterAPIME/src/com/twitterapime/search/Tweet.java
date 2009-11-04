/*
 * Tweet.java
 * 03/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.parser.FeedEntry;

/**
 * <p>
 * This class defines an entity that represents a Tweet. A tweet is a message
 * sent by an user to Twitter.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see SearchDevice
 */
public final class Tweet extends DefaultEntity implements FeedEntry {
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 */
	public Tweet() {
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public Tweet(Hashtable data) {
		super(data);
	}
}