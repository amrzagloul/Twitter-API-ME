/*
 * UserAccount.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines an entity that represents a user's account.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.1
 * @see UserAccountManager
 */
public final class UserAccount extends DefaultEntity {
//#ifdef PP_ANDROID
//@	/**
//@	 * <p>
//@	 * Serial UID.
//@	 * </p>
//@	 */
//@	private static final long serialVersionUID = 6313289563858269077L;
//#endif

	/**
	 * <p>
	 * Max length for name.
	 * </p>
	 */
	public static final int MAX_LEN_NAME = 20;
	
	/**
	 * <p>
	 * Max length for description.
	 * </p>
	 */
	public static final int MAX_LEN_DESCRIPTION = 160;

	/**
	 * <p>
	 * Max length for URL.
	 * </p>
	 */
	public static final int MAX_LEN_URL = 100;

	/**
	 * <p>
	 * Max length for location.
	 * </p>
	 */
	public static final int MAX_LEN_LOCATION = 30;
	
	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 */
	public UserAccount() {
	}

	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public UserAccount(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccount class.<br/>
	 * This constructor is used when the UserAccount object is going to be
	 * used for Follow or Unfollow operations on {@link UserAccountManager}.
	 * </p>
	 * @param userNameOrID Username or ID.
	 * @throws IllegalArgumentException If userNameOrID is empty/null.
	 */
	public UserAccount(String userNameOrID) {
		if (StringUtil.isEmpty(userNameOrID)) {
			throw new IllegalArgumentException(
				"Username/ID must not be empty/null.");
		}
		//
		Hashtable data = new Hashtable();
		try {
			Long.parseLong(userNameOrID);
			data.put(MetadataSet.USERACCOUNT_ID, userNameOrID);
		} catch (Exception e) {
			//it is not supposed to be an ID, since it is not a number.
		}
		data.put(MetadataSet.USERACCOUNT_USER_NAME, userNameOrID);
		setData(data);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccount class.<br/>
	 * Use this constructor when you intend to update the user profile's data 
	 * {@link UserAccountManager#updateProfile(UserAccount)}.
	 * </p>
	 * @param name Name.
	 * @param description Description.
	 * @param url URL.
	 * @param location Location.
	 */
	public UserAccount(String name, String description, String url,
		String location) {
		Hashtable data = new Hashtable(4);
		//
		if (name != null) {
			data.put(MetadataSet.USERACCOUNT_NAME, name);
		}
		if (description != null) {
			data.put(MetadataSet.USERACCOUNT_DESCRIPTION, description);
		}
		if (url != null) {
			data.put(MetadataSet.USERACCOUNT_URL, url);
		}
		if (location != null) {
			data.put(MetadataSet.USERACCOUNT_LOCATION, location);
		}
		//
		setData(data);
	}
	
	/**
	 * <p>
	 * Get the last tweet posted by the user.
	 * </p>
	 * @return Tweet.
	 */
	public Tweet getLastTweet() {
		return (Tweet)getObject(MetadataSet.USERACCOUNT_LAST_TWEET);
	}
	
	/**
	 * <p>
	 * Get username/ID.
	 * </p>
	 * @return Username/ID.
	 */
	String getUserNameOrID() {
		String id = getString(MetadataSet.USERACCOUNT_ID);
		if (StringUtil.isEmpty(id)) {
			id = getString(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		return id;
	}
	
	/**
	 * <p>
	 * Get username/ID and consecutive param.
	 * </p>
	 * @return Username/ID and param.
	 */
	String[] getUserNameOrIDParamValue() {
		String[] paramValue = new String[2];
		//
		String id = getString(MetadataSet.USERACCOUNT_ID);
		if (!StringUtil.isEmpty(id)) {
			try {
				Long.parseLong(id); //test whether it is a number.
				//
				paramValue[0] = "user_id";
				paramValue[1] = id;
				//
				return paramValue;
			} catch (Exception e) {
				//it is not supposed to be an ID, since it is not a number.
			}
		}
		//
		String us = getString(MetadataSet.USERACCOUNT_USER_NAME);
		if (!StringUtil.isEmpty(us)) {
			paramValue[0] = "screen_name";
			paramValue[1] = us;
		}
		//
		return paramValue;
	}
	
	/**
	 * <p>
	 * Verify if username or ID is informed.
	 * </p>
	 * @throws IllegalArgumentException If username and ID are not informed.
	 */
	void validateUserNameOrID() {
		if (StringUtil.isEmpty(getUserNameOrID())) {
			throw new IllegalArgumentException(
				"Username/ID must no be empty/null");
		}
	}
}
