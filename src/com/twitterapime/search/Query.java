/*
 * Query.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class Query {
	/**
	 * 
	 */
	String query;

	/**
	 * 
	 */
	Query(String query) {
		try {
			this.query = new String(query.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InvalidQueryException("Invalid UTF-8 string.");
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Query)) {
			return false;
		} else {
			return query.equals(((Query) o).query);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return query;
	}
}