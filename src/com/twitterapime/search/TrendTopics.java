/*
 * TrendTopics.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.search.handler.TrendTopicsHandler;
import com.twitterapime.search.handler.TrendTopicsWoeidHandler;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class provides methods for searching for trend topics.
 * </p>
 * <p>
 * <pre>
 * TrendTopics tt = TrendTopics.getInstance();
 * Query q1 = QueryComposer.date(new Date());
 * Topic[] ts = tt.searchDailyTopics(q);
 * for (int i = 0; i < ts.length; i++) {
 *   list.append(ts[i].getString(MetadataSet.TOPIC_TEXT), null);
 * }
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.5
 */
public final class TrendTopics {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static TrendTopics singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service trends current.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/trends/current" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/trends/current
	 * </a>
	 * </p>
	 * @see TrendTopics#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_TRENDS_CURRENT =
		"TWITTER_API_URL_SERVICE_TRENDS_CURRENT";
	
	/**
	 * <p>
	 * Key for Twitter API URL service trends daily.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/trends/daily" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/trends/daily
	 * </a>
	 * </p>
	 * @see TrendTopics#setServiceURL(String, String)
	 * @see TrendTopics#searchDailyTopics(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_TRENDS_DAILY =
		"TWITTER_API_URL_SERVICE_TRENDS_DAILY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service trends weekly.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/trends/weekly" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/trends/weekly
	 * </a>
	 * </p>
	 * @see TrendTopics#setServiceURL(String, String)
	 * @see TrendTopics#searchWeeklyTopics(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_TRENDS_WEEKLY =
		"TWITTER_API_URL_SERVICE_TRENDS_WEEKLY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service trends woeid.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/trends/%3Awoeid" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/trends/%3Awoeid
	 * </a>
	 * </p>
	 * @see TrendTopics#setServiceURL(String, String)
	 * @see TrendTopics#searchNowTopics(Query)
	 * @see TrendTopics#searchNowTopics(String, Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_TRENDS_WOEID =
		"TWITTER_API_URL_SERVICE_TRENDS_WOEID";
	
	static {
		SERVICES_URL = new Hashtable(3);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_TRENDS_WOEID,
			"http://api.twitter.com/1/trends/:woeid.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_TRENDS_DAILY,
			"http://api.twitter.com/1/trends/daily.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_TRENDS_WEEKLY,
			"http://api.twitter.com/1/trends/weekly.json");
	}
	
	/**
	 * <p>
	 * Get a single instance of TrendTopics class.
	 * </p>
	 * @return TrendTopics single instance.
	 */
	public static synchronized TrendTopics getInstance() {
		if (singleInstance == null) {
			singleInstance = new TrendTopics();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private TrendTopics() {
	}
	
	/**
	 * <p>
	 * Set a new URL to a given Twitter API service. This method is very useful
	 * in case Twitter API decides to change a service's URL. So there is no
	 * need to wait for a new version of this API to get it working back.
	 * </p>
	 * <p>
	 * <b>Be careful about using this method, since it can cause unexpected
	 * results, case you enter an invalid URL.</b>
	 * </p>
	 * @param serviceKey Service key.
	 * @param url New URL.
	 * @see TrendTopics#TWITTER_API_URL_SERVICE_TRENDS_WOEID
	 * @see TrendTopics#TWITTER_API_URL_SERVICE_TRENDS_DAILY
	 * @see TrendTopics#TWITTER_API_URL_SERVICE_TRENDS_WEEKLY
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}

	/**
	 * <p>
	 * Get most recent topics in the world.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#excludeHashtags()}</li>
	 * </ul>
	 * </p>
	 * @param query The filter query. If null all topics are returned.
	 * @return Most recent topics.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	public Topic[] searchNowTopics(Query query) throws IOException,
		LimitExceededException {
		return searchNowTopics("1", query);
	}

	/**
	 * <p>
	 * Get most recent topics from a given location on earth.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#excludeHashtags()}</li>
	 * </ul>
	 * </p>
	 * @param woeid <a href="http://developer.yahoo.com/geo/geoplanet/" target="_blank">Yahoo! Where On Earth ID</a>.
	 * @param query The filter query. If null all topics are returned.
	 * @return Most recent topics.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If woeid is null/empty.
	 */
	public Topic[] searchNowTopics(String woeid, Query query)
		throws IOException, LimitExceededException {
		if (StringUtil.isEmpty(woeid)) {
			throw new IllegalArgumentException("Woeid must not be empty/null.");
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_TRENDS_WOEID);
		url = StringUtil.replace(url, ":woeid", woeid);
		//
		if (query != null) {
			url += '?' + query.toString();
		}
		//
		HttpRequest req = new HttpRequest(url);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			TrendTopicsWoeidHandler handler = new TrendTopicsWoeidHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedTopics();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Get daily topics.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#date(java.util.Date)}</li>
	 * <li>{@link QueryComposer#excludeHashtags()}</li>
	 * </ul>
	 * </p>
	 * @param query The filter query. If null all topics are returned.
	 * @return Daily topics.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	public Topic[] searchDailyTopics(Query query) throws IOException,
		LimitExceededException {
		return search(getURL(TWITTER_API_URL_SERVICE_TRENDS_DAILY), query);
	}

	/**
	 * <p>
	 * Get weekly topics.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#date(java.util.Date)}</li>
	 * <li>{@link QueryComposer#excludeHashtags()}</li>
	 * </ul>
	 * </p>
	 * @param query The filter query. If null all topics are returned.
	 * @return Weekly topics.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	public Topic[] searchWeeklyTopics(Query query) throws IOException,
		LimitExceededException {
		return search(getURL(TWITTER_API_URL_SERVICE_TRENDS_WEEKLY), query);
	}
	
	/**
	 * <p>
	 * Get topics.
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#date(java.util.Date)}</li>
	 * <li>{@link QueryComposer#excludeHashtags()}</li>
	 * </ul>
	 * </p>
	 * @param query The filter query. If null all topics are returned.
	 * @return Weekly topics.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	private Topic[] search(String url, Query query) throws IOException,
		LimitExceededException {
		if (query != null) {
			url += '?' + query.toString();
		}
		//
		HttpRequest req = new HttpRequest(url);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			TrendTopicsHandler handler = new TrendTopicsHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedTopics();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Get an URL related to the given service key.
	 * </p>
	 * @param serviceKey Service key.
	 * @return URL.
	 */
	private String getURL(String serviceKey) {
		return (String)SERVICES_URL.get(serviceKey);
	}
}
