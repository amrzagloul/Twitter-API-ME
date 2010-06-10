/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.InvalidQueryException;

/**
 * @author Main
 *
 */
public class UserAccountManagerTest extends TestCase {
	/**
	 * 
	 */
	public static final String CONSUMER_KEY = "KQlYF5kzKrBHm6s9gOyAVQ";
	
	/**
	 * 
	 */
	public static final String CONSUMER_SECRET = "yv57uIvC8CMNo6NPyebwyDwbbw306xuXew4U5x81Ljw";
	
	/**
	 * 
	 */
	private Credential credential1;
	
	/**
	 * 
	 */
	private Credential credential2;

	/**
	 * 
	 */
	private Credential credential3;

	/**
	 * 
	 */
	private UserAccountManager userMngr1;
	
	/**
	 * 
	 */
	private UserAccountManager userMngr2;

	/**
	 * 
	 */
	private UserAccountManager userMngr3;
	
	/**
	 * 
	 */
	public UserAccountManagerTest() {
		super("UserAccountManagerTest");
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Throwable {
		String conKey = UserAccountManagerTest.CONSUMER_KEY;
		String conSec = UserAccountManagerTest.CONSUMER_SECRET;
		//
		credential1 = new Credential("twiterapimetest", "f00bar", conKey, conSec);
		credential2 = new Credential("twiterapimetst2", "f00bar", conKey, conSec);
		credential3 = new Credential("username", "password", conKey, conSec);
		//
		userMngr1 = UserAccountManager.getInstance(credential1);
		userMngr2 = UserAccountManager.getInstance(credential2);
		userMngr3 = UserAccountManager.getInstance(credential3);
		//
		if (!(userMngr1.verifyCredential() && userMngr2.verifyCredential() && !userMngr3.verifyCredential())) {
			throw new IllegalStateException("TweetERTest: Login failed!");
		}
	}
	
	/**
	 * @see com.sonyericsson.junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Throwable {
		userMngr1.signOut();
		userMngr2.signOut();
		//
		try {
			userMngr1.verifyCredential();
			//
			throw new IllegalStateException("UserAccountManagerTest: Sign out failed!");
		} catch (IllegalStateException e) {
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getInstance(com.twitterapime.rest.Credential)}.
	 */
	public void testGetInstance() {
		try {
			UserAccountManager.getInstance(null);
			fail("test: 1");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		assertEquals("test: 3", userMngr3, UserAccountManager.getInstance(credential3));
		assertSame("test: 4", userMngr1, UserAccountManager.getInstance(credential1));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getRateLimitStatus()}.
	 */
	public void testGetRateLimitStatus() {
		try {
			userMngr3.getRateLimitStatus();
			fail("test: 1");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			RateLimitStatus rsl = userMngr1.getRateLimitStatus();
			//
			assertNotNull("test: 3", rsl);
			assertTrue("test: 4", rsl.size() > 0);
		} catch (Exception e) {
			fail("test: 5");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#isVerified()}.
	 */
	public void testIsVerified() {
		assertTrue("test: 1", userMngr1.isVerified());
		assertTrue("test: 2", userMngr2.isVerified());
		assertFalse("test: 3", userMngr3.isVerified());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#verifyCredential()}.
	 */
	public void testVerifyCredential() {
		try {
			assertFalse(userMngr3.verifyCredential());
		} catch (Exception e) {
			fail("test: 1");
		}
		//
		try {
			assertTrue("test: 2", userMngr1.verifyCredential());
			assertTrue("test: 3", userMngr2.verifyCredential());
		} catch (Exception e) {
			fail("test: 4");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		try {
			userMngr3.getUserAccount();
			fail("test: 1");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			UserAccount ua = userMngr1.getUserAccount();
			//
			assertNotNull("test: 3", ua);
			assertTrue("test: 4", ua.size() > 0);
		} catch (Exception e) {
			fail("test: 5");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getCredential()}.
	 */
	public void testGetCredential() {
		assertSame("test: 1", credential1, userMngr1.getCredential());
		assertSame("test: 2", credential2, userMngr2.getCredential());
		assertSame("test: 3", credential3, userMngr3.getCredential());
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#follow(UserAccount)}.
	 */
	public void testFollow() {
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			userMngr1.follow(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			userMngr1.follow(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			userMngr3.follow(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (userMngr1.isFollowing(ua)) {
				userMngr1.unfollow(ua);
			}
			//
			ua = userMngr1.follow(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue("test: 9", userMngr1.isFollowing(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			userMngr1.follow(ua);
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
		//
		try {
			userMngr1.follow(new UserAccount("jdahsjkdhadkja"));
			fail("test: 13");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 14");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#unfollow(UserAccount)}.
	 */
	public void testUnfollow() {
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			userMngr1.unfollow(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			userMngr1.unfollow(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			userMngr3.unfollow(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (!userMngr1.isFollowing(ua)) {
				userMngr1.follow(ua);
			}
			//
			ua = userMngr1.unfollow(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse("test: 9", userMngr1.isFollowing(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			userMngr1.unfollow(ua);
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
		//
		try {
			userMngr1.unfollow(new UserAccount("jdahsjkdhadkja"));
			fail("test: 13");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 14");
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#block(UserAccount)}.
	 */
	public void testBlock() {
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			userMngr1.block(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			userMngr1.block(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			userMngr3.block(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (userMngr1.isBlocking(ua)) {
				userMngr1.unblock(ua);
			}
			//
			ua = userMngr1.block(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue("test: 9", userMngr1.isBlocking(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			userMngr1.block(new UserAccount("jdahsjkdhadkja"));
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#unblock(UserAccount)}.
	 */
	public void testUnblock() {
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			userMngr1.unblock(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			userMngr1.unblock(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			userMngr2.unblock(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (!userMngr1.isBlocking(ua)) {
				userMngr1.block(ua);
			}
			//
			ua = userMngr1.unblock(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse("test: 9", userMngr1.isBlocking(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			userMngr1.unblock(new UserAccount("jdahsjkdhadkja"));
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
	}
}