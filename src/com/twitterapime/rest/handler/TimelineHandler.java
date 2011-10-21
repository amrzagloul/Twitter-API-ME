/*
 * TimelineHandler.java
 * 10/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Attributes;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.search.TweetEntity;
import com.twitterapime.search.handler.TweetEntityHandler;

/**
 * <p>
 * Handler class for parsing the XML timeline from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.2
 */
public final class TimelineHandler extends DefaultXMLHandler {
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
	 * List of tweets.
	 * </p>
	 */
	private Vector tweetList = new Vector(10);
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userValues;

	/**
	 * <p>
	 * Hash with user account values from reposted tweet.
	 * </p>
	 */
	private Hashtable retweetUserValues;

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable tweetValues;

	/**
	 * <p>
	 * Hash with reposted tweet values.
	 * </p>
	 */
	private Hashtable retweetValues;
	
	/**
	 * <p>
	 * Hash with tweet's location values.
	 * </p>
	 */
	private Hashtable locationValues;
	
	/**
	 * <p>
	 * Hash with reposted tweet's location values.
	 * </p>
	 */
	private Hashtable retweetLocationValues;

	/**
	 * <p>
	 * Hash with tweet's entity values.
	 * </p>
	 */
	private Hashtable entityValues;
	
	/**
	 * <p>
	 * Hash with reposted tweet's entity values.
	 * </p>
	 */
	private Hashtable retweetEntityValues;

	/**
	 * <p>
	 * Search device listener object.
	 * </p> 
	 */
	private SearchDeviceListener listener;
		
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		super.startElement(namespaceURI, localName, qName, attrs);
		//
		if (localName.toLowerCase().equals("status")) {
			tweetValues = new Hashtable(5);
			retweetValues = new Hashtable(5);
			userValues = new Hashtable(25);
			retweetUserValues = new Hashtable(25);
			locationValues = new Hashtable(10);
			retweetLocationValues = new Hashtable(10);
			entityValues = new Hashtable(3);
			retweetEntityValues = new Hashtable(3);
			//
			tweetValues.put(
				MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userValues));
			//
			tweetList.addElement(new Tweet(tweetValues));
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.startsWith("/statuses/status/retweeted_status/user/")) {
			userHandler.populate(retweetUserValues, xmlPath, text);
		} else if (xmlPath.startsWith(
				"/statuses/status/retweeted_status/geo/")) {
			locationHandler.populate(retweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith(
				"/statuses/status/retweeted_status/place/")) {
			locationHandler.populate(retweetLocationValues, xmlPath, text);
		} else if (xmlPath.startsWith(
				"/statuses/status/retweeted_status/entities/")) {
			entityHandler.populate(retweetEntityValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/retweeted_status/")) {
			tweetHandler.populate(retweetValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/user/")) {
			userHandler.populate(userValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/geo/")) {
			locationHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/place/")) {
			locationHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/entities/")) {
			entityHandler.populate(entityValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/")) {
			tweetHandler.populate(tweetValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		super.endElement(namespaceURI, localName, qName);
		//
		if (localName.toLowerCase().equals("status")) {
			if (retweetValues.size() > 0) {  // is it a retweet?
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
			if (locationValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_LOCATION,
					new GeoLocation(locationValues));
			}
			//
			if (entityValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_ENTITY,
					new TweetEntity(entityValues));
			}
			//
			fireTweetParsed((Tweet) tweetList.lastElement());
		}
	}
	
	/**
	 * <p>
	 * Return the parsed tweets.
	 * </p>
	 * @return Array of tweets.
	 */
	public Tweet[] getParsedTweets() {
		Tweet[] ts = new Tweet[tweetList.size()];
		tweetList.copyInto(ts);
		//
		return ts;
	}
	
	/**
	 * <p>
	 * Set the search device listener object.
	 * </p>
	 * @param listener Listener object.
	 */
	public void setSearchDeviceListener(SearchDeviceListener listener) {
		this.listener = listener;
	}
	
	/**
	 * <p>
	 * Fire the listened when a tweet is parsed.
	 * </p>
	 * @param tweet Parsed tweet.
	 */
	private void fireTweetParsed(Tweet tweet) {
		if (listener != null) {
			listener.tweetFound(tweet);
		}
	}
}