/*
 * ParserFactory.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class ParserFactory {
	/**
	 * @return
	 */
	public static FeedParser getDefaultFeedParser() {
		final String JAVA_ME_FEED_PARSER_IMPL_CLASS =
			"impl.javame.com.twitterapime.parser.AtomTweetParserImpl";
		//
		PlatformProvider p = PlatformProviderSelector.getCurrentProvider();
		FeedParser parser = null;
		try {
			if (p.getID() == PlatformProvider.PPID_JAVA_ME) {
				parser = (FeedParser)
					Class.forName(JAVA_ME_FEED_PARSER_IMPL_CLASS).newInstance();
			} else {
				throw new IllegalArgumentException(
					"Unknown platform ID: " + p.getID());
			}
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		}
		//
		return parser;
	}
	
	/**
	 * @return
	 */
	public static ErrorMessageParser getDefaultErrorMessageParser(){
		final String JAVA_ME_ERROR_MSG_PARSER_IMPL_CLASS =
			"impl.javame.com.twitterapime.parser.XMLErrorMessageParserImpl";
		//
		PlatformProvider p = PlatformProviderSelector.getCurrentProvider();
		ErrorMessageParser parser = null;
		try {
			if (p.getID() == PlatformProvider.PPID_JAVA_ME) {
				parser = (ErrorMessageParser)
					Class.forName(
						JAVA_ME_ERROR_MSG_PARSER_IMPL_CLASS).newInstance();
			} else {
				throw new IllegalArgumentException(
					"Unknown platform ID: " + p.getID());
			}
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		}
		//
		return parser;
	}
	
	/**
	 * 
	 */
	ParserFactory() {
	}
}