/*
 * TweetHandler.java
 * 23/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML tweet from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public final class TweetHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userAccountValues = new Hashtable(20);

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable tweetValues = new Hashtable(10);
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.equals("/status/created_at")) {
			tweetValues.put(
				MetadataSet.TWEET_PUBLISH_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (xmlPath.equals("/status/id")) {
			tweetValues.put(MetadataSet.TWEET_ID, text);
		} else if (xmlPath.equals("/status/text")) {
			tweetValues.put(MetadataSet.TWEET_CONTENT, text);
		} else if (xmlPath.equals("/status/source")) {
			tweetValues.put(
				MetadataSet.TWEET_SOURCE, StringUtil.removeTags(text));
		} else if (xmlPath.equals("/status/in_reply_to_status_id")) {
			tweetValues.put(MetadataSet.TWEET_IN_REPLY_TO_TWEET_ID, text);
		} else if (xmlPath.equals("/status/in_reply_to_user_id")) {
			tweetValues.put(MetadataSet.TWEET_IN_REPLY_TO_USER_ID, text);
		} else if (xmlPath.equals("/status/favorited")) {
			tweetValues.put(MetadataSet.TWEET_FAVOURITE, text);
		} else if (xmlPath.equals("/status/user/id")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_ID, text);
		} else if (xmlPath.equals("/status/user/name")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_NAME, text);
		} else if (xmlPath.equals("/status/user/screen_name")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_USER_NAME, text);
		} else if (xmlPath.equals("/status/user/location")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_LOCATION, text);
		} else if (xmlPath.equals("/status/user/description")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_DESCRIPTION, text);
		} else if (xmlPath.equals("/status/user/profile_image_url")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_PICTURE_URI, text);
		} else if (xmlPath.equals("/status/user/url")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_URL, text);
		} else if (xmlPath.equals("/status/user/protected")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_PROTECTED, text);
		} else if (xmlPath.equals("/status/user/followers_count")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, text);
		} else if (xmlPath.equals("/status/user/profile_background_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, text);
		} else if (xmlPath.equals("/status/user/profile_text_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, text);
		} else if (xmlPath.equals("/status/user/profile_link_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, text);
		} else if (xmlPath.equals("/status/user/friends_count")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, text);
		} else if (xmlPath.equals("/status/user/created_at")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_CREATE_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (xmlPath.equals("/status/user/favourites_count")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_FAVOURITES_COUNT, text);
		} else if (xmlPath.equals("/status/user/utc_offset")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_UTC_OFFSET, text);
		} else if (xmlPath.equals("/status/user/time_zone")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_TIME_ZONE, text);
		} else if (xmlPath.equals("/status/user/profile_background_image_url")){
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, text);
		} else if (xmlPath.equals("/status/user/statuses_count")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, text);
		} else if (xmlPath.equals("/status/user/notifications")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, text);
		} else if (xmlPath.equals("/status/user/verified")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_VERIFIED, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
		tweetValues.put(
			MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userAccountValues));
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
	 */
	public void loadParsedTweet(Tweet tweet) {
		tweet.setData(tweetValues);
	}
}