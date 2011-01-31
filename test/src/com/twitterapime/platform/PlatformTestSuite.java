/**
 * 
 */
package com.twitterapime.platform;

import junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class PlatformTestSuite extends TestSuite {
	/**
	 * 
	 */
	public PlatformTestSuite() {
		addTest(new PlatformProviderSelectorTest());
		addTest(new PlatformProviderTest());
	}
}