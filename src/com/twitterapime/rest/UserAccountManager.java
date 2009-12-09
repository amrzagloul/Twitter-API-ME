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
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.rest.handler.UserAccountHandler;
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
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
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
	 * Get an instance of UserAccountManager class and associate it to a given
	 * user credential.
	 * </p>
	 * @param c Credentials.
	 * @return AccountManager object.
	 */
	public synchronized static UserAccountManager getInstance(Credential c) {
		if (c == null) {
			throw new IllegalArgumentException("Credential cannot be null.");
		}
		if (userAccountMngrPoll == null) {
			userAccountMngrPoll = new Hashtable();
		}
		//
		UserAccountManager uam = (UserAccountManager)userAccountMngrPoll.get(c);
		if (uam == null) {
			uam = new UserAccountManager(c);
			userAccountMngrPoll.put(c, uam);
		}
		//
		return uam;
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
			throw new IllegalArgumentException("URL cannot be empty/null.");
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
	 * Get the rate status limit info. This info describes the access limits to
	 * Twitter REST API, e.g., {@link MetadataSet#RATELIMITSTATUS_HOURLY_LIMIT},
	 * {@link MetadataSet#RATELIMITSTATUS_REMAINING_HITS} and
	 * {@link MetadataSet#RATELIMITSTATUS_RESET_TIME}. Stay aware of these
	 * limits, since it can impact the usage of some methods of this API.
	 * </p>
	 * @return Rate status limit info.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 */
	public RateLimitStatus getRateLimitStatus() throws IOException {
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
		} catch (LimitExceededException e) {
			//Twitter API specs states this operation is not API rate limited.
			//That's why this exception is suppressed.
			throw new IllegalStateException(
				"Unexpected LimitExceededException: " + e.getMessage());
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
		return verified;
	}
	
	/**
	 * <p>
	 * Verify whether the given user's credential are valid. This method
	 * authenticates the API to Twitter REST API.
	 * </p>
	 * @return Valid credentials (true).
	 * @throws IOException If an I/O error occurs.
	 */
	public boolean verifyCredential() throws IOException {
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
					UserAccountHandler handler = new UserAccountHandler();
					parser.parse(conn.openInputStream(), handler);
					//
					account = handler.getParsedUserAccount();
				} catch (ParserException e) {
					throw new IOException(e.getMessage());
				}
			} else if (respCode == HttpConnection.HTTP_UNAUTHORIZED) {
				verified = false;
			} else {
				try {
					HttpResponseCodeInterpreter.perform(conn);
				} catch (LimitExceededException e) {
					//Twitter API specs states this operation is not API rate
					//limited. That's why this exception is suppressed.
				}
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
	 * Get the user account.
	 * </p>
	 * @return User account object.
	 * @throws SecurityException If it is not properly logged in.
	 */
	public UserAccount getUserAccount() {
		checkVerified();
		//
		return account;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return credential.equals(o);
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
	 * Check whether it is verified.
	 * </p>
	 * @throws SecurityException If it is not properly verified.
	 */
	synchronized void checkVerified() {
		if (!verified) {
			throw new SecurityException(
				"User's credentials have not been verified yet.");
		}
	}
}