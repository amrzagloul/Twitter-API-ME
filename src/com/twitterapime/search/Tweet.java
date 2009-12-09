/*
 * Tweet.java
 * 03/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccount;

/**
 * <p>
 * This class defines an entity that represents a Tweet. A tweet is a message
 * posted by an user to Twitter.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see SearchDevice
 * @see TweetER
 */
public final class Tweet extends DefaultEntity {
	/**
	 * 
	 */
	public static final int MAX_CHARACTERS = 140;

	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 */
	public Tweet() {
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public Tweet(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * @param content Content (status).
	 * @throws IllegalArgumentException If content is invalid.
	 */
	public Tweet(String content) {
		if (content == null) {
			throw new IllegalArgumentException("Content cannot be null");
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.TWEET_CONTENT, content);
		setData(data);
		//
		validateContent();
	}
	
	/**
	 * <p>
	 * Validate tweet's content.
	 * </p>
	 * @throws IllegalArgumentException If the content is null/empty or exceeds
	 *         140 characters.
	 */
	public void validateContent() {
		String text = getString(MetadataSet.TWEET_CONTENT);
		if (text == null || (text = text.trim()).length() == 0) {
			throw new IllegalArgumentException("Content cannot be empty/null.");
		} else if (text.length() > 140) {
			throw new IllegalArgumentException(
				"Content cannot be longer than"+MAX_CHARACTERS+" characters.");
		}
	}

	/**
	 * <p>
	 * Get the user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getUserAccount() {
		return (UserAccount)data.get(MetadataSet.TWEET_USER_ACCOUNT);
	}
}