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
 * This is factory class for creating new parser objects. 
 * </p>
 * <p>
 * The creation of a parser object is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class ParserFactory {
	/**
	 * <p>
	 * Get an instance of a parser responsible for parsing a web feed.
	 * </p>
	 * @return The web feed parser object.
	 */
	public static FeedParser getDefaultFeedParser() {
		try {
			return (FeedParser)getParser(
				Class.forName("com.twitterapime.parser.FeedParser"));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Get an instance of a parser responsible for parsing the error messages
	 * from Twitter API.
	 * </p>
	 * @return The error message parser object.
	 */
	public static ErrorMessageParser getDefaultErrorMessageParser() {
		try {
			return (ErrorMessageParser)getParser(
				Class.forName("com.twitterapime.parser.ErrorMessageParser"));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Get the proper parser object for the parser class passed as argument
	 * according to the current plataform provider.
	 * </p>
	 * @param parserClass Parser class object.
	 * @return Proper parser object.
	 */
	private static Parser getParser(Class parserClass) {
		final String JAVA_ME_FEED_PARSER_IMPL_CLASS =
			"impl.javame.com.twitterapime.parser.AtomTweetParserImpl";
		final String JAVA_ME_ERROR_MSG_PARSER_IMPL_CLASS =
			"impl.javame.com.twitterapime.parser.XMLErrorMessageParserImpl";
		//
		PlatformProvider p = PlatformProviderSelector.getCurrentProvider();
		Parser parser = null;
		//
		try {
			final Class FEED_PARSER_CLASS =
				Class.forName("com.twitterapime.parser.FeedParser");
			final Class ERROR_MSG_PARSER_CLASS =
				Class.forName("com.twitterapime.parser.ErrorMessageParser");
			//
			//if JAVA ME PLATFORM
			if (p.getID() == PlatformProvider.PPID_JAVA_ME) {
				if (parserClass.equals(FEED_PARSER_CLASS)) {
					parser = (Parser)Class.forName(
						JAVA_ME_FEED_PARSER_IMPL_CLASS).newInstance();
				} else if (parserClass.equals(ERROR_MSG_PARSER_CLASS)) {
					parser = (Parser)Class.forName(
						JAVA_ME_ERROR_MSG_PARSER_IMPL_CLASS).newInstance();
				} else {
					throw new IllegalArgumentException(
						"Unknown parser class: " + parserClass.getName());
				}
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
	 * <p>
	 * Package-protected constructor to avoid object instantiation.
	 * </p>
	 */
	ParserFactory() {
	}
}