package com.twitterapime.xauth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnector;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.xauth.encoders.Base64Encoder;
import com.twitterapime.xauth.encoders.HMAC;

/**
 * @author ernandes
 *
 */
public final class XAuthSigner {
	/**
	 * 
	 */
	private final String consumerKey;

	/**
	 * 
	 */
	private final String consumerSecret;

	/**
	 * @param baseString
	 * @param consumerSecret
	 * @param tokenSecret
	 * @return
	 */
	private static String getSignature(String baseString, String consumerSecret,
		String tokenSecret) {
		byte[] b = HMAC.getHmac(baseString, consumerSecret + '&' + tokenSecret);
		//
		return Base64Encoder.encode(b);
	}

	/**
	 * @param consumerKey
	 * @param consumerSecret
	 */
	public XAuthSigner(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	/**
	 * @param req
	 * @param username
	 * @param password
	 */
	public void signForAccessToken(HttpRequest req, String username,
		String password) {
		req.setBodyParameter(XAuth.MODE, "client_auth");
		req.setBodyParameter(XAuth.USERNAME, username);
		req.setBodyParameter(XAuth.PASSWORD, password);
		//
		OAuthParameters params = new OAuthParameters(consumerKey);
		//
		String str = getSignatureBaseString(req, params);
		//
		str = getSignature(str, consumerSecret, XAuth.EMPTY_TOKEN_SECRET);
		params.put(XAuth.SIGNATURE, str);
		//
		str = params.getAuthorizationHeaderValue();
		req.setHeaderField(XAuth.HEADER, str);
	}

	/**
	 * @param req
	 * @param access
	 */
	public void sign(HttpRequest req, Token access) {
		OAuthParameters params = new OAuthParameters(consumerKey);
		params.put(XAuth.TOKEN, access.getToken());
		//
		String str = getSignatureBaseString(req, params);
		//
		str = getSignature(str, consumerSecret, access.getSecret());
		params.put(XAuth.SIGNATURE, str);
		//
		str = params.getAuthorizationHeaderValue();
		req.setHeaderField(XAuth.HEADER, str);
	}

	/**
	 * @param req
	 * @param params
	 * @return
	 */
	private String getSignatureBaseString(HttpRequest req,
		OAuthParameters params) {
		String method = HttpConnector.encodeURL(req.getMethod(), true);
		String url = HttpConnector.encodeURL(req.getSanitizedURL(), true);
		//
		addParams(params, req.getQueryStringParams());
		addParams(params, req.getBodyParameters());
		//
		String sortedParams =
			HttpConnector.encodeURL(
				params.getSortedEncodedParamsAsString(), true);
		//
		return method + '&' + url + '&' + sortedParams;
	}
	
	/**
	 * @param params
	 * @param p
	 */
	private void addParams(OAuthParameters params, Hashtable p) {
		String key;
		Enumeration keys = p.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			params.put(key, (String)p.get(key));
		}
	}
}