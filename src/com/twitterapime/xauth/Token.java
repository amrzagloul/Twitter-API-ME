package com.twitterapime.xauth;

import com.twitterapime.util.StringUtil;

/**
 * @author ernandes
 *
 */
public final class Token {
	/**
	 * 
	 */
	private String token;
	
	/**
	 * 
	 */
	private String secret;

	/**
	 * @param accessTokenString
	 */
	public static Token parse(String accessTokenString) {
		String[] tokens = StringUtil.split(accessTokenString, '&');
		String access = StringUtil.split(tokens[0], '=')[1];
		String secret = StringUtil.split(tokens[1], '=')[1];
		//
 		return new Token(access, secret);
	}
	
	/**
	 * @param token
	 * @param secret
	 */
	public Token(String token, String secret) {
		this.token = token;
		this.secret = secret;
	}

	/**
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "oauth_token: " + token + " oauth_token_secret: " + secret;
	}
}