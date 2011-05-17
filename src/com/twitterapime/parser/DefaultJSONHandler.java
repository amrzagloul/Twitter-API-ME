/*
 * DefaultJSONHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines a default JSON document handler.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.5
 */
public class DefaultJSONHandler implements JSONHandler {
	/**
	 * <p>
	 * Hold the parsed values.
	 * </p>
	 */
	protected Hashtable content;
	
	/**
	 * <p>
	 * Element's key where the parsers will start reading.
	 * </p>
	 */
	protected String startKey;
	
	/**
	 * <p>
	 * Create an instance of DefaultJSONHandler class.
	 * </p> 
	 * @param startKey Start key reading.
	 */
	public DefaultJSONHandler(String startKey) {
		this.startKey = startKey;
	}
	
	/**
	 * @see com.twitterapime.parser.JSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		content = readJSON(jsonObj.getJSONObject(startKey), new Hashtable());
	}

	/**
	 * <p>
	 * Get the parsed content from JSON data.
	 * </p>
	 * @return Parsed content.
	 */
	public Hashtable getParsedContent() {
		return content;
	}
	
	/**
	 * @see com.twitterapime.parser.JSONHandler#isMember(com.twitterapime.parser.JSONObject, java.lang.String)
	 */
	public boolean isMember(JSONObject jsonObj, String key)
		throws ParserException {
		return jsonObj.getString(key).startsWith("{");
	}
	
	/**
	 * @see com.twitterapime.parser.JSONHandler#isArray(com.twitterapime.parser.JSONObject, java.lang.String)
	 */
	public boolean isArray(JSONObject jsonObj, String key)
		throws ParserException {
		return jsonObj.getString(key).startsWith("[");
	}

	/**
	 * <p>
	 * Read recursively the JSON object in order to parse the content.
	 * </p>
	 * @param jsonObj JSON object.
	 * @param data Holds the parsed data.
	 * @return Parsed data.
	 * @throws ParserException If there is an error in the document format.
	 */
	protected Hashtable readJSON(JSONObject jsonObj, Hashtable data)
		throws ParserException {
		Enumeration keys = jsonObj.keys();
		//
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			//
			if (isMember(jsonObj, key)) {
				data.put(
					key, readJSON(jsonObj.getJSONObject(key), new Hashtable()));
			} else if (isArray(jsonObj, key)) {
				JSONArray array = jsonObj.getJSONArray(key);
				Hashtable[] arrayObj = new Hashtable[array.length()];
				//
				for (int i = 0; i < arrayObj.length; i++) {
					arrayObj[i] =
						readJSON(array.getJSONObject(i), new Hashtable());
				}
				//
				data.put(key, arrayObj);
			} else {
				data.put(key, jsonObj.getString(key));
			}
		}
		//
		return data;
	}
	
	/**
	 * <p>
	 * Replace the key of a given property.
	 * </p>
	 * @param hashtable Hastable.
	 * @param searchKey Key to be replaced.
	 * @param replacementKey Replacement key.
	 */
	protected void replaceProperty(Hashtable hashtable,
		String searchKey, String replacementKey) {
		Object value = hashtable.get(searchKey);
		//
		if (value != null) {
			hashtable.remove(searchKey);
			//
			if (value instanceof String) {
				String str = value.toString();
				//
				if (StringUtil.isEmpty(str) || "null".equals(str)) {
					return;
				}
			}
			//
			hashtable.put(replacementKey, value);
		}
	}
}
