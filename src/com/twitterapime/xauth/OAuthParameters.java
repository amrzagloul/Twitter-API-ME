package com.twitterapime.xauth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnector;
import com.twitterapime.util.QSort;

/**
 * @author ernandes
 *
 */
final class OAuthParameters {
	/**
	 * 
	 */
	private Hashtable params;

	/**
	 * @param consumerKey
	 */
	public OAuthParameters(String consumerKey) {
		params = new Hashtable();
		params.put(XAuthConstants.TIMESTAMP, getTimestampInSeconds());
		params.put(XAuthConstants.SIGN_METHOD, "HMAC-SHA1");
		params.put(XAuthConstants.VERSION, "1.0");
		params.put(XAuthConstants.NONCE, getTimestampInSeconds());
		params.put(XAuthConstants.CONSUMER_KEY, consumerKey);
	}

	/**
	 * @return
	 */
	public String getSortedEncodedParamsAsString() {
		StringBuffer buffer = new StringBuffer();
		String[] sKeys = sortedKeys();
		//
		for (int i = 0; i < sKeys.length; i++) {
			buffer.append(HttpConnector.encodeURL(sKeys[i], true));
			buffer.append('=');
			buffer.append(
				HttpConnector.encodeURL((String)params.get(sKeys[i]), true));
			//
			if (i +1 < sKeys.length) {
				buffer.append('&');
			}
		}
		//
		return buffer.toString();
	}

	/**
	 * @return
	 */
	public String getAuthorizationHeaderValue() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("OAuth ");
		//
		String[] sKeys = sortedKeys();
		for (int i = 0; i < sKeys.length; i++) {
			if (sKeys[i].startsWith(XAuthConstants.PARAM_PREFIX)) {
				buffer.append(sKeys[i]);
				buffer.append('=');
				buffer.append('"');
				buffer.append(
					HttpConnector.encodeURL(
						(String)params.get(sKeys[i]), true));
				buffer.append('"');
				//
				if (i +1 < sKeys.length) {
					buffer.append(", ");
				}
			}
		}
		//
		return buffer.toString();
	}

	/**
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		params.put(key, value);
	}

	/**
	 * @return
	 */
	private String[] sortedKeys() {
		int i = 0;
		String[] sKeys = new String[params.size()];
		Enumeration keys = params.keys();
		//
		while (keys.hasMoreElements()) {
			sKeys[i++] = (String)keys.nextElement();
		}
		//
		QSort qsort = new QSort();
		qsort.quicksort(sKeys, 0, sKeys.length -1);
		//
		return sKeys;
	}

	/**
	 * @return
	 */
	private String getTimestampInSeconds() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}