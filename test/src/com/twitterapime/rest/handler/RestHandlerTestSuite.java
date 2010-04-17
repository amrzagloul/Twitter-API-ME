/**
 * 
 */
package com.twitterapime.rest.handler;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class RestHandlerTestSuite extends TestSuite {
	/**
	 * 
	 */
	public RestHandlerTestSuite() {
		addTest(new AccountHandlerTest());
		addTest(new DirectMessageHandlerTest());
		addTest(new RateLimitStatusHandlerTest());
		addTest(new StatusHandlerTest());
		addTest(new TimelineHandlerTest());
		addTest(new TweetHandlerTest());
		addTest(new UserAccountHandlerTest());
	}
}