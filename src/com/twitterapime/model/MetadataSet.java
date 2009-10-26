/*
 * MetadataSet.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

/**
 * <p>
 * MetadataSet is a interface that specifies a collection of metadata
 * attributes applicable to the entities that implement the interface Entity.
 * The prefix of each constant indicates which class that constant belongs to.
 * For instance, "TWEET_XXX" belongs to Tweet class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class MetadataSet {
	/**
	 * <p>
	 * Reports the Tweet ID, e.g., "tag:search.twitter.com,2005:9637830025".
	 * </p>
	 */
	public static final String TWEET_ID = "TWEET_ID";

	/**
	 * <p>
	 * Reports the Tweet URI, e.g.,
	 * "http://twitter.com/twitteruser/statuses/7895462131".
	 * </p>
	 */
	public static final String TWEET_URI = "TWEET_URI";

	/**
	 * <p>
	 * Reports the Tweet content, e.g., "Hello Twitter!!! How are you?".
	 * </p>
	 */
	public static final String TWEET_CONTENT = "TWEET_CONTENT";

	/**
	 * <p>
	 * Reports the Tweet publish date, e.g., "2009-10-20 23:42:50".
	 * </p>
	 */
	public static final String TWEET_PUBLISH_DATE = "TWEET_PUBLISH_DATE";

	/**
	 * <p>
	 * Reports the Tweet source (application), e.g., "TweetApp".
	 * </p>
	 */
	public static final String TWEET_SOURCE = "TWEET_SOURCE";

	/**
	 * <p>
	 * Reports the Tweet language code, e.g., "en".
	 * </p>
	 */
	public static final String TWEET_LANG = "TWEET_LANG";

	/**
	 * <p>
	 * Reports the Tweet author name, e.g., "John Smith".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_NAME = "TWEET_AUTHOR_NAME";

	/**
	 * <p>
	 * Reports the Tweet author username, e.g., "johnsmith".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_USERNAME = "TWEET_AUTHOR_USERNAME";

	/**
	 * <p>
	 * Reports the Tweet author URI, e.g., "http://twitter.com/twitteruser".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_URI = "TWEET_AUTHOR_URI";

	/**
	 * <p>
	 * Reports the Tweet author picture URI, e.g.,
	 * "http://a3.twimg.com/profile_images/8978974/pic_normal.JPG".
	 * </p>
	 */
	public static final String TWEET_AUTHOR_PICTURE_URI = "TWEET_AUTHOR_PICTURE_URI";

	/**
	 * <p>
	 * Package-protected constructor to avoid object instantiation.
	 * </p>
	 */
	MetadataSet() {
	}
}