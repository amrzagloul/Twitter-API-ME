/*
 * HttpRequest.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.XAuthSigner;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class HttpRequest {
	/**
	 * 
	 */
	private String url;
	
	/**
	 * 
	 */
	private String method;
	
	/**
	 * 
	 */
	private Hashtable bodyParams;
	
	/**
	 * 
	 */
	private Hashtable headers;
	
	/**
	 * 
	 */
	private HttpConnection conn;
	
	/**
	 * 
	 */
	private XAuthSigner signer;
	
	/**
	 * 
	 */
	private Token token;

	/**
	 * @param url
	 * @param method
	 */
	public HttpRequest(String url) {
		this.url = url;
		method = HttpConnection.GET;
		bodyParams = new Hashtable();
		headers = new Hashtable();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public HttpResponse send() throws IOException {
		close();
		//
		conn = HttpConnector.open(url);
		conn.setRequestMethod(method);
		//
		if (signer != null && token != null) {
			signer.sign(this, token); //sign request.
		}
		//
		setHeaderFields(conn);
		if (HttpConnection.POST.equals(method)) {
			setBodyParameters(conn);
		}
		//
		return new HttpResponse(conn);
	}
	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setHeaderField(String key, String value) {
		this.headers.put(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setBodyParameter(String key, String value) {
		this.bodyParams.put(key, value);
	}

	/**
	 * @return
	 */
	public Hashtable getHeaderFields() {
		return headers;
	}
	
	/**
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return
	 */
	public Hashtable getBodyParameters() {
		return bodyParams;
	}
	
	/**
	 * @return
	 */
	public Hashtable getQueryStringParams() {
		Hashtable params = new Hashtable();
		String query = getQueryString();
		//
		if (query != null) {
			String[] ps = StringUtil.split(query, '&');
			String[] pv;
			//
			for (int i = 0; i < ps.length; i++) {
				pv = StringUtil.split(ps[i], '=');
				params.put(pv[0], pv[1]);
			}
		}
		//
		return params;
	}

	/**
	 * @return
	 */
	public String getURL() {
		return url;
	}

	/**
	 * @return
	 */
	public String getMethod() {
		return method; 
	}

	/**
	 * @return
	 */
	public String getSanitizedURL() {
		final int i = url.indexOf('?');
		//
		return i != -1 ? url.substring(0, i) : url;
	}

	/**
	 * @return
	 */
	public String getQueryString() {
		final int i = url.indexOf('?');
		//
		return i != -1 ? url.substring(i +1, url.length()) : null;
	}
	
	/**
	 * @param signer
	 */
	public void setSigner(XAuthSigner signer, Token token) {
		this.signer = signer;
		this.token = token;
	}

	/**
	 * @param conn
	 * @throws IOException
	 */
	private void setHeaderFields(HttpConnection conn) throws IOException {
		String key;
		Enumeration keys = headers.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			conn.setRequestProperty(key, (String)headers.get(key));
		}
	}

	/**
	 * @param conn
	 * @throws IOException
	 */
	private void setBodyParameters(HttpConnection conn) throws IOException {
		byte[] content = queryString(bodyParams).getBytes();
		//
		conn.setRequestProperty(
			"Content-Length", String.valueOf(content.length));
		conn.setRequestProperty(
			"Content-Type", "application/x-www-form-urlencoded");
		//
		OutputStream out = conn.openOutputStream();
		out.write(content);
		out.flush();
		out.close();
	}

	/**
	 * @param p
	 * @return
	 */
	private String queryString(Hashtable p) {
		String key;
		StringBuffer queryStr = new StringBuffer();
		Enumeration keys = p.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			//
			queryStr.append(key);
			queryStr.append('=');
			queryStr.append((String)p.get(key));
			
			if (keys.hasMoreElements()) {
				queryStr.append('&');
			}
		}
		//
		return queryStr.toString();
	}
}