/*
 * ListManager.java
 * 23/11/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.ListHandler;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines the methods responsible for managing Lists.
 * </p>
 * <p>
 * <pre>
 * ...
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   ListManager listMngr = ListManager.getInstance(uam);
 *   List[] lists = listMngr.getLists();
 * }
 * ...
 * ListManager listMngr = ListManager.getInstance();
 * List[] lists = listMngr.getLists(new UserAccount("username"));
 * ...
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.6
 */
public final class ListManager {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * ListManager pool used to cache instanced associated to user
	 * accounts.
	 * </p>
	 */
	private static Hashtable listsMngrPool;

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static ListManager singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getLists()
	 * @see ListManager#getLists(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS =
		"TWITTER_API_URL_SERVICE_USER_LISTS";

	static {
		SERVICES_URL = new Hashtable(4);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LISTS,
			"http://api.twitter.com/1/:user/lists.xml");
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
		if (listsMngrPool != null) {
			Enumeration keys = listsMngrPool.keys();
			Object key;
			ListManager value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (ListManager)listsMngrPool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					listsMngrPool.remove(key);
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
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LISTS
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of ListManager class and associate it to a given
	 * user account.
	 * </p>
	 * @param uam User account manager.
	 * @return ListManager instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static ListManager getInstance(
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
		if (listsMngrPool == null) {
			listsMngrPool = new Hashtable();
		}
		//
		ListManager fsmr = (ListManager)listsMngrPool.get(uam);
		if (fsmr == null) {
			fsmr = new ListManager(uam);
			listsMngrPool.put(uam, fsmr);
		}
		//
		return fsmr;
	}
	
	/**
	 * <p>
	 * Get a single instance of ListManager class, which is NOT associated
	 * to any user account.
	 * </p>
	 * @return ListManager single instance.
	 */
	public synchronized static ListManager getInstance() {
		if (singleInstance == null) {
			singleInstance = new ListManager();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * Create an instance of ListManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private ListManager() {
	}
	
	/**
	 * <p>
	 * Create an instance of ListManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private ListManager(UserAccountManager uam) {
		userAccountMngr = uam;
	}
	
	/**
	 * <p>
	 * Get the lists (public and privates) of the authenticated user.
	 * </p>
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 */
	public List[] getLists() throws IOException, LimitExceededException {
		checkUserAuth();
		//
		return getLists(userAccountMngr.getUserAccount());
	}

	/**
	 * <p>
	 * Get the lists (public) of the given user. If the given user is same as
	 * authenticated user, private lists will also be returned.
	 * </p>
	 * @param user User account object.
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user account is null.
	 */
	public List[] getLists(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		String username = user.getString(MetadataSet.USERACCOUNT_USER_NAME);
		//
		if (StringUtil.isEmpty(username)) {
			throw new IllegalArgumentException(
				"User account's name must not be null.");
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_USER_LISTS);
		//
		url = StringUtil.replace(url, ":user", username);
		//
		HttpRequest req;
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
			ListHandler handler = new ListHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedLists();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * {@link Timeline#startGetListTweets(List, Query, SearchDeviceListener)}
	 */
	public void startGetListTweets(List list, Query q, SearchDeviceListener l) {
		Timeline tml;
		//
		if (userAccountMngr != null) {
			tml = Timeline.getInstance(userAccountMngr);
		} else {
			tml = Timeline.getInstance();
		}
		//
		tml.startGetListTweets(list, q, l);
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
}