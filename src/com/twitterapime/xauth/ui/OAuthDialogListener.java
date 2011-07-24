/*
 * OAuthDialogListener.java
 * 21/07/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth.ui;

import com.twitterapime.xauth.Token;

/**
 * <p>
 * This interface defines all the events that can be triggered during the
 * authentication process.
 * </p>
 * <p>
 * Implement this interface in order to retrieve the access token, which will be
 * used to sign the requests to Twitter.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.8
 * @see OAuthDialogWrapper
 */
public interface OAuthDialogListener {
	/**
	 * <p>
	 * Called when the user has authorized the application to access his
	 * Twitter account.
	 * </p>
	 * @param accessToken Access token.
	 */
	public void onAuthorize(Token accessToken);
	
	/**
	 * <p>
	 * Called when the user has denied access to his Twitter account by the
	 * application.
	 * </p>
	 * @param message Message.
	 */
	public void onAccessDenied(String message);
	
	/**
	 * <p>
	 * Called when an unknown error occurs during authentication process.
	 * </p>
	 * @param error Error.
	 * @param message Message.
	 */
	public void onFail(String error, String message);
}
