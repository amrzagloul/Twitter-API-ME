/*
 * StatusHandler.java
 * 09/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.search.handler.TweetEntityHandler;

/**
 * <p>
 * Handler class for parsing the status' XML results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.2
 */
public final class StatusHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * User Account XML handler object.
	 * </p>
	 */
	private UserAccountHandler userHandler = new UserAccountHandler();
	
	/**
	 * <p>
	 * Tweet XML handler object.
	 * </p>
	 */
	private TweetHandler tweetHandler = new TweetHandler();
	
	/**
	 * <p>
	 * GeoLocation XML handler object.
	 * </p>
	 */
	private GeoLocationHandler locationHandler = new GeoLocationHandler();
	
	/**
	 * <p>
	 * TweetEntity XML handler object.
	 * </p>
	 */
	private TweetEntityHandler entityHandler = new TweetEntityHandler();

	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable tweeetUserValues = new Hashtable(25);
	
	/**
	 * <p>
	 * Hash with user account values from reposted tweet.
	 * </p>
	 */
	private Hashtable retweetUserValues = new Hashtable(25);

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable tweetValues = new Hashtable(10);
	
	/**
	 * <p>
	 * Hash with reposted tweet values.
	 * </p>
	 */
	private Hashtable retweetValues = new Hashtable(10);
	
	/**
	 * <p>
	 * Hash with tweet's location values.
	 * </p>
	 */
	private Hashtable tweetLocationValues = new Hashtable(10);

	/**
	 * <p>
	 * Hash with reposted tweet location values.
	 * </p>
	 */
	private Hashtable retweetLocationValues = new Hashtable(10);

	/**
	 * <p>
	 * Hash with tweet's entity values.
	 * </p>
	 */
	private Hashtable tweetEntityValues = new Hashtable(3);

	/**
	 * <p>
	 * Hash with reposted tweet's entity values.
	 * </p>
	 */
	private Hashtable retweetEntityValues = new Hashtable(3);

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.startsWith("/status/retweeted_status/user/")) {
			userHandler.populate(retweetUserValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/retweeted_status/geo/")) {
			locationHandler.populate(retweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/retweeted_status/place/")) {
			locationHandler.populate(retweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/retweeted_status/entities/")) {
			entityHandler.populate(retweetEntityValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/retweeted_status/")) {
			tweetHandler.populate(retweetValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/user/")) {
			userHandler.populate(tweeetUserValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/geo/")) {
			locationHandler.populate(tweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/place/")) {
			locationHandler.populate(tweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/entities/")) {
			entityHandler.populate(tweetEntityValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/")) {
			tweetHandler.populate(tweetValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
		tweetValues.put(
			MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(tweeetUserValues));
		//
		if (retweetValues.size() > 0) { // is it a retweet?
			if (retweetUserValues.size() > 0) {
				retweetValues.put(
					MetadataSet.TWEET_USER_ACCOUNT,
					new UserAccount(retweetUserValues));
			}
			if (retweetLocationValues.size() > 0) {
				retweetValues.put(
					MetadataSet.TWEET_LOCATION,
					new GeoLocation(retweetLocationValues));
			}
			if (retweetEntityValues.size() > 0) {
				retweetValues.put(
					MetadataSet.TWEET_ENTITY,
					new TweetEntity(retweetEntityValues));
			}
			//
			tweetValues.put(
				MetadataSet.TWEET_REPOSTED_TWEET, new Tweet(retweetValues));
		}
		//
		if (tweetLocationValues.size() > 0) {
			tweetValues.put(
				MetadataSet.TWEET_LOCATION,
				new GeoLocation(tweetLocationValues));
		}
		//
		if (tweetEntityValues.size() > 0) {
			tweetValues.put(
				MetadataSet.TWEET_ENTITY,
				new TweetEntity(tweetEntityValues));
		}
	}
	
	/**
	 * <p>
	 * Return the parsed tweet.
	 * </p>
	 * @return Tweet.
	 */
	public Tweet getParsedTweet() {
		return new Tweet(tweetValues);
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given tweet.
	 * </p>
	 * @param tweet Tweet to be loaded.
	 */
	public void loadParsedTweet(Tweet tweet) {
		tweet.setData(tweetValues);
	}
}
