/**
 * 
 */
package com.twitterapime.search.handler;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class SearchHandlerTestSuite extends TestSuite {
	/**
	 * 
	 */
	public SearchHandlerTestSuite() {
		addTest(new SearchResultHandlerTest());
	}
}