/*
 * MIDletOAuthDialogWrapper.java
 * 24/07/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.xauth.ui;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.MIDlet;

import com.twitterapime.xauth.ui.OAuthDialogListener;
import com.twitterapime.xauth.ui.OAuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a MIDlet object, which will request the 
 * device's native browser to hold the authentication process to Twitter. It is 
 * responsible for informing to Twitter all the parameters about the application 
 * that is trying to get access to a given user account.
 * </p>
 * <p>
 * This class only supports "out-of-band" mode.
 * </p>
 * <p>
 * In addition, this class also tracks the process in order to identify when the 
 * authorization is granted or denied, so it can notify the application. This 
 * notification is done throught a listener object that implements 
 * {@link OAuthDialogListener}. It is important to point out, that this class
 * only notifies any listener about error: grant and deny events are not 
 * notified.
 * </p>
 * <p>
 * Before using this class, the developer must register an app on Twitter
 * Developer website.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.8
 */
public final class MIDletOAuthDialogWrapper extends OAuthDialogWrapper {
	/**
	 * <p>
	 * MIDlet instance.
	 * </p>
	 */
	private MIDlet midlet;
	
	/**
	 * <p>
	 * Create an instance of MIDletOAuthDialogWrapper class.
	 * </p>
	 * @param midlet MIDlet to display Twitter's login page on native browser.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param oauthListener OAuth listener.
	 * @throws IllegalArgumentException If midlet is null.
	 */
	public MIDletOAuthDialogWrapper(MIDlet midlet, String consumerKey, 
		String consumerSecret, OAuthDialogListener oauthListener) {
		super(consumerKey, consumerSecret, null, oauthListener);
		//
		if (midlet == null) {
			throw new IllegalArgumentException("MIDlet must not be null.");
		}
		//
		setEnableCustomResultPages(false);
		this.midlet = midlet;
	}
	
	/**
	 * <p>
	 * Create an instance of MIDletOAuthDialogWrapper class.
	 * </p>
	 * @param midlet MIDlet to display Twitter's login page on native browser.
	 */
	public MIDletOAuthDialogWrapper(MIDlet midlet) {
		this(midlet, null, null, null);
	}

	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		try {
			boolean exit = midlet.platformRequest(url);
			//
			if (exit) { //must exit MIDlet?
				midlet.notifyDestroyed();
			}
		} catch (ConnectionNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#setCallbackUrl(java.lang.String)
	 */
	public void setCallbackUrl(String callbackUrl) {
		super.setCallbackUrl(null);
	}

	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadHTML(java.lang.String)
	 */
	protected void loadHTML(String htmlContent) {
	}	
}
