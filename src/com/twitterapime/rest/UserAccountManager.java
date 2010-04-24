/*
 * UserAccountManager.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpConnector;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;

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
 * @version 1.1
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
	 * Create a Http connection to the given URL.
	 * </p>
	 * @param url URL.
	 * @param c User's credential for log in purposes.
	 * @return Http connection.
	 * @throws IOException If an I/O error occurs.
	 */
	static synchronized HttpConnection getHttpConn(String url, Credential c)
		throws IOException {
		if (url == null || (url = url.trim()).length() == 0) {
			throw new IllegalArgumentException("URL must not be empty/null.");
		}
		//
		HttpConnection conn = HttpConnector.open(url);
		boolean hasException = true;
		//
		try {
			if (c != null) {
				String crdntls = c.getBasicHttpAuthCredential();
				crdntls = HttpConnector.encodeBase64(crdntls);
				conn.setRequestProperty("Authorization", "Basic " + crdntls);
			}
			//
			hasException = false;
		} finally {
			if (hasException) {
				conn.close();
			}
		}
		//
		return conn;
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
		HttpConnection conn =
			getHttpConn(TWITTER_URL_RATE_STATUS_LIMIT, credential);
		Parser parser = ParserFactory.getDefaultParser();
		RateLimitStatusHandler handler = new RateLimitStatusHandler();
		//
		try {
			HttpResponseCodeInterpreter.perform(conn);
			//
			parser.parse(conn.openInputStream(), handler);
			//
			return handler.getParsedRateLimitStatus();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
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
		HttpConnection conn =
			getHttpConn(TWITTER_URL_VERIFY_CREDENTIALS, credential);
		//
		try {
			final int respCode = conn.getResponseCode();
			//
			if (respCode == HttpConnection.HTTP_OK) {
				verified = true;
				//
				try {
					Parser parser =	ParserFactory.getDefaultParser();
					AccountHandler handler = new AccountHandler();
					parser.parse(conn.openInputStream(), handler);
					//
					account = handler.getParsedUserAccount();
				} catch (ParserException e) {
					throw new IOException(e.getMessage());
				}
				//
				saveSelfOnPool();
			} else if (respCode == HttpConnection.HTTP_UNAUTHORIZED) {
				verified = false;
			} else {
				HttpResponseCodeInterpreter.perform(conn);
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
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
	 */
	public UserAccount getUserAccount() {
		checkValid();
		checkVerified();
		//
		return account;
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
		HttpConnection conn =
			getHttpConn(TWITTER_URL_IS_FOLLOWING_USER + qryStr, credential);
		//
		try {
			HttpResponseCodeInterpreter.perform(conn);
			//
			InputStream dis = conn.openInputStream();
			byte[] bs = new byte[dis.available()];
			dis.read(bs);
			final String result = new String(bs).trim().toLowerCase();
			//
			return result.equals("true");
		} finally {
			if (conn != null) {
				conn.close();
			}
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
		HttpConnection conn =
			getHttpConn(TWITTER_URL_IS_BLOCKING_USER + id + ".xml", credential);
		//
		try {
			if (conn.getResponseCode() == HttpConnection.HTTP_NOT_FOUND) {
				return false; //not blocked!
			}
			//
			HttpResponseCodeInterpreter.perform(conn);
			//
			return true;
		} finally {
			if (conn != null) {
				conn.close();
			}
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
		HttpConnection conn = getHttpConn(actionUrl, credential);
		//
		try {
			conn.setRequestMethod(HttpConnection.POST);
			//
			OutputStream out = conn.openOutputStream();
			try {
				//TODO: move this checking to UserAccount(String).
				Long.parseLong(id); // is only numbers?
				out.write(("user_id=" + id).getBytes());
			} catch (NumberFormatException e) {
				//user name.
				out.write(("screen_name=" + id).getBytes());
			}
			out.flush();
			out.close();
			//
			HttpResponseCodeInterpreter.perform(conn);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(conn.openInputStream(), handler);
			//
			return handler.getParsedUserAccount();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
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