/*
 * TrendTopicsHandler.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search.handler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

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
 * @version 1.1
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
			Enumeration keys = content.keys();
			Vector topics = new Vector(50);
			//
			while (keys.hasMoreElements()) {
				String key = keys.nextElement().toString();
				System.out.println(key);
				//
				Object[] tss = (Object[])content.get(key);
				//
				if (key.length() == 10) { //only date.
					key += " 00:00:00";
				} else if (key.length() == 16) { //date and time but secs.
					key += ":00";
				}
				String time = StringUtil.convertTweetDateToLong(key) + "";
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
					data.put(MetadataSet.TOPIC_DATE, time);
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
