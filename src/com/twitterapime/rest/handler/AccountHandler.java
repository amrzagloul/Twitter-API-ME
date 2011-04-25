/*
 * AccountHandler.java
 * 09/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Attributes;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the account's XML results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.2
 */
public final class AccountHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Tweet xml handler object.
	 * </p>
	 */
	private TweetHandler tHandler = new TweetHandler();
	
	/**
	 * <p>
	 * User Account XML handler object.
	 * </p>
	 */
	private UserAccountHandler uaHandler = new UserAccountHandler();
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userAccountValues;

	/**
	 * <p>
	 * Hash with last tweet values.
	 * </p>
	 */
	private Hashtable lastTweetValues;
	
	/**
	 * <p>
	 * List of users.
	 * </p>
	 */
	private Vector usersList = new Vector(10);
	
	/**
	 * <p>
	 * List of users hash.
	 * </p>
	 */
	private Vector usersHashList = new Vector(10);
	
	/**
	 * <p>
	 * Next cursor index.
	 * </p>
	 */
	private long nextCursorIndex;

	/**
	 * <p>
	 * Previous cursor index.
	 * </p>
	 */
	private long prevCursorIndex;

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		super.startElement(namespaceURI, localName, qName, attrs);
		//
		if (localName.toLowerCase().equals("user")) {
			userAccountValues = new Hashtable(25);
			lastTweetValues = new Hashtable(5);
			//
			//
			usersHashList.addElement(userAccountValues);
			usersList.addElement(new UserAccount(userAccountValues));
		}
	}

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.indexOf("/user/status/") != -1) {
			tHandler.populate(lastTweetValues, xmlPath, text);
		} else if (xmlPath.endsWith("/next_cursor")) {
			nextCursorIndex = Long.parseLong(text);
		} else if (xmlPath.endsWith("/previous_cursor")) {
			prevCursorIndex = Long.parseLong(text);
		} else {
			uaHandler.populate(userAccountValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		super.endElement(namespaceURI, localName, qName);
		if (localName.toLowerCase().equals("user")
				&& lastTweetValues.size() > 0) {
			userAccountValues.put(
				MetadataSet.USERACCOUNT_LAST_TWEET, new Tweet(lastTweetValues));
		}
	}
	
	/**
	 * <p>
	 * Return the parsed user accounts.
	 * </p>
	 * @return User accounts.
	 */
	public UserAccount[] getParsedUserAccounts() {
		UserAccount[] users = new UserAccount[usersList.size()];
		usersList.copyInto(users);
		//
		return users;
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given UserAccount.
	 * </p>
	 * @param user UserAccount to be loaded.
	 * @param index UserAccount index.
	 */
	public void loadParsedUserAccount(UserAccount user, int index) {
		user.setData((Hashtable)usersHashList.elementAt(index));
	}
	
	/**
	 * <p>
	 * Return the next cursor index.
	 * </p>
	 * @return Index.
	 */
	public long getNextCursorIndex() {
		return nextCursorIndex;
	}

	/**
	 * <p>
	 * Return the previous cursor index.
	 * </p>
	 * @return Index.
	 */
	public long getPreviousCursorIndex() {
		return prevCursorIndex;
	}
}
