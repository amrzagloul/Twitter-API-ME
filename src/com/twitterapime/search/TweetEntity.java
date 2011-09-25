/*
 * TweetEntity.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;

/**
 * <p>
 * This class defines a tweet's entity.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.5
 */
public final class TweetEntity extends DefaultEntity {
//#ifdef PP_ANDROID
//@	/**
//@	 * <p>
//@	 * Serial UID.
//@	 * </p>
//@	 */
//@	private static final long serialVersionUID = -5120105475832454594L;
//#endif

	/**
	 * <p>
	 * Create an instance of TweetEntity class.
	 * </p>
	 */
	public TweetEntity() {
	}

	/**
	 * <p>
	 * Create an instance of TweetEntity class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public TweetEntity(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Get an array of hashtags from tweet's entity.
	 * </p>
	 * @return Hashtags.
	 */
	public TweetEntity[] getHashtags() {
		return toArray((Vector)data.get(MetadataSet.TWEETENTITY_HASHTAGS));
	}
	
	/**
	 * <p>
	 * Get an array of URLs from tweet's entity.
	 * </p>
	 * @return URLs.
	 */
	public TweetEntity[] getURLs() {
		return toArray((Vector)data.get(MetadataSet.TWEETENTITY_URLS));
	}
	
	/**
	 * <p>
	 * Get an array of mentions from tweet's entity.
	 * </p>
	 * @return Mentions.
	 */
	public TweetEntity[] getMentions() {
		return toArray((Vector)data.get(MetadataSet.TWEETENTITY_MENTIONS));
	}
	
	/**
	 * <p>
	 * Convert a given vector to a TweetEntity array.
	 * </p>
	 * @param v Vector.
	 * @return Array.
	 */
	private TweetEntity[] toArray(Vector v) {
		if (v == null) {
			return new TweetEntity[0];
		}
		//
		TweetEntity[] tes = new TweetEntity[v.size()];
		v.copyInto(tes);
		//
		return tes;
	}
}
