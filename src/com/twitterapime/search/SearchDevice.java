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
import com.twitterapime.parser.FeedParserListener;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;

/**
 * <p>
 * This class is the entry point of Search API, which defines the methods
 * responsible for submitting a query to Twitter Search API.
 * </p>
 * <p>
 * <pre>
 * SearchDevice sd = SearchDevice.getInstance();
 * Query q1 = QueryComposer.from("twitteruser");
 * Query q2 = QueryComposer.containAny("search api");
 * Query q = QueryComposer.append(q1, q2);
 * Tweet[] ts = sd.searchTweets(q);
 * for (int i = 0; i < ts.length; i++) {
 *   list.append(ts[i].getString(MetadataSet.TWEET_CONTENT), null);
 * }
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see SearchDeviceListener
 * @see QueryComposer
 */
public final class SearchDevice {
	/**
	 * <p>
	 * Twitter Search API URI.
	 * </p>
	 */
	static final String TWITTER_URL_ATOM = "http://search.twitter.com/search.atom";

	/**
	 * <p>
	 * Query prefix.
	 * </p>
	 */
	static final String TWITTER_QUERY_STRING_PREFIX = "q=";

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	static SearchDevice device;
	
	/**
	 * <p>
	 * Time at which Twitter Search API was access by this class.
	 * </p>
	 */
	static long lastCallTime;
	
	/**
	 * <p>
	 * Number of calls to Twitter Search API since this class was loaded.
	 * </p>
	 */
	static int apiCallsCount;

	/**
	 * <p>
	 * Get an instance of SearchDevice class.
	 * </p>
	 * @return A SearchDevice object.
	 */
	public static SearchDevice getInstance() {
		if (device == null) {
			device = new SearchDevice();
		}
		//
		return device;
	}
	
	/**
	 * <p>
	 * Package-protected constructor to avoid object instantiation.
	 * </p>
	 */
	SearchDevice() {
	}

	/**
	 * <p>
	 * Search for tweets that match the given query. This method gets blocked
	 * until the search is completed or an exception is thrown.
	 * </p>
	 * @param query The query.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	public Tweet[] searchTweets(Query query) throws IOException,
			LimitExceededException {
		return searchTweets(query, null);
	}

	/**
	 * <p>
	 * Search for tweets that match the given query string. This method gets
	 * blocked until the search is completed or an exception is thrown.
	 * </p>
	 * @param queryString The query string.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	public Tweet[] searchTweets(String queryString) throws IOException,
			LimitExceededException {
		return searchTweets(new Query(queryString), null);
	}
	
	/**
	 * <p>
	 * Search for tweets that match the given query. This method does not wait
	 * for the search process is completed to return. To have access to this
	 * search's result, a SearchDeviceListener object must be registered. 
	 * </p>
	 * @param query The query.
	 * @param listener Listener object to be notified about the search's result.
	 */
	public void startSearchTweets(Query query, SearchDeviceListener listener) {
		startSearchTweets(query.toString(), listener);
	}

	/**
	 * <p>
	 * Search for tweets that match the given query string. This method does not
	 * wait for the search process is completed to return. To have access to
	 * this search's result, a SearchDeviceListener object must be registered. 
	 * </p>
	 * @param queryString The query string.
	 * @param listener Listener object to be notified about the search's result.
	 */
	public void startSearchTweets(final String queryString,
			final SearchDeviceListener listener) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					searchTweets(new Query(queryString), listener);
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
	 * <p>
	 * Get the call count submitted to Twitter Search API.
	 * </p>
	 * @return Call count.
	 */
	public int getAPICallsCount() {
		return apiCallsCount;
	}

	/**
	 * <p>
	 * Get the time at which the last call was submitted to Twitter Search API.
	 * </p>
	 * @return Time of last call.
	 */
	public long getLastAPICallTime() {
		return lastCallTime;
	}
	
	/**
	 * <p>
	 * Search for tweets that match the given query. If the listener parameter
	 * is not passed, this method gets blocked until the search is completed or
	 * an exception is thrown. Otherwise, the result is returned through the
	 * listener.
	 * </p>
	 * @param queryString The query string.
	 * @param lstnr The listener object.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	Tweet[] searchTweets(Query query, final SearchDeviceListener lstnr)
			throws IOException, LimitExceededException {
		updateAPIInfo();
		//
		HttpConnection conn = getHttpConn(query.toString());
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
		} finally {
			conn.close();
		}
	}
	
	/**
	 * <p>
	 * Get a Http connection to the given query string.
	 * </p>
	 * @param queryStr The query string.
	 * @return The Http connection object.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
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
		boolean hasException = true;
		//
		try {
			c.setRequestMethod(HttpConnection.GET);
			//verify whether there is an error in the request.
			HttpResponseCodeHandler.handleSearchAPICodes(c);
			hasException = false;
		} finally {
			if (hasException) {
				c.close();
			}
		}
		//
		return c;
	}
	
	/**
	 * <p>
	 * Update some internal information regarding the API.
	 * </p>
	 */
	void updateAPIInfo() {
		lastCallTime = System.currentTimeMillis();
		apiCallsCount++;
	}
}