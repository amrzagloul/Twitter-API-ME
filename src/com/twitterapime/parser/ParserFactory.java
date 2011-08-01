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
 * This factory class is responsible for creating new parser objects. 
 * </p>
 * <p>
 * The creation of a parser object is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.4
 * @since 1.0
 */
public final class ParserFactory {
	/**
	 * <p>
	 * Constant for XML parser type.
	 * </p>
	 */
	public static final int XML = 1;
	
	/**
	 * <p>
	 * Constant for JSON parser type.
	 * </p>
	 */
	public static final int JSON = 2;
	
	/**
	 * <p>
	 * Create the default parser instance according to the underlying platform.
	 * </p>
	 * @return Parser instance.
	 */
	public static Parser getDefaultParser() {
		return getParser(XML);
	}
	
	/**
	 * <p>
	 * Create a parser instance of a given type.
	 * </p>
	 * @param type Parser type.
	 * @return Parser instance.
	 * @see ParserFactory#XML
	 * @see ParserFactory#JSON
	 */
	public static Parser getParser(int type) {
		final long PPID = PlatformProviderSelector.getCurrentProvider().getID();
		//
		//#ifdef PP_JAVA_ME
		if (PPID == PlatformProvider.PPID_JAVA_ME) {
			if (type == JSON) {
				return new impl.javame.com.twitterapime.parser.JSONOrgParser();
			} else {
				return new impl.javame.com.twitterapime.parser.KXML2Parser();
			}
		}
		//#else
		//#ifdef PP_RIM
//@		if (PPID == PlatformProvider.PPID_RIM) {
//@			if (type == JSON) {
//@				return new impl.javame.com.twitterapime.parser.JSONOrgParser();
//@			} else {
//@				return new impl.javame.com.twitterapime.parser.KXML2Parser();
//@			}
//@		}
		//#else
//@		//
		//#ifdef PP_ANDROID
//@		if (PPID == PlatformProvider.PPID_ANDROID) {
//@			if (type == JSON) {
//@				return new impl.javame.com.twitterapime.parser.JSONOrgParser();				
//@			} else {
//@				return new impl.android.com.twitterapime.parser.SAXParser();				
//@			}
//@		}
		//#endif
		//#endif
		//#endif
		//
		throw new IllegalArgumentException("Unknown platform ID: " + PPID);
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private ParserFactory() {
	}
}
