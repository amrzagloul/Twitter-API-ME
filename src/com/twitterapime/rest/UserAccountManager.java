/*
 * UserAccountManager.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpConnector;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.XAuthSigner;

/**
 * <p>
 * This class is responsible for managing the user account.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential("username", "password", "consKey", "consSec");
 * UserAccountManager uam = UserAccountManager.getInstance(c)
 * if (uam.verifyCredential()) {
 *   System.out.println("User logged in...");
 * }
 * uam.signOut();
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class UserAccountManager {
	/**
	 * <p>
	 * UserAccountManager pool used to cache instanced associated to user
	 * credentials.
	 * </p>
	 */
	private static Hashtable userAccountMngrPoll;
	
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * Key for Twitter API URL service account verify credentials.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS";

	/**
	 * <p>
	 * Key for Twitter API URL service OAuth access token.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN =
		"TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN";
	
	/**
	 * <p>
	 * Key for Twitter API URL service account rate limit status.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getRateLimitStatus()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships create.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#follow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships destroy.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#unfollow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#isFollowing(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service blocks create.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#block(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_CREATE =
		"TWITTER_API_URL_SERVICE_BLOCKS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service blocks destroy.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#unblock(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_DESTROY =
		"TWITTER_API_URL_SERVICE_BLOCKS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#isBlocking(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_EXISTS =
		"TWITTER_API_URL_SERVICE_BLOCKS_EXISTS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service users show.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getUserAccount()
	 */
	public static final String TWITTER_API_URL_SERVICE_USERS_SHOW =
		"TWITTER_API_URL_SERVICE_USERS_SHOW";
	
	static {
		SERVICES_URL = new Hashtable(10);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS,
			"http://api.twitter.com/1/account/verify_credentials.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN,
			"https://api.twitter.com/oauth/access_token");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS,
			"http://api.twitter.com/1/account/rate_limit_status.xml");
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
			TWITTER_API_URL_SERVICE_BLOCKS_CREATE,
			"http://api.twitter.com/1/blocks/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_DESTROY,
			"http://api.twitter.com/1/blocks/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_EXISTS,
			"http://api.twitter.com/1/blocks/exists/");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USERS_SHOW,
			"http://api.twitter.com/1/users/show.xml");
	}

	/**
	 * <p>
	 * User's credentials.
	 * </p>
	 */
	private Credential credential;
	
	/**
	 * <p>
	 * Flag that indicates whether the user's credentials were verified.
	 * </p>
	 */
	private boolean verified;
	
	/**
	 * <p>
	 * Marks the instance as invalidated.
	 * </p>
	 */
	private boolean invalidated;
	
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private Token token;
	
	/**
	 * <p>
	 * XAuth signer instance.
	 * </p>
	 */
	private XAuthSigner signer;
	
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
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_BLOCKS_CREATE
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_BLOCKS_DESTROY
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_BLOCKS_EXISTS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_USERS_SHOW
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of UserAccountManager class and associate it to a given
	 * user credential.
	 * </p>
	 * @param c Credentials.
	 * @return AccountManager object.
	 * @throws IllegalArgumentException If credential is null.
	 */
	public synchronized static UserAccountManager getInstance(Credential c) {
		if (c == null) {
			throw new IllegalArgumentException("Credential must not be null.");
		}
		//
		UserAccountManager uam = null;
		//
		if (userAccountMngrPoll == null) {
			userAccountMngrPoll = new Hashtable();
		} else {
			synchronized (userAccountMngrPoll) {
				uam = (UserAccountManager)userAccountMngrPoll.get(c);
			}
		}
		//
		return uam != null ? uam : new UserAccountManager(c);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccountManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param c User's credential.
	 */
	private UserAccountManager(Credential c) {
		credential = c;
		//
		if (c.hasXAuthCredentials()) {
			String conKey = c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY);
			String conSec = c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET);
			//
			signer = new XAuthSigner(conKey, conSec);
		}
	}

	/**
	 * <p>
	 * Returns a set of info about the number of API requests available to the
	 * requesting user before the REST API limit is reached for the current
	 * hour.
	 * </p>
	 * <p>
	 * Stay aware of these limits, since it can impact the usage of some methods
	 * of this API.
	 * </p>
	 * @return Rate limiting status info.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public RateLimitStatus getRateLimitStatus() throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS));
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			RateLimitStatusHandler handler = new RateLimitStatusHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedRateLimitStatus();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Return whether it is properly verified.
	 * </p>
	 * @return Verified (true).
	 */
	public boolean isVerified() {
		checkValid();
		//
		return verified;
	}
	
	/**
	 * <p>
	 * Verify whether the given user's credential are valid. This method
	 * authenticates the API to Twitter REST API.
	 * </p>
	 * @return Valid credentials (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public boolean verifyCredential() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			return true; //already verified.
		}
		//
		HttpRequest req;
		if (credential.hasXAuthCredentials()) {
			String user = credential.getString(MetadataSet.CREDENTIAL_USERNAME);
			String pass = credential.getString(MetadataSet.CREDENTIAL_PASSWORD);
			//
			req = createRequest(
				getURL(TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN));
			req.setMethod(HttpConnection.POST);
			//
			signer.signForAccessToken(req, user, pass);
		} else {
			req = createRequest(
				getURL(TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS));
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_OK) {
				if (credential.hasXAuthCredentials()) {
					token = Token.parse(resp.getBodyContent()); //access token.
				}
				verified = true;
				//
				saveSelfOnPool();
			} else if (resp.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
				verified = false;
			} else {
				HttpResponseCodeInterpreter.perform(resp);
			}
		} finally {
			req.close();
		}
		//
		return verified;
	}
	
	/**
	 * <p>
	 * Ends the session of the authenticating user.
	 * </p>
	 * <p>
	 * Once signed out, this instance is no longer valid for use as well as
	 * another one dependent of it. Dump them!
	 * </p>
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public synchronized void signOut() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			verified = false;
			token = null;
			signer = null;
			userAccountMngrPoll.remove(credential);
			Timeline.cleanPool();
			TweetER.cleanPool();
			//
			invalidated = true;
		}
	}
	
	/**
	 * <p>
	 * Get the user account.
	 * </p>
	 * @return User account object.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount getUserAccount() throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		String username = credential.getString(MetadataSet.CREDENTIAL_USERNAME);
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_USERS_SHOW) + "?id=" + username);
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
			return handler.getParsedUserAccount();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
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
		return manageFriendship(TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY, ua);
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
		checkValid();
		//
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		String id = ua.getString(MetadataSet.USERACCOUNT_ID);
		if (id == null || (id = id.trim()).length() == 0) {
			id = ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
			if (id == null || (id = id.trim()).length() == 0) {
				throw new IllegalArgumentException(
					"Username or ID must not be empty/null.");
			}
		}
		//
		checkVerified();
		//
		final String qryStr =
			"?user_a=" + credential.getString(MetadataSet.CREDENTIAL_USERNAME) +
			"&user_b=" + id;
		//
		HttpRequest req = createRequest(
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
		checkValid();
		//
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		String id = ua.getString(MetadataSet.USERACCOUNT_ID);
		if (id == null || (id = id.trim()).length() == 0) {
			id = ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
			if (id == null || (id = id.trim()).length() == 0) {
				throw new IllegalArgumentException(
					"Username or ID must not be empty/null.");
			}
		}
		//
		checkVerified();
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_BLOCKS_EXISTS) + id + ".xml");
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof UserAccountManager)) {
			return false;
		} else {
			return credential.equals(((UserAccountManager)o).credential);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return credential.hashCode();
	}
	
	/**
	 * <p>
	 * Get the user's credentials.
	 * </p>
	 * @return Credential object.
	 */
	Credential getCredential() {
		return credential;
	}
	
	/**
	 * <p>
	 * Create a HttpRequest object.
	 * </p>
	 * @param url URL.
	 * @param method Http method.
	 * @return Request object.
	 */
	synchronized HttpRequest createRequest(String url) {
		HttpRequest req = new HttpRequest(url);
		//
		if (credential.hasXAuthCredentials()) {
			if (token != null) {
				req.setSigner(signer, token);
			}
		} else {
			String crdntls = credential.getBasicHttpAuthCredential();
			crdntls = HttpConnector.encodeBase64(crdntls);
			req.setHeaderField("Authorization", "Basic " + crdntls);
		}
		//
		return req;
	}

	/**
	 * <p>
	 * Check whether the instance is still valid.
	 * </p>
	 * @throws IllegalStateException Instance invalidated.
	 */
	private synchronized void checkValid() {
		if (invalidated) {
			throw new IllegalStateException(
				"This instance is no longer valid. Get a new one!");
		}
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
		checkValid();
		//
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		String id = ua.getString(MetadataSet.USERACCOUNT_ID);
		if (id == null || (id = id.trim()).length() == 0) {
			id = ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
			if (id == null || (id = id.trim()).length() == 0) {
				throw new IllegalArgumentException(
					"Username or ID must not be empty/null.");
			}
		}
		//
		checkVerified();
		//
		HttpRequest req = createRequest(getURL(servURLKey));
		req.setMethod(HttpConnection.POST);
		try {
			Long.parseLong(id); // is only numbers?
			req.setBodyParameter("user_id", id);
		} catch (NumberFormatException e) {
			req.setBodyParameter("screen_name", id); //user name.
		}
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
			return handler.getParsedUserAccount();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Check whether it is verified.
	 * </p>
	 * @throws SecurityException If it is not properly verified.
	 */
	private void checkVerified() {
		if (!verified) {
			throw new SecurityException(
				"User's credentials have not been verified yet.");
		}
	}

	/**
	 * <p>
	 * Save the instance on pool.
	 * </p>
	 */
	private synchronized void saveSelfOnPool() {
		if (userAccountMngrPoll.get(credential) == null) {
			userAccountMngrPoll.put(credential, this);
		}
	}
}