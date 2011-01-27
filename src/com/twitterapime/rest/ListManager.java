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
	 * @see ListManager#create(List)
	 * @see ListManager#getLists()
	 * @see ListManager#getLists(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS =
		"TWITTER_API_URL_SERVICE_USER_LISTS";

	/**
	 * <p>
	 * Key for Twitter API URL service user lists update.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#update(List)
	 * @see ListManager#delete(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_ID =
		"TWITTER_API_URL_SERVICE_USER_LISTS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists memberships.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getMemberships()
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS =
		"TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service user lists subscriptions.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#getSubscriptions()
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS=
		"TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS";

	/**
	 * <p>
	 * Key for Twitter API URL service list membership management.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#addMember(List, UserAccount)
	 * @see ListManager#removeMember(List, UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS =
		"TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service list subscription management.
	 * </p>
	 * @see ListManager#setServiceURL(String, String)
	 * @see ListManager#subscribe(List)
	 * @see ListManager#unsubscribe(List)
	 */
	public static final String TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS=
		"TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS";

	static {
		SERVICES_URL = new Hashtable(6);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LISTS,
			"http://api.twitter.com/1/:user/lists.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LISTS_ID,
			"http://api.twitter.com/1/:user/lists/:id.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS,
			"http://api.twitter.com/1/:user/lists/memberships.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS,
			"http://api.twitter.com/1/:user/lists/subscriptions.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS,
			"http://api.twitter.com/1/:user/:list_id/members.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS,
			"http://api.twitter.com/1/:user/:list_id/subscribers.xml");
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
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LISTS
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LISTS_ID
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS
	 * @see ListManager#TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS
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
	 * @throws IllegalArgumentException If list or list's name is null.
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
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS,
				HttpConnection.POST,
				getUsernameFromCredential(),
				null);
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
	 * @throws IllegalArgumentException If list or list's id is null.
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
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS_ID,
				HttpConnection.POST,
				getUsernameFromCredential(),
				list.getString(MetadataSet.LIST_ID));
		//
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
	 * @throws IllegalArgumentException If list or list's id is null.
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
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS_ID,
				HttpConnection.POST,
				getUsernameFromCredential(),
				list.getString(MetadataSet.LIST_ID));
		//
		req.setBodyParameter("_method", "DELETE");
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
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user or username is null.
	 */
	public List[] getMemberships(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS_MEMBERSHIPS,
				null,
				user.getString(MetadataSet.USERACCOUNT_USER_NAME),
				null);
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
	 * @return Lists.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user or username is null.
	 */
	public List[] getSubscriptions(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS_SUBSCRIPTIONS,
				null,
				user.getString(MetadataSet.USERACCOUNT_USER_NAME),
				null);
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
	 * @throws IllegalArgumentException If list, list's id or username is null.
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
		list.checkEmpty(MetadataSet.LIST_USER_ACCOUNT);
		list.getUserAccount().checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS,
				HttpConnection.POST,
				getUsernameFromList(list),
				list.getString(MetadataSet.LIST_ID));
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
	 * @throws IllegalArgumentException If list, list's id or name is null.
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
		list.checkEmpty(MetadataSet.LIST_USER_ACCOUNT);
		list.getUserAccount().checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LIST_ID_SUBSCRIBERS,
				HttpConnection.POST,
				getUsernameFromList(list),
				list.getString(MetadataSet.LIST_ID));
		//
		req.setBodyParameter("list_id", list.getString(MetadataSet.LIST_ID));
		req.setBodyParameter("_method", "DELETE");
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
	 * @throws IllegalArgumentException If list, list's, user, user's name and
	 *                                  id id is null.
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
		user.checkEmpty(MetadataSet.USERACCOUNT_ID);
		//
		String listid = list.getString(MetadataSet.LIST_ID);
		String userid = user.getString(MetadataSet.USERACCOUNT_ID);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS,
				HttpConnection.POST,
				getUsernameFromCredential(),
				listid);
		//
		req.setBodyParameter("list_id", listid);
		req.setBodyParameter("id", userid);
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
	 * @throws IllegalArgumentException If list, list's, user, user's name and
	 *                                  id id is null.
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
		user.checkEmpty(MetadataSet.USERACCOUNT_ID);
		//
		String listid = list.getString(MetadataSet.LIST_ID);
		String userid = user.getString(MetadataSet.USERACCOUNT_ID);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS,
				HttpConnection.POST,
				getUsernameFromCredential(),
				listid);
		//
		req.setBodyParameter("list_id", listid);
		req.setBodyParameter("id", userid);
		req.setBodyParameter("_method", "DELETE");
		//
		return processRequest(req)[0];
	}
	
//	public UserAccount[] getMembers(List list) throws IOException,
//		LimitExceededException {
//		checkUserAuth();
//		//
//		if (list == null) {
//			throw new IllegalArgumentException("List must not be null.");
//		}
//		//
//		list.checkEmpty(MetadataSet.LIST_ID);
//		list.checkEmpty(MetadataSet.LIST_USER_ACCOUNT);
//		list.getUserAccount().checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
//		//
//		HttpRequest req =
//			createRequest(
//				TWITTER_API_URL_SERVICE_USER_LIST_ID_MEMBERS,
//				null,
//				getUsernameFromList(list),
//				list.getString(MetadataSet.LIST_ID));
//		//
//		HttpResponse s = req.send();
//		System.out.println(s.getBodyContent());
//		//
//		return null;
//	}
	
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
	 * @throws IllegalArgumentException If user or username is null.
	 */
	public List[] getLists(UserAccount user) throws IOException,
		LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		//
		user.checkEmpty(MetadataSet.USERACCOUNT_USER_NAME);
		//
		HttpRequest req =
			createRequest(
				TWITTER_API_URL_SERVICE_USER_LISTS,
				null,
				user.getString(MetadataSet.USERACCOUNT_USER_NAME),
				null);
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
	 * Get the username from user's credential.
	 * </p>
	 * @return Username.
	 */
	private String getUsernameFromCredential() {
		Credential c = userAccountMngr.getCredential();
		//
		return c.getString(MetadataSet.CREDENTIAL_USERNAME);
	}

	/**
	 * <p>
	 * Get the username from user's list.
	 * </p>
	 * @param list List.
	 * @return Username.
	 */
	private String getUsernameFromList(List list) {
		UserAccount ua = list.getUserAccount();
		//
		return ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
	}

	/**
	 * <p>
	 * Create an UserAccount object from user's credential.
	 * </p>
	 * @return UserAccount object.
	 */
	private UserAccount getUserAccountFromCredential() {
		return new UserAccount(getUsernameFromCredential());
	}
	
	/**
	 * <p>
	 * Create a request to a given URL.
	 * </p>
	 * @param urlKey URL key.
	 * @param method Method.
	 * @param username Username.
	 * @param listid List Id.
	 * @return Request.
	 */
	private HttpRequest createRequest(String urlKey, String method,
		String username, String listid) {
		String url = getURL(urlKey);
		//
		if (username != null) {
			url = StringUtil.replace(url, ":user", username);
		}
		if (listid != null) {
			url = StringUtil.replace(url, ":list_id", listid);
			url = StringUtil.replace(url, ":id", listid);
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
		if (method != null) {
			req.setMethod(method);
		}
		//
		return req;
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
