/**
 * 
 */
package com.twitterapime.model;

import junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class ModelTestSuite extends TestSuite {
	/**
	 * 
	 */
	public ModelTestSuite() {
		addTest(new DefaultEntityTest());
	}
}