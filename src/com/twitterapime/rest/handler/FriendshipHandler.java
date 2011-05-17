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
import com.twitterapime.util.StringUtil;

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
			Hashtable source = (Hashtable)content.get("source");
			//
			if (source != null) {
				String following = (String)source.get("following");
				String userId = (String)source.get("id_str");
				String username = (String)source.get("screen_name");
				String followedBy = (String)source.get("followed_by");
				String notificationsEnabled =
					(String)source.get("notifications_enabled");
				String canDM = (String)source.get("can_dm");
				String blocking = (String)source.get("blocking");
				String allReplies = (String)source.get("all_replies");
				String wantRetweets = (String)source.get("want_retweets");
				String markedSpam = (String)source.get("marked_spam");
				//
				source.clear();
				//
				if (!StringUtil.isEmpty(following)) {
					source.put(MetadataSet.FRIENDSHIP_FOLLOWING, following);
				}
				if (!StringUtil.isEmpty(userId)) {
					source.put(MetadataSet.FRIENDSHIP_USER_ID, userId);
				}
				if (!StringUtil.isEmpty(username)) {
					source.put(MetadataSet.FRIENDSHIP_USER_NAME, username);
				}
				if (!StringUtil.isEmpty(followedBy)) {
					source.put(MetadataSet.FRIENDSHIP_FOLLOWED_BY, followedBy);
				}
				if (!StringUtil.isEmpty(notificationsEnabled)
						&& !"null".equals(notificationsEnabled)) {
					source.put(
						MetadataSet.FRIENDSHIP_NOTIFICATIONS_ENABLED,
						notificationsEnabled);
				}
				if (!StringUtil.isEmpty(canDM)) {
					source.put(MetadataSet.FRIENDSHIP_CAN_DM, canDM);
				}
				if (!StringUtil.isEmpty(blocking)
						&& !"null".equals(blocking)) {
					source.put(MetadataSet.FRIENDSHIP_BLOCKING, blocking);
				}
				if (!StringUtil.isEmpty(allReplies)
						&& !"null".equals(allReplies)) {
					source.put(MetadataSet.FRIENDSHIP_ALL_REPLIES, allReplies);
				}
				if (!StringUtil.isEmpty(wantRetweets)
						&& !"null".equals(wantRetweets)) {
					source.put(
						MetadataSet.FRIENDSHIP_WANT_RETWEETS, wantRetweets);
				}
				if (!StringUtil.isEmpty(markedSpam)
						&& !"null".equals(markedSpam)) {
					source.put(MetadataSet.FRIENDSHIP_MARKED_SPAM, markedSpam);
				}
			}
			//
			Hashtable target = (Hashtable)content.get("target");
			//
			if (target != null) {
				String following = (String)target.get("following");
				String userId = (String)target.get("id_str");
				String username = (String)target.get("screen_name");
				String followedBy = (String)target.get("followed_by");
				//
				target.clear();
				//
				if (!StringUtil.isEmpty(following)) {
					target.put(MetadataSet.FRIENDSHIP_FOLLOWING, following);
				}
				if (!StringUtil.isEmpty(userId)) {
					target.put(MetadataSet.FRIENDSHIP_USER_ID, userId);
				}
				if (!StringUtil.isEmpty(username)) {
					target.put(MetadataSet.FRIENDSHIP_USER_NAME, username);
				}
				if (!StringUtil.isEmpty(followedBy)) {
					target.put(MetadataSet.FRIENDSHIP_FOLLOWED_BY, followedBy);
				}
			}
			//
			Hashtable friendship = new Hashtable(2);
			//
			if (source != null) {
				friendship.put(
					MetadataSet.FRIENDSHIP_SOURCE, new Friendship(source));
			}
			if (target != null) {
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
