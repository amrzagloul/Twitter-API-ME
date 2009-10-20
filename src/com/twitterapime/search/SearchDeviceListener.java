/*
 * SearchDeviceListener
 * 02/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface SearchDeviceListener {
	/**
	 * @param tweet
	 */
	public void tweetFound(Tweet tweet);
	
	/**
	 *
	 */
	public void searchCompleted();

	/**
	 * @param cause
	 */
	public void searchFailed(Throwable cause);
}
