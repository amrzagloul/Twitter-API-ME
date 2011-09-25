/*
 * Credential.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.Token;

/**
 * <p>
 * This class defines an entity that represents a user's credential.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.4
 * @since 1.1
 * @see UserAccountManager
 */
public final class Credential extends DefaultEntity {
//#ifdef PP_ANDROID
//@	/**
//@	 * <p>
//@	 * Serial UID.
//@	 * </p>
//@	 */
//@	private static final long serialVersionUID = -3520364801859808022L;
//#endif

	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * <p>
	 * <b>This constructor is not longer supported! It always throws an 
	 * exception.</b>
	 * </p>
	 * @param usernameOrEmail Username or e-mail.
	 * @param password Password.
	 * @throws IllegalArgumentException If any parameter is empty or null.
	 * @deprecated Use {@link Credential#Credential(String, String, String, String)}
	 *             instead.
	 */
	public Credential(String usernameOrEmail, String password) {
		throw new IllegalArgumentException("This constructor is deprecated!");
	}
	
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param usernameOrEmail Username or e-mail.
	 * @param password Password.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @throws IllegalArgumentException If any parameter is empty or null.
	 */
	public Credential(String usernameOrEmail, String password, 
		String consumerKey,	String consumerSecret) {
		if (StringUtil.isEmpty(usernameOrEmail)) {
			throw new IllegalArgumentException(
				"Username or e-mail must not be empty/null");
		}
		if (StringUtil.isEmpty(password)) {
			throw new IllegalArgumentException(
				"Password must not be empty/null");
		}
		if (StringUtil.isEmpty(consumerKey)) {
			throw new IllegalArgumentException(
				"Consumer Key must not be empty/null");
		}
		if (StringUtil.isEmpty(consumerSecret)) {
			throw new IllegalArgumentException(
				"Consumer Secret must not be empty/null");
		}
		//
		Hashtable credtls = new Hashtable(4);
		//
		if (usernameOrEmail.indexOf('@') != -1) { //is e-mail?
			credtls.put(MetadataSet.CREDENTIAL_EMAIL, usernameOrEmail);	
		} else {
			credtls.put(MetadataSet.CREDENTIAL_USERNAME, usernameOrEmail);
		}
		credtls.put(MetadataSet.CREDENTIAL_PASSWORD, password);
		credtls.put(MetadataSet.CREDENTIAL_CONSUMER_KEY, consumerKey);
		credtls.put(MetadataSet.CREDENTIAL_CONSUMER_SECRET, consumerSecret);
		//
		setData(credtls);
	}
	
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param usernameOrEmail Username or e-mail.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param accessToken OAuth access token.
	 * @throws IllegalArgumentException If any parameter is empty/null.
	 * @deprecated Use {@link Credential#Credential(String, String, Token)} 
	 *             instead.
	 */
	public Credential(String usernameOrEmail, String consumerKey,
		String consumerSecret, Token accessToken) {
		this(usernameOrEmail, "ignored", consumerKey, consumerSecret);
		//
		if (accessToken == null) {
			throw new IllegalArgumentException("accessToken must not be null.");
		}
		//
		data.put(MetadataSet.CREDENTIAL_ACCESS_TOKEN, accessToken);
	}
	
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param accessToken OAuth access token.
	 * @throws IllegalArgumentException If any parameter is empty/null.
	 */
	public Credential(String consumerKey, String consumerSecret, 
		Token accessToken) {
		this("ignored", consumerKey, consumerSecret, accessToken);
	}
	
	/**
	 * <p>
	 * Get entered OAuth access token.
	 * <p>
	 * @return Token.
	 */
	Token getAccessToken() {
		return (Token)getObject(MetadataSet.CREDENTIAL_ACCESS_TOKEN);
	}
	
	/**
	 * <p>
	 * Verify whether username were entered.
	 * </p>
	 * @return true username entered.
	 */
	boolean hasUsername() {
		return data.get(MetadataSet.CREDENTIAL_USERNAME) != null;
	}
	
	/**
	 * <p>
	 * Get username or e-mail credential.
	 * </p>
	 * @return Username or e-mail.
	 */
	String getUsernameOrEmail() {
		String username = getString(MetadataSet.CREDENTIAL_USERNAME);
		//
		if (username == null) {
			username = getString(MetadataSet.CREDENTIAL_EMAIL);
		}
		//
		return username;
	}
	
	/**
	 * <p>
	 * Set username.
	 * </p>
	 * @param username Username.
	 */
	void setUsername(String username) {
		data.put(MetadataSet.CREDENTIAL_USERNAME, username);
	}
}
