/**
 * 
 */
package com.twitterapime.io;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class IOTestSuite extends TestSuite {
	/**
	 * 
	 */
	public IOTestSuite() {
		addTest(new HttpConnectorTest());
		addTest(new HttpResponseCodeInterpreterTest());
	}
}