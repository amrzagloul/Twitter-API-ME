/*
 * WebViewOAuthDialogWrapper.java
 * 21/07/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.android.com.twitterapime.xauth.ui;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.twitterapime.xauth.ui.OAuthDialogListener;
import com.twitterapime.xauth.ui.OAuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a WebView component, which will hold the 
 * authentication process to Twitter. It is responsible for informing to
 * Twitter all the parameters about the application that is trying to get 
 * access to a given user account.
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
public final class WebViewOAuthDialogWrapper extends OAuthDialogWrapper {
	/**
	 * <p>
	 * WebView object.
	 * </p>
	 */
	private WebView webView;
	
	/**
	 * <p>
	 * Create an instance of WebViewOAuthDialogWrapper class.
	 * </p>
	 * @param webView WebView to display Twitter's login page.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param callbackUrl Callback Url. (null for "Out-of-band" mode)
	 * @param oauthListener OAuth listener.
	 */
	public WebViewOAuthDialogWrapper(WebView webView, String consumerKey,
		String consumerSecret, String callbackUrl, 
		OAuthDialogListener oauthListener) {
		super(consumerKey, consumerSecret, callbackUrl, oauthListener);
		//
		if (webView == null) {
			throw new IllegalArgumentException("WebView must not be null.");
		}
		//
		this.webView = webView;
		this.webView.setWebViewClient(new WebViewClientOAuth());
	}
	
	/**
	 * <p>
	 * Create an instance of WebViewOAuthDialogWrapper class.
	 * </p>
	 * @param webView WebView to display Twitter's login page.
	 */
	public WebViewOAuthDialogWrapper(WebView webView) {
		this(webView, null, null, null, null);
	}

	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		webView.loadUrl(url);
	}
	
	/**
	 * @see com.twitterapime.xauth.ui.OAuthDialogWrapper#loadHTML(java.lang.String)
	 */
	protected void loadHTML(String htmlContent) {
		webView.loadData(htmlContent, "text/html", "utf-8");
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class WebViewClientOAuth extends WebViewClient {
		/**
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			trackUrl(url);
		}
	}
}
