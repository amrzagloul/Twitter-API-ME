/*
 * XAuthSigner.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;

/**
 * <p>
 * This class implements a xAuth signer.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.3
 */
public final class XAuthSigner extends OAuthSigner {
	/**
	 * <p>
	 * Create an instance of XAuthSigner class.
	 * </p>
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @throws IllegalArgumentException If consumer key/secret is empty or null.
	 */
	public XAuthSigner(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret);
	}
	
	/**
	 * <p>
	 * Sign the given request to obtain the access token.
	 * </p>
	 * @param req Http request.
	 * @param username User name.
	 * @param password Password.
	 */
	public void signForAccessToken(HttpRequest req, String username,
		String password) {
		req.setMethod(HttpConnection.POST);
		req.setBodyParameter(OAuthConstants.MODE, "client_auth");
		req.setBodyParameter(OAuthConstants.USERNAME, username);
		req.setBodyParameter(OAuthConstants.PASSWORD, password);
		//
		OAuthParameters params = new OAuthParameters(consumerKey);
		//
		String str = getSignatureBaseString(req, params);
		//
		str = getSignature(
			str, consumerSecret, OAuthConstants.EMPTY_TOKEN_SECRET);
		params.put(OAuthConstants.SIGNATURE, str);
		//
		str = params.getAuthorizationHeaderValue();
		req.setHeaderField(OAuthConstants.HEADER, str);
	}
}
