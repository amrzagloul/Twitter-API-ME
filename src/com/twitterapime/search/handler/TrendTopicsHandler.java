/*
 * TrendTopicsHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.parser.JSONObject;
import com.twitterapime.parser.ParserException;
import com.twitterapime.search.Topic;

/**
 * <p>
 * Handler class for parsing the JSON trend topics search result.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public final class TrendTopicsHandler extends DefaultJSONHandler {
	/**
	 * @see com.twitterapime.parser.DefaultJSONHandler#handle(com.twitterapime.parser.JSONObject)
	 */
	public void handle(JSONObject jsonObj) throws ParserException {
		super.handle(jsonObj);
	}
	
	/**
	 * <p>
	 * Get parsed topics.
	 * </p>
	 * @return Topics.
	 */
	public Topic[] getParsedTopics() {
		return null;
	}
}
