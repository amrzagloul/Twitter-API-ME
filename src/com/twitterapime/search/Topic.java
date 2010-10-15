/*
 * Topic.java
 * 15/10/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;

/**
 * <p>
 * This class defines a topic from trend topics feature.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.5
 */
public final class Topic extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of Topic class.
	 * </p>
	 */
	public Topic() {
	}

	/**
	 * <p>
	 * Create an instance of Topic class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public Topic(Hashtable data) {
		super(data);
	}
}
