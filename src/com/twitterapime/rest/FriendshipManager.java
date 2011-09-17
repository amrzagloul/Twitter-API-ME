/*
 * FriendshipManager.java
 * 22/08/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.Cursor;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.rest.handler.FriendshipHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines the methods responsible for managing friendship (friends 
 * and followers).
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   FriendshipManager fdr = FriendshipManager.getInstance(uam);
 *   String[] ids = fdr.getFriendsID(null);
 * }
 * 
 * ...
 * 
 * FriendshipManager fdr = FriendshipManager.getInstance();
 * String[] ids = fdr.getFriendsID(new UserAccount("twapime"), null);
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.4
 */
public final class FriendshipManager {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * FriendshipManager pool used to cache instanced associated to user
	 * accounts.
	 * </p>
	 */
	private static Hashtable friendsMngrPool;

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static FriendshipManager singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service friends id.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friends/ids" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friends/ids
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFriendsID(Query)
	 * @see FriendshipManager#getFriendsID(UserAccount, Query)
	 * @see FriendshipManager#getFriendsIDs(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDS_ID =
		"TWITTER_API_URL_SERVICE_FRIENDS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service followers id.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/followers/ids" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/followers/ids
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFollowersID(Query)
	 * @see FriendshipManager#getFollowersID(UserAccount, Query)
	 * @see FriendshipManager#getFollowersIDs(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FOLLOWERS_ID =
		"TWITTER_API_URL_SERVICE_FOLLOWERS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships create.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/friendships/create" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/friendships/create
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#follow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships destroy.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/friendships/destroy" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/friendships/destroy
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#unfollow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friendships/exists" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friendships/exists
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#isFollowing(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships incoming.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friendships/incoming" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friendships/incoming
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getIncomingFollowersID(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_INCOMING =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_INCOMING";

	/**
	 * <p>
	 * Key for Twitter API URL service friendships outgoing.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friendships/outgoing" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friendships/outgoing
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getOutgoingFriendsID(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_OUTGOING =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_OUTGOING";

	/**
	 * <p>
	 * Key for Twitter API URL service blocks create.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/blocks/create" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/blocks/create
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#block(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_CREATE =
		"TWITTER_API_URL_SERVICE_BLOCKS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service blocks destroy.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/blocks/destroy" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/blocks/destroy
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#unblock(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_DESTROY =
		"TWITTER_API_URL_SERVICE_BLOCKS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friendships/exists" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friendships/exists
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#isBlocking(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_EXISTS =
		"TWITTER_API_URL_SERVICE_BLOCKS_EXISTS";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses friends.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/statuses/friends" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/statuses/friends
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFriends(Query)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_FRIENDS =
		"TWITTER_API_URL_SERVICE_STATUSES_FRIENDS";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses followers.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/statuses/followers" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/statuses/followers
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFollowers(Query)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_FOLLOWERS =
		"TWITTER_API_URL_SERVICE_STATUSES_FOLLOWERS";

	/**
	 * <p>
	 * Key for Twitter API URL service friendships show.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/friendships/show" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/friendships/show
	 * </a>
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFriendship(UserAccount, UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_SHOW =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_SHOW";

	static {
		SERVICES_URL = new Hashtable(13);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDS_ID,
			"http://api.twitter.com/1/friends/ids.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FOLLOWERS_ID,
			"http://api.twitter.com/1/followers/ids.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE,
			"http://api.twitter.com/1/friendships/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY,
			"http://api.twitter.com/1/friendships/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS,
			"http://api.twitter.com/1/friendships/exists.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_INCOMING,
			"http://api.twitter.com/1/friendships/incoming.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_OUTGOING,
			"http://api.twitter.com/1/friendships/outgoing.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_CREATE,
			"http://api.twitter.com/1/blocks/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_DESTROY,
			"http://api.twitter.com/1/blocks/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_EXISTS,
			"http://api.twitter.com/1/blocks/exists/");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_FRIENDS,
			"http://api.twitter.com/1/statuses/friends.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_FOLLOWERS,
			"http://api.twitter.com/1/statuses/followers.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_SHOW,
			"http://api.twitter.com/1/friendships/show.json");
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

	/**
	 * <p>
	 * Get an URL related to the given service key.
	 * </p>
	 * @param serviceKey Service key.
	 * @return URL.
	 */
	private String getURL(String serviceKey) {
		return (String)SERVICES_URL.get(serviceKey);
	}
	
	/**
	 * <p>
	 * Release the objects which account is no longer authenticated.
	 * </p>
	 */
	static synchronized void cleanPool() {
		if (friendsMngrPool != null) {
			Enumeration keys = friendsMngrPool.keys();
			Object key;
			FriendshipManager value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (FriendshipManager)friendsMngrPool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					friendsMngrPool.remove(key);
				}
			}
		}
	}
	
	/**
	 * <p>
	 * Set a new URL to a given Twitter API service. This method is very useful
	 * in case Twitter API decides to change a service's URL. So there is no
	 * need to wait for a new version of this API to get it working back.
	 * </p>
	 * <p>
	 * <b>Be careful about using this method, since it can cause unexpected
	 * results, case you enter an invalid URL.</b>
	 * </p>
	 * @param serviceKey Service key.
	 * @param url New URL.
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDS_ID
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FOLLOWERS_ID
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_CREATE
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_DESTROY
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_EXISTS
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_STATUSES_FRIENDS
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_STATUSES_FOLLOWERS
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_SHOW
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of FriendshipManager class and associate it to a given
	 * user account.
	 * </p>
	 * @param uam User account manager.
	 * @return FriendshipManager instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static FriendshipManager getInstance(
		UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (friendsMngrPool == null) {
			friendsMngrPool = new Hashtable();
		}
		//
		FriendshipManager fsmr = (FriendshipManager)friendsMngrPool.get(uam);
		if (fsmr == null) {
			fsmr = new FriendshipManager(uam);
			friendsMngrPool.put(uam, fsmr);
		}
		//
		return fsmr;
	}
	
	/**
	 * <p>
	 * Get a single instance of FriendshipManager class, which is NOT associated
	 * to any user account.
	 * </p>
	 * @return FriendshipManager single instance.
	 */
	public synchronized static FriendshipManager getInstance() {
		if (singleInstance == null) {
			singleInstance = new FriendshipManager();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * Create an instance of FriendshipManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private FriendshipManager() {
	}
	
	/**
	 * <p>
	 * Create an instance of FriendshipManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private FriendshipManager(UserAccountManager uam) {
		userAccountMngr = uam;
	}
	
	/**
	 * <p>
	 * Get the friends ID list of verified user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 * @deprecated Use {@link FriendshipManager#getFriendsIDs(Query)}.
	 */
	public String[] getFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDS_ID),
			userAccountMngr.getUserAccount(),
			query);
	}

	/**
	 * <p>
	 * Get the friends ID list of a given user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param user User.
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 * @deprecated Use {@link FriendshipManager#getFriendsIDs(Query)}.
	 */
	public String[] getFriendsID(UserAccount user, Query query)
		throws IOException, LimitExceededException {
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDS_ID), user, query);
	}

	/**
	 * <p>
	 * Get the followers ID list of verified user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 * @deprecated Use {@link FriendshipManager#getFollowersIDs(Query)}.
	 */
	public String[] getFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FOLLOWERS_ID),
			userAccountMngr.getUserAccount(),
			query);
	}
	
	/**
	 * <p>
	 * Get the followers ID list of a given user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param user User.
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 * @deprecated Use {@link FriendshipManager#getFollowersIDs(Query)}.
	 */
	public String[] getFollowersID(UserAccount user, Query query)
		throws IOException,	LimitExceededException {
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FOLLOWERS_ID), user, query);
	}
	
	/**
	 * <p>
	 * Get the friends IDs of the authenticating user or given user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have 
	 * navigate through the cursor returned and perform other requests in order
	 * to retrieve more results.
	 * </p>
	 * <pre>
	 * FriendshipManager fm = ...;
	 * Query query = QueryComposer.cursor(-1);
	 * Cursor cursor = null;
	 * 
	 * do {
	 *   if (cursor != null) {
	 *     query = QueryComposer.cursor(cursor.getNextPageIndex());
	 *   }
	 *   
	 *   cursor = fm.getFriendsIDs(query); //friends of authenticating user.
	 *    
	 *   while (cursor.hasMoreElements()) {
	 *       String friendID = (String)cursor.nextElement();
	 *       ...
	 *   }
	 * } while (cursor.hasMorePages());
	 * ...
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userID(String)}</li>
	 * <li>{@link QueryComposer#screenName(String)}</li>
	 * <li>{@link QueryComposer#cursor(long)}</li>
	 * </ul>
	 * </p>
	 * @param query Query.
	 * @return Friends.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @see MetadataSet#USERACCOUNT_FRIENDS_COUNT
	 */
	public Cursor getFriendsIDs(Query query) throws IOException,
		LimitExceededException {
		return getFriendsIDsOrFollowersIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDS_ID), query);
	}
	
	/**
	 * <p>
	 * Get the followers IDs of the authenticating user or given user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have 
	 * navigate through the cursor returned and perform other requests in order
	 * to retrieve more results.
	 * </p>
	 * <pre>
	 * FriendshipManager fm = ...;
	 * Query query = QueryComposer.cursor(-1);
	 * Cursor cursor = null;
	 * 
	 * do {
	 *   if (cursor != null) {
	 *     query = QueryComposer.cursor(cursor.getNextPageIndex());
	 *   }
	 *   
	 *   cursor = fm.getFollowersIDs(query); //followers of authenticating user.
	 *    
	 *   while (cursor.hasMoreElements()) {
	 *       String followerID = (String)cursor.nextElement();
	 *       ...
	 *   }
	 * } while (cursor.hasMorePages());
	 * ...
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userID(String)}</li>
	 * <li>{@link QueryComposer#screenName(String)}</li>
	 * <li>{@link QueryComposer#cursor(long)}</li>
	 * </ul>
	 * </p>
	 * @param query Query.
	 * @return Friends.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @see MetadataSet#USERACCOUNT_FOLLOWERS_COUNT
	 */
	public Cursor getFollowersIDs(Query query) throws IOException,
		LimitExceededException {
		return getFriendsIDsOrFollowersIDs(
			getURL(TWITTER_API_URL_SERVICE_FOLLOWERS_ID), query);
	}

	/**
	 * <p>
	 * Get the friends of the authenticating user or given user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have 
	 * navigate through the cursor returned and perform other requests in order
	 * to retrieve more results.
	 * </p>
	 * <pre>
	 * FriendshipManager fm = ...;
	 * Query query = QueryComposer.cursor(-1);
	 * Cursor cursor = null;
	 * 
	 * do {
	 *   if (cursor != null) {
	 *     query = QueryComposer.cursor(cursor.getNextPageIndex());
	 *   }
	 *   
	 *   cursor = fm.getFriends(query); //friends of authenticating user.
	 *    
	 *   while (cursor.hasMoreElements()) {
	 *       UserAccount friend = (UserAccount)cursor.nextElement();
	 *       ...
	 *   }
	 * } while (cursor.hasMorePages());
	 * ...
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userID(String)}</li>
	 * <li>{@link QueryComposer#screenName(String)}</li>
	 * <li>{@link QueryComposer#cursor(long)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param query Query.
	 * @return Friends.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @deprecated
	 */
	public Cursor getFriends(Query query) throws IOException,
		LimitExceededException {
		return getFriendsOrFollowers(
			getURL(TWITTER_API_URL_SERVICE_STATUSES_FRIENDS), query);
	}
	
	/**
	 * <p>
	 * Get the followers of the authenticating user or given user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have 
	 * navigate through the cursor returned and perform other requests in order
	 * to retrieve more results.
	 * </p>
	 * <pre>
	 * FriendshipManager fm = ...;
	 * Query query = QueryComposer.cursor(-1);
	 * Cursor cursor = null;
	 * 
	 * do {
	 *   if (cursor != null) {
	 *     query = QueryComposer.cursor(cursor.getNextPageIndex());
	 *   }
	 *   
	 *   cursor = fm.getFollowers(query); //followers of authenticating user.
	 *    
	 *   while (cursor.hasMoreElements()) {
	 *       UserAccount friend = (UserAccount)cursor.nextElement();
	 *       ...
	 *   }
	 * } while (cursor.hasMorePages());
	 * ...
	 * </pre>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#userID(String)}</li>
	 * <li>{@link QueryComposer#screenName(String)}</li>
	 * <li>{@link QueryComposer#cursor(long)}</li>
	 * <li>{@link QueryComposer#includeEntities()}</li>
	 * </ul>
	 * </p>
	 * @param query Query.
	 * @return Followers.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @deprecated
	 */
	public Cursor getFollowers(Query query) throws IOException,
		LimitExceededException {
		return getFriendsOrFollowers(
			getURL(TWITTER_API_URL_SERVICE_STATUSES_FOLLOWERS), query);
	}

	/**
	 * <p>
	 * Allows the authenticating user to follow the user specified in the given
	 * UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from followed user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already followed or does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount follow(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE, ua);
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to unfollow the user specified in the
	 * given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from unfollowed user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already unfollowed or does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount unfollow(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY, ua);
	}
	
	/**
	 * <p>
	 * Verify whether the authenticating user is following the user specified in
	 * the given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Following (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws InvalidQueryException If user does not exist or is protected.
	 * @throws SecurityException If it is not authenticated.
	 */
	public boolean isFollowing(UserAccount ua) throws IOException,
		LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		Credential c = userAccountMngr.getCredential();
		final String qryStr =
			"?user_a=" + c.getString(MetadataSet.CREDENTIAL_USERNAME) +
			"&user_b=" + ua.getUserNameOrID();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS) + qryStr);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			return resp.getBodyContent().toLowerCase().equals("true");
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to block the user specified in the given
	 * UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from blocked user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount block(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_BLOCKS_CREATE, ua);
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to unblock the user specified in the
	 * given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from unblocked user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount unblock(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_BLOCKS_DESTROY, ua);
	}
	
	/**
	 * <p>
	 * Verify whether the authenticating user is blocking the user specified in
	 * the given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Blocking (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws InvalidQueryException If user does not exist or is protected.
	 * @throws SecurityException If it is not authenticated.
	 */
	public boolean isBlocking(UserAccount ua) throws IOException,
		LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_BLOCKS_EXISTS) +
			ua.getUserNameOrID() +
			".xml");
		//
		try {
			HttpResponse resp = req.send();
			if (resp.getCode() == HttpConnection.HTTP_NOT_FOUND) {
				return false; //not blocked!
			}
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			return true;
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Get the IDs for every user who has a pending request to follow the
	 * authenticating user.
	 * </p>
	 * <p>
	 * As the number of requests of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Pending followers id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getIncomingFollowersID(Query)
	 */
	public String[] getIncomingFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDSHIPS_INCOMING),
			userAccountMngr.getUserAccount(),
			query);
	}
	
	/**
	 * <p>
	 * Get the IDs for every protected user for whom the authenticating user has
	 * a pending follow request.
	 * </p>
	 * <p>
	 * As the number of requests of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Pending friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getOutgoingFriendsID(Query)
	 */
	public String[] getOutgoingFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDSHIPS_OUTGOING),
			userAccountMngr.getUserAccount(),
			query);
	}
	
	/**
	 * <p>
	 * Get the friendship's details between the authenticating user and a given
	 * user.
	 * </p>
	 * @param target Target user.
	 * @return Friendship details.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @throws IllegalArgumentException if source, target, source's id/username
	 * 									or target's id/username is empty. 
	 */
	public Friendship getFriendship(UserAccount target)
		throws IOException,	LimitExceededException {
		checkUserAuth();
		//
		Credential c = userAccountMngr.getCredential();
		String username = c.getString(MetadataSet.CREDENTIAL_USERNAME);
		//
		return getFriendship(new UserAccount(username), target);
	}
	
	/**
	 * <p>
	 * Get the friendship's details between two users.
	 * </p>
	 * @param source Source user.
	 * @param target Target user.
	 * @return Friendship details.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException if source, target, source's id/username
	 * 									or target's id/username is empty. 
	 */
	public Friendship getFriendship(UserAccount source, UserAccount target)
		throws IOException,	LimitExceededException {
		if (source == null) {
			throw new IllegalArgumentException("Source must not me null.");
		}
		if (target == null) {
			throw new IllegalArgumentException("Target must not me null.");
		}
		//
		source.validateUserNameOrID();
		target.validateUserNameOrID();
		//
		String[] pvSrc = source.getUserNameOrIDParamValue();
		String[] pvTgt = target.getUserNameOrIDParamValue();
		//
		if ("user_id".equals(pvSrc[0])) {
			pvSrc[0] = "source_id";
		} else {
			pvSrc[0] = "source_screen_name";
		}
		if ("user_id".equals(pvTgt[0])) {
			pvTgt[0] = "target_id";
		} else {
			pvTgt[0] = "target_screen_name";
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_FRIENDSHIPS_SHOW);
		url += "?" + pvSrc[0] + "=" + pvSrc[1] + "&" + pvTgt[0] + "=" +pvTgt[1];
		//
		HttpRequest req;
		//
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getParser(ParserFactory.JSON);
			FriendshipHandler handler = new FriendshipHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedFriendship();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Verify whether the authenticating user is followed by the user specified
	 * in the given UserAccount object.
	 * </p>
	 * @param user UserAccount object containing the user name or ID.
	 * @return Followed by (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException if user or user's id/username is empty. 
	 */
	public boolean isFollowedBy(UserAccount user) throws IOException,
		LimitExceededException {
		Friendship details = getFriendship(user).getSource();
		//
		String followedBy =
			details.getString(MetadataSet.FRIENDSHIP_FOLLOWED_BY);
		//
		return followedBy != null && "true".equals(followedBy);
	}

	/**
	 * <p>
	 * Verify whether the authenticating user is enabled to send Direct
	 * Messagens to the user specified in the given UserAccount object.
	 * </p>
	 * @param user UserAccount object containing the user name or ID.
	 * @return Enabled (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException if user or user's id/username is empty.
	 */
	public boolean isEnabledToSendDMTo(UserAccount user) throws IOException,
		LimitExceededException {
		Friendship details = getFriendship(user).getSource();
		//
		String canSendDM =
			details.getString(MetadataSet.FRIENDSHIP_CAN_DM);
		//
		return canSendDM != null && "true".equals(canSendDM);
	}

	/**
	 * <p>
	 * Perform an operation on authenticating user regarding the friendship
	 * management, e.g., follow, unfollow, block or unblock users.
	 * </p>
	 * @param servURLKey Service URL Key.
	 * @param ua UserAccount object containing the user name or ID.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already affected by the action or does
	 *         not exist.
	 * @throws SecurityException If the user is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	private UserAccount manageFriendship(String servURLKey, UserAccount ua)
		throws IOException, LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		String[] pv = ua.getUserNameOrIDParamValue();
		HttpRequest req = userAccountMngr.createRequest(getURL(servURLKey));
		req.setMethod(HttpConnection.POST);
		req.setBodyParameter(pv[0], pv[1]);
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_FORBIDDEN) {
				//already following/blocking.
				throw new InvalidQueryException(
					HttpResponseCodeInterpreter.getErrorMessage(resp));
			}
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedUserAccounts()[0];
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Get the friends/followers of the authenticating user or given user.
	 * </p>
	 * @param url Url.
	 * @param query Query.
	 * @return Friends/Followers.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @throws IllegalArgumentException If url is null/empty.
	 */
	private Cursor getFriendsOrFollowers(String url, Query query)
		throws IOException,	LimitExceededException {
		if (StringUtil.isEmpty(url)) {
			throw new IllegalArgumentException("Url must not be null/empty.");
		}
		//
		if (query != null) {
			url += "?" + query.toString();
		}
		//
		HttpRequest req;
		//
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			Cursor cursor =
				new Cursor(
					handler.getParsedUserAccounts(),
					handler.getPreviousCursorIndex(),
					handler.getNextCursorIndex());
			//
			return cursor;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}

	/**
	 * <p>
	 * Get the friend/follower IDs of the authenticating user or given user.
	 * </p>
	 * @param url Url.
	 * @param query Query.
	 * @return Friends/Followers.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException Given user is protected.
	 * @throws IllegalArgumentException If url is null/empty.
	 */
	private Cursor getFriendsIDsOrFollowersIDs(String url, Query query)
		throws IOException,	LimitExceededException {
		if (StringUtil.isEmpty(url)) {
			throw new IllegalArgumentException("Url must not be null/empty.");
		}
		//
		if (query != null) {
			url += "?" + query.toString();
		}
		//
		HttpRequest req;
		//
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			IDsHandler handler = new IDsHandler(Long.MAX_VALUE);
			parser.parse(resp.getStream(), handler);
			//
			Vector idsVec = handler.getIDsList();
			String[] ids = new String[idsVec.size()];
			//
			idsVec.copyInto(ids);
			//
			Cursor cursor =
				new Cursor(
					ids,
					handler.getCursorPreviousIndex(),
					handler.getCursorNextIndex());
			//
			return cursor;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}

	/**
	 * <p>
	 * Retrieve friends/followers IDs of a given user.
	 * </p>
	 * @param url Service URL.
	 * @param user User.
	 * @param query Max count of IDs to be returned.
	 * @return IDs.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 */
	private String[] retrieveIDs(String url, UserAccount user, Query query)
		throws IOException, LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		user.validateUserNameOrID();
		//
		String[] pv = user.getUserNameOrIDParamValue();
		url += "?" + pv[0] + "=" + pv[1];
		//
		long maxCount = Long.MAX_VALUE;
		if (query != null) {
			maxCount =
				Long.parseLong(StringUtil.split(query.toString(), '=')[1]);
		}
		//
		long loadedCount = 0;
		long cursorNextIdx = -1;
		Vector idsList = new Vector(20);
		IDsHandler handler = new IDsHandler(maxCount);
		Parser parser = ParserFactory.getDefaultParser();
		//
		do {
			HttpRequest req;
			if (userAccountMngr != null) {
				req = userAccountMngr.createRequest(
					url + "&cursor=" + cursorNextIdx);
			} else {
				req = new HttpRequest(url + "&cursor=" + cursorNextIdx);
			}
			//
			try {
				HttpResponse resp = req.send();
				//
				HttpResponseCodeInterpreter.perform(resp);
				//
				parser.parse(resp.getStream(), handler);
				//
				loadedCount += copyVector(idsList, handler.getIDsList());
				cursorNextIdx = handler.getCursorNextIndex();
				//
				handler.clear();
			} catch (ParserException e) {
				throw new IOException(e.getMessage());
			} finally {
				req.close();
			}
		} while (loadedCount < maxCount && cursorNextIdx != 0);
		//
		String[] ids = new String[idsList.size()];
		idsList.copyInto(ids);
		//
		return ids;
	}
	
	/**
	 * <p>
	 * Copy all elements from a vector to another one. 
	 * </p>
	 * @param to Vector to.
	 * @param from Vector from.
	 * @return Count of items copied.
	 */
	private int copyVector(Vector to, Vector from) {
		int size = from.size();
		for (int i = 0; i < size; i++) {
			to.addElement(from.elementAt(i));
		}
		//
		return size;
	}
	
	/**
	 * <p>
	 * Check if the user's is authenticated.
	 * </p>
	 * @throws SecurityException User is not authenticated.
	 */
	private void checkUserAuth() {
		if (userAccountMngr == null || !userAccountMngr.isVerified()) {
			throw new SecurityException(
			    "User's credential must be entered to perform this operation.");
		}
	}
	
	/**
	 * <p>
	 * XML handlers for parsing a list of IDs.
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.4
	 */
	private static class IDsHandler extends DefaultXMLHandler {
		/**
		 * <p>
		 * Max count of IDs.
		 * </p>
		 */
		private final long maxCount;
		
		/**
		 * <p>
		 * Count of IDs read.
		 * </p>
		 */
		private long count;

		/**
		 * <p>
		 * Cursor previous index.
		 * </p>
		 */
		private long cursorPrevIdx;

		/**
		 * <p>
		 * Cursor next index.
		 * </p>
		 */
		private long cursorNextIdx;

		/**
		 * <p>
		 * IDs list.
		 * </p>
		 */
		private Vector idsList = new Vector(20);
		
		/**
		 * <p>
		 * Create an instance of IDsHandler class.
		 * </p>
		 * @param maxCount Max count of IDs.
		 */
		public IDsHandler(long maxCount) {
			this.maxCount = maxCount;
		}
		
		/**
		 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
		 */
		public void text(String text) throws ParserException {
			text = text.trim();
			//
			if (xmlPath.equals("/id_list/ids/id")) {
				if (count < maxCount) {
					idsList.addElement(text);
					count++;
				}
			} else if (xmlPath.equals("/id_list/next_cursor")) {
				cursorNextIdx = Long.parseLong(text);
			} else if (xmlPath.equals("/id_list/previous_cursor")) {
				cursorPrevIdx = Long.parseLong(text);
			}
		}
		
		/**
		 * <p>
		 * Get IDs list.
		 * </p>
		 * @return List.
		 */
		public Vector getIDsList() {
			return idsList;
		}
		
		/**
		 * <p>
		 * Cursor previous index.
		 * </p>
		 * @return Index.
		 */
		public long getCursorPreviousIndex() {
			return cursorPrevIdx;
		}
		
		/**
		 * <p>
		 * Cursor next index.
		 * </p>
		 * @return Index.
		 */
		public long getCursorNextIndex() {
			return cursorNextIdx;
		}
		
		/**
		 * <p>
		 * Clear internal state of handler.
		 * </p>
		 */
		public void clear() {
			idsList.removeAllElements();
			cursorNextIdx = 0;
			cursorPrevIdx = 0;
		}
	}
}