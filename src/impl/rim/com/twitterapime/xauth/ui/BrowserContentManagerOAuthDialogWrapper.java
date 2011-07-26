/*
 * BrowserContentManagerOAuthDialogWrapper.java
 * 21/07/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.rim.com.twitterapime.xauth.ui;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.browser.field.BrowserContent;
import net.rim.device.api.browser.field.BrowserContentManager;
import net.rim.device.api.browser.field.Event;
import net.rim.device.api.browser.field.RenderingApplication;
import net.rim.device.api.browser.field.RequestedResource;

import com.twitterapime.xauth.ui.OAuthDialogListener;
import com.twitterapime.xauth.ui.OAuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a BrowserContentManager component, which 
 * will hold the authentication process to Twitter. It is responsible for 
 * informing to Twitter all the parameters about the application that is trying 
 * to get access to a given user account.
 * </p>
 * <p>
 * In addition, this class also tracks the process in order to identify when the 
 * authorization is granted or denied, so it can notify the application. This 
 * notification is done throught a listener object that implements 
 * {@link OAuthDialogListener}. 
 * </p>
 * <p>
 * Before using this class, the developer must register an app on Twitter
 * Developer website.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.8
 */
public final class BrowserContentManagerOAuthDialogWrapper 
	extends OAuthDialogWrapper {
	/**
	 * <p>
	 * BrowserContentManager object.
	 * </p>
	 */
	private BrowserContentManager browserManager;
	
	/**
	 * <p>
	 * RenderingApplicationListenerOAuth object.
	 * </p>
	 */
	private RenderingApplicationListenerOAuth renderingListenerOAuth;
	
	/**
	 * <p>
	 * RenderingApplication object.
	 * </p>
	 */
	private RenderingApplication renderingApplication;
	
	/**
	 * <p>
	 * Create an instance of BrowserContentManagerOAuthDialogWrapper class.
	 * </p>
	 * @param browserManager BrowserContentManager to display Twitter's login 
	 *                       page.
	 * @param renderingApplication Rendering Application.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param callbackUrl Callback Url. (null for "Out-of-band" mode)
	 * @param oauthListener OAuth listener.
	 */
	public BrowserContentManagerOAuthDialogWrapper(
		BrowserContentManager browserManager,
		RenderingApplication renderingApplication, String consumerKey, 
		String consumerSecret, String callbackUrl, 
		OAuthDialogListener oauthListener) {
		super(consumerKey, consumerSecret, callbackUrl, oauthListener);
		//
		if (browserManager == null) {
			throw new IllegalArgumentException(
				"BrowserContentManager must not be null.");
		}
		if (renderingApplication == null) {
			throw new IllegalArgumentException(
				"RenderingApplication must not be null.");
		}
		//
		this.browserManager = browserManager;
		this.renderingApplication = renderingApplication;
		renderingListenerOAuth = new RenderingApplicationListenerOAuth();
	}
	
	/**
	 * <p>
	 * Create an instance of BrowserFieldOAuthDialogWrapper class.
	 * </p>
	 * @param browserManager BrowserContentManager to display Twitter's login 
	 *                       page.
	 * @param renderingApplication Rendering Application.
	 */
	public BrowserContentManagerOAuthDialogWrapper(
		BrowserContentManager browserManager,
		RenderingApplication renderingApplication) {
		this(browserManager, renderingApplication, null, null, null, null);
	}

	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		try {
			browserManager.setContent(
				(HttpConnection)Connector.open(url),
				renderingListenerOAuth,
				null);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadHTML(java.lang.String)
	 */
	protected void loadHTML(String htmlContent) {
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#displayOAuthDeniedPage(java.lang.String)
	 */
	protected void displayOAuthDeniedPage(String message) {
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#displayOAuthErrorPage(java.lang.String, java.lang.String)
	 */
	protected void displayOAuthErrorPage(String error, String message) {
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#displayOAuthSuccessPage()
	 */
	protected void displayOAuthSuccessPage() {
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class RenderingApplicationListenerOAuth
		implements RenderingApplication {
		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#eventOccurred(net.rim.device.api.browser.field.Event)
		 */
		public Object eventOccurred(Event event) {
	        if (event.getUID() == Event.EVENT_BROWSER_CONTENT_CHANGED) {
                if (event.getSource() instanceof BrowserContent) {
                    BrowserContent browserField =
                    	(BrowserContent)event.getSource();
                    //
                    trackUrl(browserField.getURL());
                }
	        }
	        //
			return renderingApplication.eventOccurred(event);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getAvailableHeight(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getAvailableHeight(BrowserContent browserField) {
			return renderingApplication.getAvailableHeight(browserField);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getAvailableWidth(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getAvailableWidth(BrowserContent browserField) {
			return renderingApplication.getAvailableWidth(browserField);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getHTTPCookie(java.lang.String)
		 */
		public String getHTTPCookie(String url) {
			return renderingApplication.getHTTPCookie(url);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getHistoryPosition(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getHistoryPosition(BrowserContent browserField) {
			return renderingApplication.getHistoryPosition(browserField);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getResource(net.rim.device.api.browser.field.RequestedResource, net.rim.device.api.browser.field.BrowserContent)
		 */
		public HttpConnection getResource(RequestedResource resource,
			BrowserContent referrer) {
			return renderingApplication.getResource(resource, referrer);
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#invokeRunnable(java.lang.Runnable)
		 */
		public void invokeRunnable(Runnable runnable) {
			renderingApplication.invokeRunnable(runnable);
		}
	}
}
