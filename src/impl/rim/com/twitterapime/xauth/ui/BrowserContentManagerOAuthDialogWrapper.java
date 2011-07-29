/*
 * BrowserContentManagerOAuthDialogWrapper.java
 * 21/07/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.rim.com.twitterapime.xauth.ui;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.browser.field.BrowserContent;
import net.rim.device.api.browser.field.BrowserContentManager;
import net.rim.device.api.browser.field.Event;
import net.rim.device.api.browser.field.RedirectEvent;
import net.rim.device.api.browser.field.RenderingApplication;
import net.rim.device.api.browser.field.RequestedResource;
import net.rim.device.api.browser.field.UrlRequestedEvent;
import net.rim.device.api.system.Display;

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
	private RenderingApplicationOAuthDialog renderingListenerOAuth;
	
	/**
	 * <p>
	 * Create an instance of BrowserContentManagerOAuthDialogWrapper class.
	 * </p>
	 * @param browserManager BrowserContentManager to display Twitter's login 
	 *                       page.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param callbackUrl Callback Url. (null for "Out-of-band" mode)
	 * @param oauthListener OAuth listener.
	 */
	public BrowserContentManagerOAuthDialogWrapper(
		BrowserContentManager browserManager, String consumerKey, 
		String consumerSecret, String callbackUrl, 
		OAuthDialogListener oauthListener) {
		super(consumerKey, consumerSecret, callbackUrl, oauthListener);
		//
		if (browserManager == null) {
			throw new IllegalArgumentException(
				"BrowserContentManager must not be null.");
		}
		//
		this.browserManager = browserManager;
		renderingListenerOAuth = new RenderingApplicationOAuthDialog();
	}
	
	/**
	 * <p>
	 * Create an instance of BrowserFieldOAuthDialogWrapper class.
	 * </p>
	 * @param browserManager BrowserContentManager to display Twitter's login 
	 *                       page.
	 */
	public BrowserContentManagerOAuthDialogWrapper(
		BrowserContentManager browserManager) {
		this(browserManager, null, null, null, null);
	}

	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		loadUrl(url, null, null);
	}
	
	/**
	 * <p>
	 * Load the given Url in the browser component. 
	 * </p>
	 * @param url Url to load.
	 * @param postData Data to post.
	 * @param event Event.
	 */
	protected void loadUrl(final String url, final byte[] postData, 
		final Event event) {
		new Thread() {
			public void run() {
				try {
					HttpConnection conn = (HttpConnection)Connector.open(url);
					//
					if (postData != null) {
						conn.setRequestMethod(HttpConnection.POST);
						conn.setRequestProperty(
							"Content-Type",
							"application/x-www-form-urlencoded");
		                conn.setRequestProperty(
		                	"Content-Length", String.valueOf(postData.length));
		                //
		                OutputStream out = conn.openOutputStream();
		                out.write(postData);
		                out.close();
					}
					//
					browserManager.setContent(
						conn,	renderingListenerOAuth,	event);
				} catch (IOException e) {
					throw new IllegalArgumentException(e.getMessage());
				}
			}
		}.start();
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
	private class RenderingApplicationOAuthDialog
		implements RenderingApplication {
		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#eventOccurred(net.rim.device.api.browser.field.Event)
		 */
		public Object eventOccurred(Event event) {
			final int euid = event.getUID();
			//
	        if (euid == Event.EVENT_URL_REQUESTED) {
	        	UrlRequestedEvent reqEvent = (UrlRequestedEvent)event;
	        	//
	        	loadUrl(reqEvent.getURL(), reqEvent.getPostData(), event);
	        } else if (euid == Event.EVENT_REDIRECT) {
	        	RedirectEvent redEvent = (RedirectEvent)event;
	        	//
	        	loadUrl(redEvent.getLocation(), null, event);
	        	trackUrl(redEvent.getLocation());
	        }
	        //
			return null;
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getAvailableHeight(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getAvailableHeight(BrowserContent browserField) {
			return Display.getHeight();
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getAvailableWidth(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getAvailableWidth(BrowserContent browserField) {
			return Display.getWidth();
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getHTTPCookie(java.lang.String)
		 */
		public String getHTTPCookie(String url) {
			return null;
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getHistoryPosition(net.rim.device.api.browser.field.BrowserContent)
		 */
		public int getHistoryPosition(BrowserContent browserField) {
			return 0;
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#getResource(net.rim.device.api.browser.field.RequestedResource, net.rim.device.api.browser.field.BrowserContent)
		 */
		public HttpConnection getResource(RequestedResource resource,
			BrowserContent referrer) {
			if (resource == null || resource.getUrl() == null) {
				return null;
	        }
			//
			try {
				return (HttpConnection)Connector.open(resource.getUrl());
			} catch (IOException e) {
				return null;
			}
		}

		/**
		 * @see net.rim.device.api.browser.field.RenderingApplication#invokeRunnable(java.lang.Runnable)
		 */
		public void invokeRunnable(Runnable runnable) {
			(new Thread(runnable)).start();
		}
	}
}
