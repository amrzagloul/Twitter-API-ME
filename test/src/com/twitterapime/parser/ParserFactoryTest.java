/**
 * 
 */
package com.twitterapime.parser;

import impl.android.com.twitterapime.parser.SAXParser;
import impl.javame.com.twitterapime.parser.JSONOrgParser;
import impl.javame.com.twitterapime.parser.KXML2Parser;

import com.twitterapime.platform.PlatformProviderSelector;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class ParserFactoryTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public ParserFactoryTest() {
		super("ParserFactoryTest");
	}

	/**
	 * Test method for {@link com.twitterapime.parser.ParserFactory#getDefaultParser()}.
	 */
	public void testGetDefaultParser() {
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //J2ME
		//
		Parser parser = ParserFactory.getDefaultParser();
		//
		assertNotNull(parser);
		assertEquals(new KXML2Parser().getClass(), parser.getClass());
		//
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[1]); //ANDROID
		//
		parser = ParserFactory.getDefaultParser();
		//
		assertNotNull(parser);
		assertEquals(new SAXParser().getClass(), parser.getClass());
	}

	/**
	 * Test method for {@link com.twitterapime.parser.ParserFactory#getParser(int)}.
	 */
	public void testGetParser() {
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //J2ME
		//
		Parser parser = ParserFactory.getParser(ParserFactory.XML);
		//
		assertNotNull(parser);
		assertEquals(new KXML2Parser().getClass(), parser.getClass());
		//
		parser = ParserFactory.getParser(ParserFactory.JSON);
		//
		assertNotNull(parser);
		assertEquals(new JSONOrgParser().getClass(), parser.getClass());
		//
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[1]); //ANDROID
		parser = ParserFactory.getParser(ParserFactory.XML);
		//
		assertNotNull(parser);
		assertEquals(new SAXParser().getClass(), parser.getClass());
		//
		parser = ParserFactory.getParser(ParserFactory.JSON);
		//
		assertNotNull(parser);
		assertEquals(new JSONOrgParser().getClass(), parser.getClass());
	}
}
