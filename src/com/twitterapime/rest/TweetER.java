/*
 * TweetER.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.TweetHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * This class defines the methods responsible for managing (e.g. post, retrieve,
 * etc) tweets.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential("username", "password");
 * UserAccountManager uam = UserAccountManager.getInstance(c)
 * TweetER ter = TweetER.getInstance(uam);
 * Tweet t = ter.post(new Tweet("status message"));
 * 
 * TweetER ter = TweetER.getInstance();
 * Tweet t = ter.findByID("12635687984");
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 * @see Tweet
 * @see UserAccountManager
 */
public final class TweetER {
	/**
	 * <p>
	 * TweetER pool used to cache instanced associated to user accounts.
	 * </p>
	 */
	private static Hashtable tweetERPoll;

	/**
	 * <p>
	 * Twitter REST API update status URI.
	 * </p>
	 */
	private static final String TWITTER_URL_UPDATE_STATUS =
		"http://twitter.com/statuses/update.xml";

	/**
	 * <p>
	 * Twitter REST API show status URI.
	 * </p>
	 */
	private static final String TWITTER_URL_SHOW_STATUS =
		"http://twitter.com/statuses/show/";

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static TweetER singleInstance;
	
	/**
	 * <p>
	 * Get an instance of TweetER class and associate it to a given user
	 * account.
	 * </p>
	 * @param uam User account manager.
	 * @return TweetER instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static TweetER getInstance(UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (tweetERPoll == null) {
			tweetERPoll = new Hashtable();
		}
		//
		TweetER ter = (TweetER)tweetERPoll.get(uam);
		if (ter == null) {
			ter = new TweetER(uam);
			tweetERPoll.put(uam, ter);
		}
		//
		return ter;
	}

	/**
	 * <p>
	 * Get a single instance of TweetER class, which is NOT associated to any
	 * user account.
	 * </p>
	 * @return TweetER single instance.
	 */
	public synchronized static TweetER getInstance() {
		if (singleInstance == null) {
			singleInstance = new TweetER();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private TweetER() {
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private TweetER(UserAccountManager uam) {
		userAccountMngr = uam;
	}

	/**
	 * <p>
	 * Find the tweet associated to the given ID. This method does not require
	 * to be logged in to Twitter to use it. In other words, you can use the
	 * TweetER single instance ({@link TweetER#getInstance()}). If the given ID
	 * does not exist, <code>null</code> will be returned.
	 * </p>
	 * @param id Tweet's ID.
	 * @return Tweet.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If the request tweet is protected.
	 * @throws IllegalArgumentException If the given ID is empty/null.
	 */
	public Tweet findByID(String id) throws LimitExceededException,
		IOException {
		if (id == null || (id = id.trim()).length() == 0) {
			throw new IllegalArgumentException("ID must not be empty/null.");
		}
		//
		final String url = TWITTER_URL_SHOW_STATUS + id + ".xml";
		final Credential credential =
			userAccountMngr != null ? userAccountMngr.getCredential() : null;
		//
		HttpConnection conn = UserAccountManager.getHttpConn(url, credential);
		Parser parser = ParserFactory.getDefaultParser();
		TweetHandler handler = new TweetHandler();
		//
		try {
			HttpResponseCodeInterpreter.perform(conn);
			//
			parser.parse(conn.openInputStream(), handler);
			//
			return handler.getParsedTweet();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} catch (InvalidQueryException e) {
			final int repCode = conn.getResponseCode();
			//
			if (repCode == HttpConnection.HTTP_FORBIDDEN) {
				//the refered tweet id is protected.
				throw new SecurityException(e.getMessage());
			} else if (repCode == HttpConnection.HTTP_NOT_FOUND) {
				//tweet id not found.
				return (Tweet)null;
			} else {
				throw e;
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * <p>
	 * Post a given tweet to Twitter. The tweet's content must be up to 140
	 * characters and must be properly logged in ({@link UserAccountManager}).
	 * </p>
	 * @param tweet Tweet to be posted.
	 * @return Tweet post with some additional data.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IllegalArgumentException If the given tweet is null/empty, etc.
	 */
	public Tweet post(Tweet tweet) throws IOException {
		if (tweet == null) {
			throw new IllegalArgumentException("Tweet must not be null.");
		}
		//
		tweet.validateContent();
		//
		if (userAccountMngr == null) {
			throw new SecurityException(
			    "User's credential must be entered to perform this operation.");
		}
		//
		HttpConnection conn =
			UserAccountManager.getHttpConn(
				TWITTER_URL_UPDATE_STATUS, userAccountMngr.getCredential());
		Parser parser = ParserFactory.getDefaultParser();
		TweetHandler handler = new TweetHandler();
		//
		try {
			final String content = tweet.getString(MetadataSet.TWEET_CONTENT);
			//
			conn.setRequestMethod(HttpConnection.POST);
			//
			OutputStream out = conn.openOutputStream();
			out.write(("status=" + content).getBytes());
			out.flush();
			out.close();
			//
			HttpResponseCodeInterpreter.perform(conn);
			//
			parser.parse(conn.openInputStream(), handler);
			handler.loadParsedTweet(tweet);
			//
			return tweet;
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
}