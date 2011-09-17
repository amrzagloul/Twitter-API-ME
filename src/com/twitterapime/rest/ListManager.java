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

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.rest.handler.ListHandler;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.SearchDeviceListener;

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
 * @version 1.1
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
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/%3Auser/lists" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/%3Auser/lists
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS =
		"TWITTER_API_URL_SERVICE_USER_LISTS";

	/**
	 * <p>
	 * Key for Twitter API URL service user lists update.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/%3Auser/lists/%3Aid" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/%3Auser/lists/%3Aid
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_ID =
		"TWITTER_API_URL_SERVICE_USER_LISTS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists memberships.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/%3Auser/lists/memberships" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/%3Auser/lists/memberships
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS =
		"TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service list membership management.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/%3Auser/%3Alist_id/members" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/%3Auser/%3Alist_id/members
	 * </a>
	 * <a href="http://dev.twitter.com/docs/api/1/get/%3Auser/%3Alist_id/members" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/%3Auser/%3Alist_id/members
	 * </a>
	 * <a href="http://dev.twitter.com/docs/api/1/delete/%3Auser/%3Alist_id/members" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/delete/%3Auser/%3Alist_id/members
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS =
		"TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists subscriptions.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/%3Auser/lists/subscriptions" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/%3Auser/lists/subscriptions
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS=
		"TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service list subscription management.
	 * </p>
	 * <p>
	 * <a href="https://dev.twitter.com/docs/api/1/post/:user/:list_id/subscribers" target="_blank">
	 *   https://dev.twitter.com/docs/api/1/post/:user/:list_id/subscribers
	 * </a>
	 * <a href="https://dev.twitter.com/docs/api/1/get/:user/:list_id/subscribers" target="_blank">
	 *   https://dev.twitter.com/docs/api/1/get/:user/:list_id/subscribers
	 * </a>
	 * <a href="https://dev.twitter.com/docs/api/1/delete/:user/:list_id/subscribers" target="_blank">
	 *   https://dev.twitter.com/docs/api/1/delete/:user/:list_id/subscribers
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @deprecated
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS=
		"TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS";

	/**
	 * <p>
	 * Key for Twitter API URL service lists members destroy.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/members/destroy" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/members/destroy
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#removeMember(List, UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_MEMBERS_DESTROY =
		"TWITTER_API_URL_SERVICE_LISTS_MEMBERS_DESTROY";

	/**
	 * <p>
	 * Key for Twitter API URL service lists members.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/lists/members" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/lists/members
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getMembers(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_MEMBERS =
		"TWITTER_API_URL_SERVICE_LISTS_MEMBERS";

	/**
	 * <p>
	 * Key for Twitter API URL service lists members create.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/members/create" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/members/create
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#addMember(List, UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_MEMBERS_CREATE =
		"TWITTER_API_URL_SERVICE_LISTS_MEMBERS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists subscribers destroy.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/subscribers/destroy" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/subscribers/destroy
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#unsubscribe(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_DESTROY =
		"TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists subscribers create.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/subscribers/create" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/subscribers/create
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#subscribe(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_CREATE=
		"TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_CREATE";

	/**
	 * <p>
	 * Key for Twitter API URL service lists subscribers.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/lists/subscriptions" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/lists/subscriptions
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getSubscribers(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS =
		"TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists destroy.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/destroy" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/destroy
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#delete(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_DESTROY =
		"TWITTER_API_URL_SERVICE_LISTS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists update.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/update" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/update
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#update(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_UPDATE =
		"TWITTER_API_URL_SERVICE_LISTS_UPDATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists all.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/lists/all" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/lists/all
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getSubscriptions()
	 * @see ListManager#getSubscriptions(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_ALL =
		"TWITTER_API_URL_SERVICE_LISTS_ALL";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists create.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/post/lists/create" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/post/lists/create
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#create(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_CREATE =
		"TWITTER_API_URL_SERVICE_LISTS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service lists memberships.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/lists/memberships" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/lists/memberships
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getMemberships()
	 * @see ListManager#getMemberships(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS_MEMBERSHIPS =
		"TWITTER_API_URL_SERVICE_LISTS_MEMBERSHIPS";

	/**
	 * <p>
	 * Key for Twitter API URL service lists.
	 * </p>
	 * <p>
	 * <a href="http://dev.twitter.com/docs/api/1/get/lists" target="_blank">
	 *   http://dev.twitter.com/docs/api/1/get/lists
	 * </a>
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getLists()
	 * @see ListManager#getLists(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_LISTS =
		"TWITTER_API_URL_SERVICE_LISTS";
	
	static {
		SERVICES_URL = new Hashtable(12);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_MEMBERS_DESTROY,
			"http://api.twitter.com/1/lists/members/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_MEMBERS,
			"http://api.twitter.com/1/lists/members.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_MEMBERS_CREATE,
			"http://api.twitter.com/1/lists/members/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_DESTROY,
			"http://api.twitter.com/1/lists/subscribers/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_CREATE,
			"http://api.twitter.com/1/lists/subscribers/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS,
			"http://api.twitter.com/1/lists/subscribers.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_DESTROY,
			"http://api.twitter.com/1/lists/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_UPDATE,
			"http://api.twitter.com/1/lists/update.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_ALL,
			"http://api.twitter.com/1/lists/all.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_CREATE,
			"http://api.twitter.com/1/lists/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS_MEMBERSHIPS,
			"http://api.twitter.com/1/lists/memberships.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_LISTS,
			"http://api.twitter.com/1/lists.xml");
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

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
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_MEMBERS
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_MEMBERS_CREATE
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_MEMBERS_DESTROY
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_CREATE
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_DESTROY
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_DESTROY
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_UPDATE
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_ALL
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_CREATE
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS_MEMBERSHIPS
	 * @see ListManager#TWITTER_API_URL_SERVICE_LISTS
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}

	/**
	 * <p>
	 * Creates a new list for the authenticated user.
	 * </p>
	 * @param list List to be created.
	 * @return List created.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list is null.
	 */
	public List create(List list) throws IOException, LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_NAME);
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_CREATE));
		req.setMethod(HttpConnection.POST);
		//
		req.setBodyParameter("name", list.getString(MetadataSet.LIST_NAME));
		if (!list.isEmpty(MetadataSet.LIST_MODE)) {
			req.setBodyParameter("mode", list.getString(MetadataSet.LIST_MODE));
		}
		if (!list.isEmpty(MetadataSet.LIST_DESCRIPTION)) {
			req.setBodyParameter(
				"description", list.getString(MetadataSet.LIST_DESCRIPTION));
		}
		//
		return processRequest(req)[0];
	}
	
	/**
	 * <p>
	 * Updates the given list belonged to authenticated user.
	 * </p>
	 * @param list List to be updated.
	 * @return List updated.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list is null.
	 */
	public List update(List list) throws IOException, LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_UPDATE));
		req.setMethod(HttpConnection.POST);
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		if (!list.isEmpty(MetadataSet.LIST_NAME)) {
			req.setBodyParameter("name", list.getString(MetadataSet.LIST_NAME));
		}
		if (!list.isEmpty(MetadataSet.LIST_MODE)) {
			req.setBodyParameter("mode", list.getString(MetadataSet.LIST_MODE));
		}
		if (!list.isEmpty(MetadataSet.LIST_DESCRIPTION)) {
			req.setBodyParameter(
				"description", list.getString(MetadataSet.LIST_DESCRIPTION));
		}
		//
		return processRequest(req)[0];
	}
	
	/**
	 * <p>
	 * Deletes the given list, which must be owned by the authenticated user.
	 * </p>
	 * @param list List to be deleted.
	 * @return List deleted.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list is null.
	 */
	public List delete(List list) throws IOException, LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_DESTROY));
		req.setMethod(HttpConnection.POST);
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		//
		return processRequest(req)[0];
	}
	
	/**
	 * <p>
	 * Get the lists the authenticated user has been added to.
	 * </p>
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 */
	public List[] getMemberships() throws IOException, LimitExceededException {
		checkUserAuth();
		//
		return getMemberships(getUserAccountFromCredential());
	}
	
	/**
	 * <p>
	 * Get the lists the given user has been added to.
	 * </p>
	 * @param user User.
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user is null.
	 */
	public List[] getMemberships(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		try {
			user.checkEmpty(MetadataSet.USERACCOUNT_ID);	
		} catch (IllegalArgumentException e) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_LISTS_MEMBERSHIPS);
		//
		HttpRequest req;
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		String[] usrp = user.getUserNameOrIDParamValue();
		//
		req.setBodyParameter(usrp[0], usrp[1]);
		//
		return processRequest(req);
	}
	
	/**
	 * <p>
	 * Get the lists the authenticated user follows.
	 * </p>
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 */
	public List[] getSubscriptions() throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return getSubscriptions(getUserAccountFromCredential());
	}
	
	/**
	 * <p>
	 * Get the lists the given user follows.
	 * </p>
	 * @param user User.
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user is null.
	 */
	public List[] getSubscriptions(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		try {
			user.checkEmpty(MetadataSet.USERACCOUNT_ID);	
		} catch (IllegalArgumentException e) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_LISTS_ALL);
		//
		HttpRequest req;
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		String[] usrp = user.getUserNameOrIDParamValue();
		//
		req.setBodyParameter(usrp[0], usrp[1]);
		//
		return processRequest(req);
	}
	
	/**
	 * <p>
	 * Make the authenticated user follow the given list.
	 * </p>
	 * @param list List to follow.
	 * @return List followed.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list is null.
	 */
	public List subscribe(List list) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_CREATE));
		req.setMethod(HttpConnection.POST);
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		//
		return processRequest(req)[0];
	}

	/**
	 * <p>
	 * Unsubscribes the authenticated user from the given list.
	 * </p>
	 * @param list List to stop following.
	 * @return List no longer followed.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list is null.
	 */
	public List unsubscribe(List list) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS_DESTROY));
		req.setMethod(HttpConnection.POST);
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		//
		return processRequest(req)[0];
	}

	/**
	 * <p>
	 * Add a member to a list. The authenticated user must own the list to be
	 * able to add members to it.
	 * </p>
	 * @param list List.
	 * @param user User to be added.
	 * @return List list where the user was added to.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list or user is null.
	 */
	public List addMember(List list, UserAccount user)
		throws IOException, LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		try {
			user.checkEmpty(MetadataSet.USERACCOUNT_ID);	
		} catch (IllegalArgumentException e) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_MEMBERS_CREATE));
		req.setMethod(HttpConnection.POST);
		//
		String[] usrp = user.getUserNameOrIDParamValue();
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		req.setBodyParameter(usrp[0], usrp[1]);
		//
		return processRequest(req)[0];
	}

	/**
	 * <p>
	 * Removes the given user from the list. The authenticated user must be the
	 * list's owner to remove members from the list.
	 * </p>
	 * @param list List.
	 * @param user User to be removed.
	 * @return List List where the user was removed from.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If list or user is null.
	 */
	public List removeMember(List list, UserAccount user)
		throws IOException, LimitExceededException {
		checkUserAuth();
		//
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		try {
			user.checkEmpty(MetadataSet.USERACCOUNT_ID);	
		} catch (IllegalArgumentException e) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		HttpRequest req =
			userAccountMngr.createRequest(
				getURL(TWITTER_API_URL_SERVICE_LISTS_MEMBERS_DESTROY));
		req.setMethod(HttpConnection.POST);
		//
		String[] usrp = user.getUserNameOrIDParamValue();
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		req.setBodyParameter(usrp[0], usrp[1]);
		//
		return processRequest(req)[0];
	}
	
	/**
	 * <p>
	 * Get the members of the given list. Members from a private list just can
	 * be accessed by the owner's (authenticated) list.
	 * </p>
	 * @param list List.
	 * @return Members.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If list is null.
	 */
	public UserAccount[] getMembers(List list) throws IOException,
		LimitExceededException {
		return processMembersSubscribersRequest(
			list, TWITTER_API_URL_SERVICE_LISTS_MEMBERS);
	}

	/**
	 * <p>
	 * Get the subscribers of the given public list. Private list there is no
	 * subscribers, since it is only access by the owner itself.
	 * </p>
	 * @param list List.
	 * @return Subscribers.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If list is null.
	 */
	public UserAccount[] getSubscribers(List list) throws IOException,
		LimitExceededException {
		return processMembersSubscribersRequest(
			list, TWITTER_API_URL_SERVICE_LISTS_SUBSCRIBERS);
	}

	/**
	 * <p>
	 * Get the lists (public and privates) of the authenticated user.
	 * </p>
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 */
	public List[] getLists() throws IOException, LimitExceededException {
		checkUserAuth();
		//
		return getLists(getUserAccountFromCredential());
	}

	/**
	 * <p>
	 * Get the lists (public) of the given user. If the given user is same as
	 * authenticated user, private lists will also be returned.
	 * </p>
	 * @param user User account object.
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user is null.
	 */
	public List[] getLists(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		try {
			user.checkEmpty(MetadataSet.USERACCOUNT_ID);	
		} catch (IllegalArgumentException e) {
			user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_LISTS);
		//
		HttpRequest req;
		if (userAccountMngr != null) {
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		String[] usrp = user.getUserNameOrIDParamValue();
		//
		req.setBodyParameter(usrp[0], usrp[1]);
		//
		return processRequest(req);
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
	 * Create an UserAccount object from user's credential.
	 * </p>
	 * @return UserAccount object.
	 */
	private UserAccount getUserAccountFromCredential() {
		return new UserAccount(
			userAccountMngr.getCredential().getString(
				MetadataSet.CREDENTIAL_USERNAME));
	}
	
	/**
	 * <p>
	 * Process the given request.
	 * </p>
	 * @param req Request.
	 * @return List array.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	private List[] processRequest(HttpRequest req) throws IOException,
		LimitExceededException {
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
	 * <p>
	 * Process the request related to members and subscribers.
	 * </p>
	 * @param list List.
	 * @param urlKey Url Key.
	 * @return Members or subscribers..
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If list is null.
	 */
	private UserAccount[] processMembersSubscribersRequest(List list,
		String urlKey) throws IOException, LimitExceededException {
		if (list == null) {
			throw new IllegalArgumentException("List must not be null.");
		}
		//
		list.checkEmpty(MetadataSet.LIST_ID);
		//
		String url =
			getURL(urlKey) + 
			"?skip_status=true&list_id=" + list.getString(MetadataSet.LIST_ID);
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
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedUserAccounts();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
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
