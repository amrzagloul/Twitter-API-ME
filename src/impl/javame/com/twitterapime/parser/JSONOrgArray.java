/*
 * JSONOrgArray.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import org.json.me.JSONException;

import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;

/**
 * <p>
 * This class defines an implemented on JSONArray based on JSON.org library.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public final class JSONOrgArray implements JSONArray {
	/**
	 * <p>
	 * JSONArray.org object.
	 * </p>
	 */
	private org.json.me.JSONArray jsonOrgArray;

	/**
	 * <p>
	 * Create an instance of JSONOrgArray class.
	 * </p>
	 * @param ja JSONArray.org object.
	 */
	public JSONOrgArray(org.json.me.JSONArray ja) {
		jsonOrgArray = ja;
	}
	
	/**
	 * @see com.twitterapime.parser.JSONArray#get(int)
	 */
	public Object get(int index) {
		try {
			return jsonOrgArray.get(index);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#getBoolean(int)
	 */
	public boolean getBoolean(int index) {
		try {
			return jsonOrgArray.getBoolean(index);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#getJSONArray(int)
	 */
	public JSONArray getJSONArray(int index) {
		try {
			return new JSONOrgArray(jsonOrgArray.getJSONArray(index));
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#getJSONObject(int)
	 */
	public JSONObject getJSONObject(int index) {
		try {
			return new JSONOrgObject(jsonOrgArray.getJSONObject(index));
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#getString(int)
	 */
	public String getString(int index) {
		try {
			return jsonOrgArray.getString(index);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#isNull(int)
	 */
	public boolean isNull(int index) {
		return jsonOrgArray.isNull(index);
	}

	/**
	 * @see com.twitterapime.parser.JSONArray#length()
	 */
	public int length() {
		return jsonOrgArray.length();
	}
}
