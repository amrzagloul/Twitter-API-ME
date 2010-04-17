/**
 * 
 */
package com.twitterapime.io.handler;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class HandlerTestSuite extends TestSuite {
	/**
	 * 
	 */
	public HandlerTestSuite() {
		addTest(new HttpResponseCodeErrorHandlerTest());
	}
}