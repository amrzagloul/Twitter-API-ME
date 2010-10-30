/*
 * TrendTopicsHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.search.Topic;
import com.twitterapime.util.StringUtil;

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
	 * <p>
	 * Create an instance of TrendTopicsHandler class.
	 * </p> 
	 */
	public TrendTopicsHandler() {
		super("trends");
	}
	
	/**
	 * <p>
	 * Get parsed topics.
	 * </p>
	 * @return Topics.
	 */
	public Topic[] getParsedTopics() {
		if (content != null && content.size() > 0) {
			String key = content.keys().nextElement().toString();
			Object[] tss = (Object[])content.get(key);
			//
			if (key.length() == 10) { //only date.
				key += " 00:00:00";
			} else if (key.length() == 16) { //date and time but secs.
				key += ":00";
			}
			String time = StringUtil.convertTweetDateToLong(key) + "";
			//
			Topic[] topics = new Topic[tss.length];
			//
			for (int i = 0; i < topics.length; i++) {
				Hashtable data = (Hashtable)tss[i];
				String name = (String)data.get("name");
				String query = (String)data.get("query");
				//
				data.clear();
				//
				data.put(MetadataSet.TOPIC_DATE, time);
				if (name != null) {
					data.put(MetadataSet.TOPIC_TEXT, name);
				}
				if (query != null) {
					data.put(MetadataSet.TOPIC_QUERY, query);
				}
				//
				topics[i] = new Topic(data);
			}
			//
			return topics;
		} else {
			return new Topic[0];
		}
	}
}
