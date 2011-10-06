/*
 * Token.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class implements an OAuth token.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.3
 */
public final class Token {
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private String token;
	
	/**
	 * <p>
	 * Secret token.
	 * </p>
	 */
	private String secret;
	
	/**
	 * <p>
	 * User Id.
	 * </p>
	 */
	private String userId;

	/**
	 * <p>
	 * Username.
	 * </p>
	 */
	private String username;

	/**
	 * <p>
	 * Parse an access token string and creates a Token object.
	 * </p>
	 * @param tokenStr Token string.
	 */
	public static Token parse(String tokenStr) {
		String access = StringUtil.getUrlParamValue(tokenStr, "oauth_token");
		String secret =
			StringUtil.getUrlParamValue(tokenStr, "oauth_token_secret");
		String userId = StringUtil.getUrlParamValue(tokenStr, "user_id");
		String username = StringUtil.getUrlParamValue(tokenStr, "screen_name");
		//
 		return new Token(access, secret, userId, username);
	}
	
	/**
	 * <p>
	 * Create an instance of Token class.
	 * </p>
	 * @param token Access token.
	 * @param secret Secret token.
	 * @throws IllegalArgumentException If token/secret is empty or null.
	 */
	public Token(String token, String secret) {
		this(token, secret, null, null);
	}

	/**
	 * <p>
	 * Create an instance of Token class.
	 * </p>
	 * @param token Access token.
	 * @param secret Secret token.
	 * @param userId User Id.
	 * @param username Username.
	 * @throws IllegalArgumentException If token/secret is empty or null.
	 */
	public Token(String token, String secret, String userId, String username) {
		if (StringUtil.isEmpty(token)) {
			throw new IllegalArgumentException("Token must not be empty/null");
		}
		if (StringUtil.isEmpty(secret)) {
			throw new IllegalArgumentException("Secret must not be empty/null");
		}
		//
		this.token = token;
		this.secret = secret;
		this.userId = userId;
		this.username = username;
	}

	/**
	 * <p>
	 * Get the access token.
	 * </p>
	 * @return Token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * <p>
	 * Get the secret token.
	 * </p>
	 * @return Token.
	 */
	public String getSecret() {
		return secret;
	}
	
	/**
	 * <p>
	 * Get the user Id.
	 * </p>
	 * @return Id.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * <p>
	 * Get the username.
	 * </p>
	 * @return Username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "oauth_token=" + token + "&oauth_token_secret=" + secret + 
			"&user_id=" + userId + "&screen_name=" + username;
	}
}