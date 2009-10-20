/*
 * FeedParserListener.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface FeedParserListener {
	/**
	 * 
	 * @param feed
	 */
	public void feedParsed(Feed feed);

	/**
	 * 
	 * @param entry
	 */
	public void feedEntryParsed(FeedEntry entry);
}