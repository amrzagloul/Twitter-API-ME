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
 * This interface defines the necessary methods for a listener that wants to be
 * notified of the events from a FeedParser object. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see FeedParser
 */
public interface FeedParserListener {
	/**
	 * <p>
	 * This method is called when the feed is completely parsed.
	 * </p>
	 * @param feed The object that contains the feed information.
	 */
	public void feedParsed(Feed feed);

	/**
	 * <p>
	 * This method is called when the feed entry is parsed. There may be many
	 * feed entries in a feed, so this method may be called many times.
	 * </p>
	 * @param entry The object that contains the feed entry information.
	 */
	public void feedEntryParsed(FeedEntry entry);
}