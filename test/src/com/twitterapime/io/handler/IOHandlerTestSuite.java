/**
 * 
 */
package com.twitterapime.io.handler;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class IOHandlerTestSuite extends TestSuite {
	/**
	 * 
	 */
	public IOHandlerTestSuite() {
		addTest(new HttpResponseCodeErrorHandlerTest());
	}
}