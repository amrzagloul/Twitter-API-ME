package com.twitterapime.xauth;

/**
 * @author ernandes
 *
 */
interface XAuth {
	public static final String TIMESTAMP = "oauth_timestamp";
	public static final String SIGN_METHOD = "oauth_signature_method";
	public static final String SIGNATURE = "oauth_signature";
	public static final String CONSUMER_KEY = "oauth_consumer_key";
	public static final String VERSION = "oauth_version";
	public static final String NONCE = "oauth_nonce";
	public static final String PARAM_PREFIX = "oauth_";
	public static final String TOKEN = "oauth_token";
	public static final String EMPTY_TOKEN_SECRET = "";
	public static final String HEADER = "Authorization";
	public static final String MODE = "x_auth_mode";
	public static final String USERNAME = "x_auth_username";
	public static final String PASSWORD = "x_auth_password";
}