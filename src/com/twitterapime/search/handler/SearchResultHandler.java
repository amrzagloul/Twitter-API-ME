/*
 * SearchResultHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Attributes;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML search result from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class SearchResultHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Search device listener object.
	 * </p> 
	 */
	private SearchDeviceListener listener;
	
	/**
	 * <p>
	 * List of tweets returned by the search.
	 * </p>
	 */
	private Vector tweetList = new Vector(15);
	
	/**
	 * <p>
	 * Current tweet.
	 * </p>
	 */
	private Tweet tweet;
	
	/**
	 * <p>
	 * Current tweet's values.
	 * </p>
	 */
	private Hashtable tweetValues;
	
	/**
	 * <p>
	 * Current tweet's user values.
	 * </p>
	 */
	private Hashtable tweetUserValues;

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		super.startElement(namespaceURI, localName, qName, attrs);
		localName = localName.toLowerCase();
		//
		if (localName.equals("entry")) {
			tweet = new Tweet();
			tweetValues = new Hashtable(15);
			tweet.setData(tweetValues);
			tweetUserValues = new Hashtable(3);
			tweetValues.put(
				MetadataSet.TWEET_USER_ACCOUNT,
				new UserAccount(tweetUserValues));
		} else if (localName.equals("link")
				&& xmlPath.equals("/feed/entry/link")) {
			final String attrValue = attrs.getValue("type");
			//
			if (attrValue.equals("text/html")) {
				tweetValues.put(MetadataSet.TWEET_URI, attrs.getValue("href"));
			} else if (attrValue.equals("image/png")) {
				tweetValues.put(
					MetadataSet.TWEET_AUTHOR_PICTURE_URI,
					attrs.getValue("href"));
				//
				tweetUserValues.put(
					MetadataSet.USERACCOUNT_PICTURE_URI,
					attrs.getValue("href"));
			}
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		super.endElement(namespaceURI, localName, qName);
		//
		if (localName.toLowerCase().equals("entry")) {
			tweetList.addElement(tweet);
			fireTweetParsed(tweet);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		if (xmlPath.equals("/feed/entry/id")) {
			tweetValues.put(
				MetadataSet.TWEET_ID, StringUtil.formatTweetID(text));
		} else if (xmlPath.equals("/feed/entry/published")) {
			tweetValues.put(
				MetadataSet.TWEET_PUBLISH_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (xmlPath.equals("/feed/entry/title")) {
			tweetValues.put(MetadataSet.TWEET_CONTENT, text);
		} else if (xmlPath.equals("/feed/entry/twitter:source")
				|| xmlPath.equals("/feed/entry/source")) {
			tweetValues.put(
				MetadataSet.TWEET_SOURCE, StringUtil.removeTags(text));
		} else if (xmlPath.equals("/feed/entry/twitter:lang")
				|| xmlPath.equals("/feed/entry/lang")) {
			tweetValues.put(MetadataSet.TWEET_LANG, text);
		} else if (xmlPath.equals("/feed/entry/author/name")) {
			String[] names = StringUtil.splitTweetAuthorNames(text);
			tweetValues.put(MetadataSet.TWEET_AUTHOR_USERNAME, names[0]);
			tweetValues.put(MetadataSet.TWEET_AUTHOR_NAME, names[1]);
			//
			tweetUserValues.put(MetadataSet.USERACCOUNT_USER_NAME, names[0]);
			tweetUserValues.put(MetadataSet.USERACCOUNT_NAME, names[1]);
			//
			final String picUri =
				"http://api.twitter.com/1/users/profile_image/" + 
				names[0] + 
				".json?size=";
			//
			tweetUserValues.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_MINI, picUri + "mini");
			tweetUserValues.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_NORMAL, picUri + "normal");
			tweetUserValues.put(
				MetadataSet.USERACCOUNT_PICTURE_URI_BIGGER, picUri + "bigger");
		} else if (xmlPath.equals("/feed/entry/author/uri")) {
			tweetValues.put(MetadataSet.TWEET_AUTHOR_URI, text);
		}
	}
	
	/**
	 * <p>
	 * Returns the parsed tweets.
	 * </p>
	 * @return Tweets.
	 */
	public Tweet[] getParsedTweets() {
		Tweet[] tweets = new Tweet[tweetList.size()];
		tweetList.copyInto(tweets);
		//
		return tweets;
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