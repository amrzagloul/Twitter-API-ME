/*
 * Feed.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import com.twitterapime.model.Entity;

/**
 * <p>
 * This interface defines the necessary methods for an entity that represents a
 * web feed. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see FeedEntry
 */
public interface Feed extends Entity {
	/**
	 * <p>
	 * Add a feed entry object into the feed.
	 * </p>
	 * @param entry The entry object.
	 */
	public void addEntry(FeedEntry entry);
	
	/**
	 * <p>
	 * Get all the feed entry objects contained in the feed.
	 * </p>
	 * @return All feed entries.
	 */
	public FeedEntry[] getEntries();
}