/**
 * 
 */
package com.twitterapime.parser;

import impl.javame.com.twitterapime.parser.JSONOrgParser;

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
		//#ifdef PP_JAVA_ME
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //J2ME
		//
		Parser parser = ParserFactory.getDefaultParser();
		//
		assertNotNull(parser);
		assertEquals(new impl.javame.com.twitterapime.parser.KXML2Parser().getClass(), parser.getClass());
		//#endif
		//
		//#ifdef PP_ANDROID
//@		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //ANDROID
//@		//
//@		Parser parser = ParserFactory.getDefaultParser();
//@		//
//@		assertNotNull(parser);
//@		assertEquals(new impl.android.com.twitterapime.parser.SAXParser().getClass(), parser.getClass());
		//#endif
	}

	/**
	 * Test method for {@link com.twitterapime.parser.ParserFactory#getParser(int)}.
	 */
	public void testGetParser() {
		//#ifdef PP_JAVA_ME
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //J2ME
		//
		Parser parser = ParserFactory.getParser(ParserFactory.XML);
		//
		assertNotNull(parser);
		assertEquals(new impl.javame.com.twitterapime.parser.KXML2Parser().getClass(), parser.getClass());
		//
		parser = ParserFactory.getParser(ParserFactory.JSON);
		//
		assertNotNull(parser);
		assertEquals(new JSONOrgParser().getClass(), parser.getClass());
		//#endif
		//
		//#ifdef PP_ANDROID
//@		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]); //ANDROID
//@		Parser parser = ParserFactory.getParser(ParserFactory.XML);
//@		//
//@		assertNotNull(parser);
//@		assertEquals(new impl.android.com.twitterapime.parser.SAXParser().getClass(), parser.getClass());
//@		//
//@		parser = ParserFactory.getParser(ParserFactory.JSON);
//@		//
//@		assertNotNull(parser);
//@		assertEquals(new JSONOrgParser().getClass(), parser.getClass());
		//#endif
	}
}
