/*
 * TrendTopicsWoeidHandler.java
 * 03/10/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultJSONHandler;
import com.twitterapime.search.Topic;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the JSON trend topics Woeid search result.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.8
 */
public final class TrendTopicsWoeidHandler extends DefaultJSONHandler {
	/**
	 * <p>
	 * Create an instance of TrendTopicsHandler class.
	 * </p> 
	 */
	public TrendTopicsWoeidHandler() {
		super("root");
	}
	
	/**
	 * <p>
	 * Get parsed topics.
	 * </p>
	 * @return Topics.
	 */
	public Topic[] getParsedTopics() {
		if (content != null && content.size() > 0) {
			String timestamp = content.get("created_at").toString();
			//
			if (timestamp.length() == 10) { //only date.
				timestamp += " 00:00:00";
			} else if (timestamp.length() == 16) { //date and time but secs.
				timestamp += ":00";
			}
			//
			timestamp = StringUtil.convertTweetDateToLong(timestamp) + "";
			//
			Object[] tss = (Object[])content.get("trends");
			Vector topics = new Vector(tss.length);
			//
			for (int i = 0; i < tss.length; i++) {
				Hashtable data = (Hashtable)tss[i];
				//
				String name = (String)data.get("name");
				String query = (String)data.get("query");
				String url = (String)data.get("url");
				String promoted = (String)data.get("promoted_content");
				//
				data.clear();
				//
				data.put(MetadataSet.TOPIC_DATE, timestamp);
				if (!isEmpty(name)) {
					data.put(MetadataSet.TOPIC_TEXT, name);
				}
				if (!isEmpty(query)) {
					data.put(MetadataSet.TOPIC_QUERY, query);
				}
				if (!isEmpty(url)) {
					data.put(MetadataSet.TOPIC_URL, url);
				}
				if (!isEmpty(promoted)) {
					data.put(MetadataSet.TOPIC_PROMOTED, promoted);
				}
				//
				topics.addElement(new Topic(data));
			}
			//
			Topic[] topicsArray = new Topic[topics.size()];
			topics.copyInto(topicsArray);
			//
			return topicsArray;
		} else {
			return new Topic[0];
		}
	}
}
