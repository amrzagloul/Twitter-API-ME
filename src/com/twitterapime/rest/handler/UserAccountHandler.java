/*
 * UserAccountHandler.java
 * 14/11/2009
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
 * Handler class for parsing the XML user account from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public final class UserAccountHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userAccountValues = new Hashtable(20);

	/**
	 * <p>
	 * Hash with last tweet values.
	 * </p>
	 */
	private Hashtable lastTweetValues = new Hashtable(10);

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.equals("/user/id")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_ID, text);
		} else if (xmlPath.equals("/user/name")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_NAME, text);
		} else if (xmlPath.equals("/user/screen_name")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_USER_NAME, text);
		} else if (xmlPath.equals("/user/location")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_LOCATION, text);
		} else if (xmlPath.equals("/user/description")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_DESCRIPTION, text);
		} else if (xmlPath.equals("/user/profile_image_url")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_PICTURE_URI, text);
		} else if (xmlPath.equals("/user/url")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_URL, text);
		} else if (xmlPath.equals("/user/protected")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_PROTECTED, text);
		} else if (xmlPath.equals("/user/followers_count")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, text);
		} else if (xmlPath.equals("/user/profile_background_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, text);
		} else if (xmlPath.equals("/user/profile_text_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, text);
		} else if (xmlPath.equals("/user/profile_link_color")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, text);
		} else if (xmlPath.equals("/user/friends_count")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, text);
		} else if (xmlPath.equals("/user/created_at")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_CREATE_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (xmlPath.equals("/user/favourites_count")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_FAVOURITES_COUNT, text);
		} else if (xmlPath.equals("/user/utc_offset")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_UTC_OFFSET, text);
		} else if (xmlPath.equals("/user/time_zone")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_TIME_ZONE, text);
		} else if (xmlPath.equals("/user/profile_background_image_url")) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI, text);
		} else if (xmlPath.equals("/user/statuses_count")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, text);
		} else if (xmlPath.equals("/user/notifications")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, text);
		} else if (xmlPath.equals("/user/verified")) {
			userAccountValues.put(MetadataSet.USERACCOUNT_VERIFIED, text);
		} else if (xmlPath.equals("/user/status/created_at")) {
			lastTweetValues.put(
				MetadataSet.TWEET_PUBLISH_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (xmlPath.equals("/user/status/id")) {
			lastTweetValues.put(MetadataSet.TWEET_ID, text);
		} else if (xmlPath.equals("/user/status/text")) {
			lastTweetValues.put(MetadataSet.TWEET_CONTENT, text);
		} else if (xmlPath.equals("/user/status/source")) {
			lastTweetValues.put(
				MetadataSet.TWEET_SOURCE, StringUtil.removeTags(text));
		} else if (xmlPath.equals("/user/status/favorited")) {
			lastTweetValues.put(MetadataSet.TWEET_FAVOURITE, text);
		} else if (xmlPath.equals("/user/status/in_reply_to_status_id")) {
			lastTweetValues.put(MetadataSet.TWEET_IN_REPLY_TO_TWEET_ID, text);
		} else if (xmlPath.equals("/user/status/in_reply_to_user_id")) {
			lastTweetValues.put(MetadataSet.TWEET_IN_REPLY_TO_USER_ID, text);
		} else if (xmlPath.equals("/user/status/in_reply_to_screen_name")) {
			lastTweetValues.put(MetadataSet.TWEET_IN_REPLY_TO_USERNAME,text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
		userAccountValues.put(
			MetadataSet.USERACCOUNT_LAST_TWEET, new Tweet(lastTweetValues));
	}
	
	/**
	 * <p>
	 * Return the parsed user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getParsedUserAccount() {
		return new UserAccount(userAccountValues);
	}
}