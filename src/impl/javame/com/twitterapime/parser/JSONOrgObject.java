/*
 * JSONOrgObject.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.util.Enumeration;

import org.json.me.JSONException;

import com.twitterapime.parser.JSONArray;
import com.twitterapime.parser.JSONObject;

/**
 * <p>
 * This class defines an implemented on JSONObject based on JSON.org library.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public final class JSONOrgObject implements JSONObject {
	/**
	 * <p>
	 * JSONObject.org object.
	 * </p>
	 */
	private org.json.me.JSONObject jsonOrgObject;
	
	/**
	 * <p>
	 * Create an instance of JSONOrgArray class.
	 * </p>
	 * @param jo JSONObject.org object.
	 */
	public JSONOrgObject(org.json.me.JSONObject jo) {
		jsonOrgObject = jo;
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#get(java.lang.String)
	 */
	public Object get(String key) {
		try {
			return jsonOrgObject.get(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) {
		try {
			return jsonOrgObject.getBoolean(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		try {
			return jsonOrgObject.getInt(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getJSONArray(java.lang.String)
	 */
	public JSONArray getJSONArray(String key) {
		try {
			return new JSONOrgArray(jsonOrgObject.getJSONArray(key));
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getJSONObject(java.lang.String)
	 */
	public JSONObject getJSONObject(String key) {
		try {
			return new JSONOrgObject(jsonOrgObject.getJSONObject(key));
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getLong(java.lang.String)
	 */
	public long getLong(String key) {
		try {
			return jsonOrgObject.getLong(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#getString(java.lang.String)
	 */
	public String getString(String key) {
		try {
			return jsonOrgObject.getString(key);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#has(java.lang.String)
	 */
	public boolean has(String key) {
		return jsonOrgObject.has(key);
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#isNull(java.lang.String)
	 */
	public boolean isNull(String key) {
		return jsonOrgObject.isNull(key);
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#keys()
	 */
	public Enumeration keys() {
		return jsonOrgObject.keys();
	}

	/**
	 * @see com.twitterapime.parser.JSONObject#length()
	 */
	public int length() {
		return jsonOrgObject.length();
	}
}
