/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestSuite;

/**
 * @author Main
 *
 */
public class RestTestSuite extends TestSuite {
	/**
	 * 
	 */
	public RestTestSuite() {
		addTest(new CredentialTest());
		addTest(new TimelineTest());
		addTest(new TweetERTest());
		addTest(new UserAccountManagerTest());
		addTest(new UserAccountTest());
		addTest(new GeoLocationTest());
		addTest(new FriendshipManagerTest());
		addTest(new ListTest());
	}
}