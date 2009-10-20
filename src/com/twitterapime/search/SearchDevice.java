/*
 * SearchDevice.java
 * 23/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.io.IOException;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpConnector;
import com.twitterapime.io.HttpResponseCodeHandler;
import com.twitterapime.parser.Feed;
import com.twitterapime.parser.FeedEntry;
import com.twitterapime.parser.FeedParser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.parser.FeedParserListener;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class SearchDevice {
	/**
	 * 
	 */
	static final String TWITTER_URL_ATOM = "http://search.twitter.com/search.atom";

	/**
	 * 
	 */
	static final String TWITTER_QUERY_STRING_PREFIX = "q=";

	/**
	 * 
	 */
	static SearchDevice device;
	
	/**
	 * 
	 */
	static long lastCallTime;
	
	/**
	 * 
	 */
	static int apiCallsCount;

	/**
	 * @return
	 */
	public static SearchDevice getInstance() {
		if (device == null) {
			device = new SearchDevice();
		}
		//
		return device;
	}
	
	/**
	 * 
	 */
	SearchDevice() {
	}

	/**
	 * @param query
	 * @return
	 * @throws IOException
	 * @throws LimitExceededException
	 */
	public Tweet[] searchTweets(Query query) throws IOException,
			LimitExceededException {
		return searchTweets(query.toString(), null);
	}

	/**
	 * @param queryString
	 * @return
	 * @throws IOException
	 * @throws LimitExceededException
	 */
	public Tweet[] searchTweets(String queryString) throws IOException,
			LimitExceededException {
		return searchTweets(queryString, null);
	}
	
	/**
	 * @param query
	 * @param listener
	 */
	public void startSearchTweets(Query query, SearchDeviceListener listener) {
		startSearchTweets(query.toString(), listener);
	}

	/**
	 * @param queryString
	 * @param listener
	 */
	public void startSearchTweets(final String queryString,
			final SearchDeviceListener listener) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					searchTweets(queryString, listener);
				} catch (Exception e) {
					if (listener != null) {
						listener.searchFailed(e);
					}
				}
			}
		};
		//
		Thread t = new Thread(r);
		t.start();
	}

	/**
	 * @return
	 */
	public int getAPICallsCount() {
		return apiCallsCount;
	}

	/**
	 * @return
	 */
	public long getLastAPICallTime() {
		return lastCallTime;
	}
	
	/**
	 * @param queryString
	 * @param lstnr
	 * @return
	 * @throws IOException
	 * @throws LimitExceededException
	 */
	Tweet[] searchTweets(String queryString, final SearchDeviceListener lstnr)
			throws IOException, LimitExceededException {
		updateAPIInfo();
		//
		HttpConnection conn = getHttpConn(queryString);
		FeedParser parser = ParserFactory.getDefaultFeedParser();
		//
		if (lstnr != null) {
			parser.setFeedParserListener(new FeedParserListener() {
				public void feedParsed(Feed feed) {
					lstnr.searchCompleted();
				}

				public void feedEntryParsed(FeedEntry entry) {
					lstnr.tweetFound((Tweet) entry);
				}
			});
		}
		//
		try {
			parser.parse(conn.openInputStream());
			//
			Feed f = parser.getFeed();
			Tweet[] ts = (Tweet[]) f.getEntries();
			//
			return ts;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * @param queryStr
	 * @return
	 * @throws IOException
	 * @throws LimitExceededException
	 */
	HttpConnection getHttpConn(String queryStr) throws IOException,
			LimitExceededException {
		if (queryStr == null || (queryStr = queryStr.trim()).length() == 0) {
			throw new IllegalArgumentException(
					"Query String cannot be empty/null.");
		}
		//
		if (!queryStr.startsWith("?")) {
			if (!queryStr.startsWith(TWITTER_QUERY_STRING_PREFIX)) {
				queryStr = '?' + TWITTER_QUERY_STRING_PREFIX + queryStr;
			} else {
				queryStr = '?' + queryStr;
			}
		}
		//
		final String url = TWITTER_URL_ATOM + HttpConnector.encodeURL(queryStr);
		HttpConnection c = HttpConnector.open(url);
		c.setRequestMethod(HttpConnection.GET);
		//
		HttpResponseCodeHandler.handleSearchAPICodes(c); //verify whether there is an error in the request.
		//
		return c;
	}
	
	/**
	 * 
	 */
	void updateAPIInfo() {
		lastCallTime = System.currentTimeMillis();
		apiCallsCount++;
	}
}