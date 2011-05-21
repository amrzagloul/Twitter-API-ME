/*
 * FriendshipHandler.java
 * 16/05/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.rest.Friendship;

/**
 * <p>
 * Handler class for parsing the JSON friendship result.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.7
 */
public class FriendshipHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * Create an instance of TrendTopicsHandler class.
	 * </p> 
	 */
	public FriendshipHandler() {
		super("relationship");
	}
	
	/**
	 * <p>
	 * Get parsed friendship.
	 * </p>
	 * @return Friendship.
	 */
	public Friendship getParsedFriendship() {
		if (content != null && content.size() > 0) {
			Hashtable friendship = new Hashtable(2);
			Hashtable source = (Hashtable)content.get("source");
			//
			if (source != null) {
				replaceProperty(
					source,
					"following",
					MetadataSet.FRIENDSHIP_FOLLOWING);
				replaceProperty(
					source,
					"id",
					MetadataSet.FRIENDSHIP_USER_ID);
				replaceProperty(
					source,
					"id_str",
					MetadataSet.FRIENDSHIP_USER_ID);
				replaceProperty(
					source,
					"screen_name",
					MetadataSet.FRIENDSHIP_USER_NAME);
				replaceProperty(
					source,
					"followed_by",
					MetadataSet.FRIENDSHIP_FOLLOWED_BY);
				replaceProperty(
					source,
					"notifications_enabled",
					MetadataSet.FRIENDSHIP_NOTIFICATIONS_ENABLED);
				replaceProperty(
					source,
					"can_dm",
					MetadataSet.FRIENDSHIP_CAN_DM);
				replaceProperty(
					source,
					"blocking",
					MetadataSet.FRIENDSHIP_BLOCKING);
				replaceProperty(
					source,
					"all_replies",
					MetadataSet.FRIENDSHIP_ALL_REPLIES);
				replaceProperty(
					source,
					"want_retweets",
					MetadataSet.FRIENDSHIP_WANT_RETWEETS);
				replaceProperty(
					source,
					"marked_spam",
					MetadataSet.FRIENDSHIP_MARKED_SPAM);
				//
				friendship.put(
					MetadataSet.FRIENDSHIP_SOURCE, new Friendship(source));
			}
			//
			Hashtable target = (Hashtable)content.get("target");
			//
			if (target != null) {
				replaceProperty(
					target,
					"following",
					MetadataSet.FRIENDSHIP_FOLLOWING);
				replaceProperty(
					target,
					"id",
					MetadataSet.FRIENDSHIP_USER_ID);
				replaceProperty(
					target,
					"id_str",
					MetadataSet.FRIENDSHIP_USER_ID);
				replaceProperty(
					target,
					"screen_name",
					MetadataSet.FRIENDSHIP_USER_NAME);
				replaceProperty(
					target,
					"followed_by",
					MetadataSet.FRIENDSHIP_FOLLOWED_BY);
				//
				friendship.put(
					MetadataSet.FRIENDSHIP_TARGET, new Friendship(target));
			}
			//
			return new Friendship(friendship);
		} else {
			return null;
		}
	}
}
