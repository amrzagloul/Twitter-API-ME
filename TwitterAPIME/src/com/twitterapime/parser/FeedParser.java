/*
 * FeedParser.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This class defines the parser responsible for parsing the content of a web
 * feed. Web feed is one of the formats supported by Twitter API to return
 * information.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see Feed
 * @see FeedParserListener
 */
public abstract class FeedParser implements Parser {
	/**
	 * <p>
	 * Feed object that contains the feed information.
	 * </p>
	 */
	protected Feed feed;

	/**
	 * <p>
	 * FeedParserListener object registered to be notified about the parser's
	 * events.
	 * </p>
	 */
	protected FeedParserListener listener;

	/**
	 * <p>
	 * Get the feed object with the feed information. This method returns
	 * <code>null</code> until the parser has completed the document parsing.
	 * </p>
	 * @return Feed object.
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * <p>
	 * Set a FeedParserListener object to listen to the parser's events.
	 * </p>
	 * @param listener The FeedParserListener object.
	 */
	public void setFeedParserListener(FeedParserListener listener) {
		this.listener = listener;
	}

	/**
	 * <p>
	 * Notify the registered listener when the parsing is completed.
	 * </p>
	 * @param feed The feed object.
	 */
	protected void fireFeedParsed(Feed feed) {
		if (listener != null) {
			listener.feedParsed(feed);
		}
	}

	/**
	 * <p>
	 * Notify the registered listener when the parsing of an entry from the feed
	 * is completed.
	 * </p>
	 * @param entry The feed entry object.
	 */
	protected void fireFeedEntryParsed(FeedEntry entry) {
		if (listener != null) {
			listener.feedEntryParsed(entry);
		}
	}
}