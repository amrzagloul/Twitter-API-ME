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
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Tweet extends DefaultEntity implements FeedEntry {
	/**
	 * 
	 */
	public Tweet() {
	}
	
	/**
	 * 
	 * @param data
	 */
	public Tweet(Hashtable data) {
		super(data);
	}
}