/*
 * FeedParser.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class FeedParser {
	/**
	 * 
	 */
	protected Feed feed;

	/**
	 * 
	 */
	protected FeedParserListener listener;

	/**
	 * @return
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * 
	 * @param listener
	 */
	public void setFeedParserListener(FeedParserListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	 * @param stream
	 * @exception ParserException
	 * @exception IOException
	 */
	public abstract void parse(InputStream stream) throws IOException,
		ParserException;

	/**
	 * 
	 * @param feed
	 */
	protected void fireFeedParsed(Feed feed) {
		if (listener != null) {
			listener.feedParsed(feed);
		}
	}

	/**
	 * 
	 * @param entry
	 */
	protected void fireFeedEntryParsed(FeedEntry entry) {
		if (listener != null) {
			listener.feedEntryParsed(entry);
		}
	}
}