/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpConnector;

/**
 * @author Main
 *
 */
public class UserAccountManagerTest extends TestCase {
	/**
	 * 
	 */
	public UserAccountManagerTest() {
		super("UserAccountManagerTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getInstance(com.twitterapime.rest.Credential)}.
	 */
	public void testGetInstance() {
		try {
			UserAccountManager.getInstance(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		assertNotNull(uam);
		assertEquals(uam, UserAccountManager.getInstance(c));
		//
		try {
			uam.verifyCredential();
		} catch (Exception e) {
			fail();
		}
		//
		assertSame(uam, UserAccountManager.getInstance(c));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getHttpConn(java.lang.String, com.twitterapime.rest.Credential)}.
	 */
	public void testGetHttpConn() {
		try {
			UserAccountManager.getHttpConn(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			UserAccountManager.getHttpConn("", null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			assertNotNull(UserAccountManager.getHttpConn("http://www.twitterapime.com", null));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential cred = new Credential("username", "password");
			HttpConnection c = UserAccountManager.getHttpConn("http://www.twitter.com", cred);
			assertEquals(c.getRequestProperty("Authorization"), "Basic " + HttpConnector.encodeBase64(cred.getBasicHttpAuthCredential()));
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getRateLimitStatus()}.
	 */
	public void testGetRateLimitStatus() {
		Credential c = new Credential("username", "password");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			uam.getRateLimitStatus();
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			c = new Credential("twiterapimetest", "f00bar");
			uam = UserAccountManager.getInstance(c);
			//
			uam.verifyCredential();
			//
			assertNotNull(uam.getRateLimitStatus());
			assertTrue(uam.getRateLimitStatus().size() > 0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#isVerified()}.
	 */
	public void testIsVerified() {
		Credential c = new Credential("username", "password");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		assertFalse(uam.isVerified());
		//
		try {
			c = new Credential("twiterapimetest", "f00bar");
			uam = UserAccountManager.getInstance(c);
			//
			uam.verifyCredential();
			//
			assertTrue(uam.isVerified());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#verifyCredential()}.
	 */
	public void testVerifyCredential() {
		Credential c = new Credential("twiterapimetest", "foobar");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			assertFalse(uam.verifyCredential());
		} catch (Exception e) {
			fail();
		}
		//
		c = new Credential("twiterapimetest", "f00bar");
		uam = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(uam.verifyCredential());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		Credential c = new Credential("username", "password");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		//
		try {
			uam.getUserAccount();
			fail();
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			c = new Credential("twiterapimetest", "f00bar");
			uam = UserAccountManager.getInstance(c);
			//
			uam.verifyCredential();
			//
			assertNotNull(uam.getUserAccount());
			assertTrue(uam.getUserAccount().size() > 0);
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getCredential()}.
	 */
	public void testGetCredential() {
		Credential c = new Credential("username", "password");
		UserAccountManager uam = UserAccountManager.getInstance(c);
		assertSame(c, uam.getCredential());
	}
}