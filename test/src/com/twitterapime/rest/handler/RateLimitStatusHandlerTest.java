/**
 * 
 */
package com.twitterapime.rest.handler;

import java.io.IOException;
import java.io.InputStream;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.RateLimitStatus;

/**
 * @author Main
 *
 */
public class RateLimitStatusHandlerTest extends TestCase {
	/**
	 * 
	 */
	public RateLimitStatusHandlerTest() {
		super("RateLimitStatusHandlerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.handler.RateLimitStatusHandler#getParsedRateLimitStatus()}.
	 */
	public void testGetParsedRateLimitStatus() {
		InputStream errorXML = null;
		RateLimitStatusHandler handler = new RateLimitStatusHandler();
		//
		try {
			errorXML = getClass().getResourceAsStream("/xml/twitterapi-rate-limit-status.xml");
			ParserFactory.getDefaultParser().parse(errorXML, handler);
		} catch (Exception e) {
			fail();
		} finally {
			if (errorXML != null) {
				try {
					errorXML.close();
				} catch (IOException e) {
				}
			}
		}
		//
		RateLimitStatus rls = handler.getParsedRateLimitStatus();
		//
		assertEquals(3, rls.size());
		assertEquals("150", rls.getString(MetadataSet.RATELIMITSTATUS_HOURLY_LIMIT));
		assertEquals("150", rls.getString(MetadataSet.RATELIMITSTATUS_REMAINING_HITS));
		assertEquals("1259630700000", rls.getString(MetadataSet.RATELIMITSTATUS_RESET_TIME));
		//
		assertEquals(0, new RateLimitStatusHandler().getParsedRateLimitStatus().size());
	}
}