/*
 * QueryComposer.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class QueryComposer {
	/**
	 * 
	 */
	static final String OP_FILTER = "filter:";

	/**
	 * 
	 */
	static final String OP_FROM = "from:";

	/**
	 * 
	 */
	static final String OP_NEAR = "near:";

	/**
	 * 
	 */
	static final String OP_WITHIN = "within:";

	/**
	 * 
	 */
	static final String OP_POSITIVE_ATTITUDE = ":)";

	/**
	 * 
	 */
	static final String OP_NEGATIVE_ATTITUDE = ":(";

	/**
	 * 
	 */
	static final String OP_NOT = "-";

	/**
	 * 
	 */
	static final String OP_OR = "OR";

	/**
	 * 
	 */
	static final String OP_REFERENCE = "@";

	/**
	 * 
	 */
	static final String OP_SINCE = "since:";

	/**
	 * 
	 */
	static final String OP_SOURCE = "source:";

	/**
	 * 
	 */
	static final String OP_TO = "to:";

	/**
	 * 
	 */
	static final String OP_UNTIL = "until:";

	/**
	 * 
	 */
	static final String OP_LANG = "lang=";

	/**
	 * 
	 */
	static final String OP_LOCALE = "locale=";

	/**
	 * 
	 */
	static final String OP_RPP = "rpp=";

	/**
	 * 
	 */
	static final String OP_PAGE = "page=";

	/**
	 * 
	 */
	static final String OP_SINCE_ID = "since_id=";

	/**
	 * 
	 */
	static final String OP_GEOCODE = "geocode=";

	/**
	 * 
	 */
	static final String OP_SHOW_USER = "show_user=";

	/**
	 * 
	 */
	QueryComposer() {
	}

	/**
	 * 
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Query append(Query q1, Query q2) {
		return new Query(q1 + "&" + q2);
	}

	/**
	 * 
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static Query and(Query q1, Query q2) {
		return new Query(q1 + " " + q2);
	}

	/**
	 * 
	 * @param value
	 * @param exactMatch
	 * @return
	 */
	public static Query contains(String value, boolean exactMatch) {
		final String QUOTE = exactMatch ? "\"" : "";
		//
		return new Query(QUOTE + value + QUOTE);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Query filter(String value) {
		return new Query(OP_FILTER + value);
	}

	/**
	 * 
	 * @param person
	 * @return
	 */
	public static Query from(String person) {
		return new Query(OP_FROM + person);
	}

	/**
	 * 
	 * @param place
	 * @param within
	 * @return
	 */
	public static Query near(String place, String within) {
		String q = OP_NEAR + place;
		if (within != null && within.length() > 0) {
			q += ' ' + OP_WITHIN + within;
		}
		//
		return new Query(q);
	}

	/**
	 * @return
	 */
	public static Query negativeAttitude() {
		return new Query(OP_NEGATIVE_ATTITUDE);
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	public static Query not(Query q) {
		return new Query(OP_NOT + q);
	}

	/**
	 * 
	 * @param q1
	 * @param q2
	 */
	public static Query or(Query q1, Query q2) {
		return new Query(q1 + " " + OP_OR + ' ' + q2);
	}

	/**
	 * @return
	 */
	public static Query positiveAttitude() {
		return new Query(OP_POSITIVE_ATTITUDE);
	}

	/**
	 * 
	 * @param person
	 * @return
	 */
	public static Query references(String person) {
		return new Query(OP_REFERENCE + person);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Query since(Date date) {
		return new Query(OP_SINCE + formatDate(date));
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public static Query source(String appName) {
		return new Query(OP_SOURCE + appName);
	}

	/**
	 * 
	 * @param person
	 * @return
	 */
	public static Query to(String person) {
		return new Query(OP_TO + person);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Query until(Date date) {
		return new Query(OP_UNTIL + formatDate(date));
	}
	
	/**
	 * @param lang
	 * @return
	 */
	public static Query lang(String lang) {
		return new Query(OP_LANG + lang);
	}

	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static Query locale(String locale) {
		return new Query(OP_LOCALE + locale);
	}

	/**
	 * 
	 * @param count
	 * @return
	 */
	public static Query resultCount(int count) {
		return new Query(OP_RPP + count);
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static Query page(int number) {
		return new Query(OP_PAGE + number);
	}

	/**
	 * @param resultCount
	 * @param pageNumber
	 * @return
	 */
	public static Query paginate(int resultCount, int pageNumber) {
		return QueryComposer.append(QueryComposer.resultCount(resultCount),
				QueryComposer.page(pageNumber));
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Query sinceID(String id) {
		return new Query(OP_SINCE_ID + id);
	}

	/**
	 * @param lat
	 * @param lon
	 * @param radius
	 * @return
	 */
	public static Query geocode(String lat, String lon, String radius) {
		return new Query(OP_GEOCODE + lat + ',' + lon + ',' + radius);
	}

	/**
	 * @param show
	 * @return
	 */
	public static Query showUser(boolean show) {
		return new Query(OP_SHOW_USER + show);
	}

	/**
	 * @param date
	 * @return
	 */
	private static String formatDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		//
		return c.get(Calendar.YEAR) + "-" +	(c.get(Calendar.MONTH) + 1) + "-" +
			c.get(Calendar.DAY_OF_MONTH);
	}
}