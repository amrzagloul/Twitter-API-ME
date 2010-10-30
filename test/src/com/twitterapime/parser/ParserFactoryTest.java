/**
 * 
 */
package com.twitterapime.parser;

import impl.javame.com.twitterapime.parser.JSONOrgParser;
import impl.javame.com.twitterapime.parser.KXML2Parser;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class ParserFactoryTest extends TestCase {
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
		Parser parser = ParserFactory.getDefaultParser();
		//
		assertNotNull(parser);
		assertEquals(new KXML2Parser().getClass(), parser.getClass());
	}

	/**
	 * Test method for {@link com.twitterapime.parser.ParserFactory#getParser(int)}.
	 */
	public void testGetParser() {
		Parser parser = ParserFactory.getParser(ParserFactory.XML);
		//
		assertNotNull(parser);
		assertEquals(new KXML2Parser().getClass(), parser.getClass());
		//
		parser = ParserFactory.getParser(ParserFactory.JSON);
		//
		assertNotNull(parser);
		assertEquals(new JSONOrgParser().getClass(), parser.getClass());
	}
}
