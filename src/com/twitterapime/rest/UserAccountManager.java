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
 * Credential c = new Credential("username", "password");
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
	 * Twitter REST API verify credentials URI.
	 * </p>
	 */
	private static final String TWITTER_URL_VERIFY_CREDENTIALS =
		"http://twitter.com/account/verify_credentials.xml";

	/**
	 * <p>
	 * Twitter REST API verify XAuth credentials URI.
	 * </p>
	 */
	private static final String TWITTER_URL_VERIFY_XAUTH_CREDENTIALS =
		"https://api.twitter.com/oauth/access_token";
	
	/**
	 * <p>
	 * Twitter REST API rate status limit URI.
	 * </p>
	 */
	private static final String TWITTER_URL_RATE_STATUS_LIMIT =
		"http://twitter.com/account/rate_limit_status.xml";
	
	/**
	 * <p>
	 * Twitter REST API follow user URI.
	 * </p>
	 */
	private static final String TWITTER_URL_FOLLOW_USER =
		"http://api.twitter.com/1/friendships/create.xml";
	
	/**
	 * <p>
	 * Twitter REST API unfollow user URI.
	 * </p>
	 */
	private static final String TWITTER_URL_UNFOLLOW_USER =
		"http://api.twitter.com/1/friendships/destroy.xml";
	
	/**
	 * <p>
	 * Twitter REST API users friendship checking URI.
	 * </p>
	 */
	private static final String TWITTER_URL_IS_FOLLOWING_USER =
		"http://api.twitter.com/1/friendships/exists.json";
	
	/**
	 * <p>
	 * Twitter REST API block user URI.
	 * </p>
	 */
	private static final String TWITTER_URL_BLOCK_USER =
		"http://api.twitter.com/1/blocks/create.xml";
	
	/**
	 * <p>
	 * Twitter REST API unblock user URI.
	 * </p>
	 */
	private static final String TWITTER_URL_UNBLOCK_USER =
		"http://api.twitter.com/1/blocks/destroy.xml";
	
	/**
	 * <p>
	 * Twitter REST API users blocking checking URI.
	 * </p>
	 */
	private static final String TWITTER_URL_IS_BLOCKING_USER =
		"http://api.twitter.com/1/blocks/exists/";
	
	/**
	 * <p>
	 * Twitter REST API show user account.
	 * </p>
	 */
	private static final String TWITTER_URL_SHOW_USER_ACCOUNT =
		"http://api.twitter.com/version/users/show.xml";
	
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
	 * User account data.
	 * </p>
	 */
	private UserAccount account;
	
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
		HttpRequest req = createRequest(TWITTER_URL_RATE_STATUS_LIMIT);
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
			req = createRequest(TWITTER_URL_VERIFY_XAUTH_CREDENTIALS);
			req.setMethod(HttpConnection.POST);
			//
			signer.signForAccessToken(req, user, pass);
		} else {
			req = createRequest(TWITTER_URL_VERIFY_CREDENTIALS);
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
			account = null;
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
		HttpRequest req = createRequest(TWITTER_URL_SHOW_USER_ACCOUNT);
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
		return manageFriendship(TWITTER_URL_FOLLOW_USER, ua);
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
		return manageFriendship(TWITTER_URL_UNFOLLOW_USER, ua);
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
			"?user_a=" + account.getString(MetadataSet.USERACCOUNT_USER_NAME) +
			"&user_b=" + id;
		//
		HttpRequest req = createRequest(TWITTER_URL_IS_FOLLOWING_USER + qryStr);
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
		return manageFriendship(TWITTER_URL_BLOCK_USER, ua);
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
		return manageFriendship(TWITTER_URL_UNBLOCK_USER, ua);
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
		HttpRequest req =
			createRequest(TWITTER_URL_IS_BLOCKING_USER + id + ".xml");
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
	 * @param actionUrl Action's URL to be performed.
	 * @param ua UserAccount object containing the user name or ID.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already affected by the action or does
	 *         not exist.
	 * @throws SecurityException If the user is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	private UserAccount manageFriendship(String actionUrl, UserAccount ua)
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
		HttpRequest req = createRequest(actionUrl);
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