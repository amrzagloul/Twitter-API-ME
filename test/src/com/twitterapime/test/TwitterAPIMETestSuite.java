/**
 * 
 */
package com.twitterapime.test;

import com.sonyericsson.junit.framework.TestSuite;
import com.twitterapime.io.IOTestSuite;
import com.twitterapime.io.handler.IOHandlerTestSuite;
import com.twitterapime.model.ModelTestSuite;
import com.twitterapime.parser.ParserTestSuite;
import com.twitterapime.platform.PlatformTestSuite;
import com.twitterapime.rest.RestTestSuite;
import com.twitterapime.rest.handler.RestHandlerTestSuite;
import com.twitterapime.search.SearchTestSuite;
import com.twitterapime.search.handler.SearchHandlerTestSuite;
import com.twitterapime.util.UtilTestSuite;

/**
 * @author Main
 *
 */
public class TwitterAPIMETestSuite extends TestSuite {
	/**
	 * 
	 */
	public TwitterAPIMETestSuite() {
		addTestSuite(new IOTestSuite().getClass());
		addTestSuite(new IOHandlerTestSuite().getClass());
		addTestSuite(new ModelTestSuite().getClass());
		addTestSuite(new ParserTestSuite().getClass());
		addTestSuite(new PlatformTestSuite().getClass());
		addTestSuite(new RestTestSuite().getClass());
		addTestSuite(new RestHandlerTestSuite().getClass());
		addTestSuite(new SearchTestSuite().getClass());
		addTestSuite(new SearchHandlerTestSuite().getClass());
		addTestSuite(new UtilTestSuite().getClass());
	}
}