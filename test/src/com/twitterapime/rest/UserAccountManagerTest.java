/**
 * 
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.test.TwitterAPIMETestCase;
import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.Token;

/**
 * @author Main
 *
 */
public class UserAccountManagerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	private static final Hashtable credentialHash = new Hashtable();
	
	/**
	 * 
	 */
	public static final String TEST_USER_1 = "twiterapimetest";
	
	/**
	 * 
	 */
	public static final String TEST_USER_2 = "twiterapimetst2";
	
	/**
	 * 
	 */
	public static final String TEST_USER_3_NON_EXISTENT = "username";
	
	/**
	 * @param username
	 * @param verifiedOrNot
	 * @return
	 * @throws LimitExceededException 
	 * @throws IOException 
	 */
	public static UserAccountManager getUserAccountManager(String username, boolean verifiedOrNot) throws IOException, LimitExceededException {
		UserAccountManager uam = UserAccountManager.getInstance(getUserCredential(username));
		//
		if (verifiedOrNot && !uam.isVerified()) {
			if (!uam.verifyCredential()) {
				throw new IllegalStateException("Login failed for username: " + username);
			}
		}
		if (!verifiedOrNot && uam.isVerified()) {
			uam.signOut();
			uam = getUserAccountManager(username, verifiedOrNot);
		}
		//
		return uam;
	}

	/**
	 * @param username
	 * @return
	 */
	public static UserAccountManager getUserAccountManager(String username) {
		return UserAccountManager.getInstance(getUserCredential(username));
	}
	
	/**
	 * @param username
	 * @return
	 */
	public static Credential getUserCredential(String username) {
		return (Credential)credentialHash.get(username);
	}

	static {
		final String CONSUMER_KEY = "";
		final String CONSUMER_SECRET = "";
		final String TOKEN_ACCESS = "";
		final String TOKEN_SECRET = "";
		//
		boolean useToken = false;
		//
		final String CONSUMER_KEY_USER_1 = StringUtil.isEmpty(CONSUMER_KEY) ? "gJT21KgpjtJvFrvkcsL5w" : CONSUMER_KEY;
		final String CONSUMER_SECRET_USER_1 = StringUtil.isEmpty(CONSUMER_SECRET) ? "uRM24w1KnN5Hno3cgsf77gkeRNQSzd1atLFzLlsk" : CONSUMER_SECRET;
		final String CONSUMER_KEY_USER_2 = StringUtil.isEmpty(CONSUMER_KEY) ? "1WwEJKBNfy2RL0I8QuE1g" : CONSUMER_KEY;
		final String CONSUMER_SECRET_USER_2 = StringUtil.isEmpty(CONSUMER_SECRET) ? "iZL18UgQx1nJUx3DSPbX5ldfd2KgHMPSJA3wFb4Bmmw" : CONSUMER_SECRET;
		//
		final String TOKEN_ACCESS_USER_1 = StringUtil.isEmpty(TOKEN_ACCESS) ? "100090763-iEaRN7aUH589CnBEDVd4HxzIcEeYWkq8Yk0dSJyG" : TOKEN_ACCESS;
		final String TOKEN_SECRET_USER_1 = StringUtil.isEmpty(TOKEN_SECRET) ? "OuV7m01mALwksfsNghk5r4Jo1LGhSIS54nR9rPeE" : TOKEN_SECRET;
		final String TOKEN_ACCESS_USER_2 = StringUtil.isEmpty(TOKEN_ACCESS) ? "134520280-2NnaRzW1mTqRyP7JaKMVLcr7pjSHBiDfFOYW0eMz" : TOKEN_ACCESS;
		final String TOKEN_SECRET_USER_2 = StringUtil.isEmpty(TOKEN_SECRET) ? "K6yf7pA1CjMqEDPyxp9yVRaAfUqA9qEvnp2MMP7z2M" : TOKEN_SECRET;
		//
		Credential credential = null;
		//
		if (useToken && !StringUtil.isEmpty(TOKEN_ACCESS_USER_1) && !StringUtil.isEmpty(TOKEN_SECRET_USER_1)) {
			credential = new Credential(TEST_USER_1, CONSUMER_KEY_USER_1, CONSUMER_SECRET_USER_1, new Token(TOKEN_ACCESS_USER_1, TOKEN_SECRET_USER_1)); 
		} else {
			credential = new Credential(TEST_USER_1, "f00bar", CONSUMER_KEY_USER_1, CONSUMER_SECRET_USER_1);
		}
		//
		credentialHash.put(TEST_USER_1, credential);
		//
		if (useToken && !StringUtil.isEmpty(TOKEN_ACCESS_USER_2) && !StringUtil.isEmpty(TOKEN_SECRET_USER_2)) {
			credential = new Credential(TEST_USER_2, CONSUMER_KEY_USER_2, CONSUMER_SECRET_USER_2, new Token(TOKEN_ACCESS_USER_2, TOKEN_SECRET_USER_2)); 
		} else {
			credential = new Credential(TEST_USER_2, "f00bar", CONSUMER_KEY_USER_2, CONSUMER_SECRET_USER_2);
		}
		//
		credentialHash.put(TEST_USER_2, credential);
		//
		credential = new Credential(TEST_USER_3_NON_EXISTENT, "password", CONSUMER_KEY_USER_1, CONSUMER_SECRET_USER_1);
		//
		credentialHash.put(TEST_USER_3_NON_EXISTENT, credential);
	}
	
	/**
	 * 
	 */
	public UserAccountManagerTest() {
		super("UserAccountManagerTest");
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		try {
			getUserAccountManager(TEST_USER_1, true);
			getUserAccountManager(TEST_USER_2, true);
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() {
		try {
			getUserAccountManager(TEST_USER_1, false);
			getUserAccountManager(TEST_USER_2, false);
		} catch (Exception e) {
			fail();
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
		assertEquals("test: 3", getUserAccountManager(TEST_USER_3_NON_EXISTENT), UserAccountManager.getInstance(getUserCredential(TEST_USER_3_NON_EXISTENT)));
		assertSame("test: 4", getUserAccountManager(TEST_USER_1), UserAccountManager.getInstance(getUserCredential(TEST_USER_1)));
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getRateLimitStatus()}.
	 */
	public void testGetRateLimitStatus() {
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).getRateLimitStatus();
			fail("test: 1");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			RateLimitStatus rsl = getUserAccountManager(TEST_USER_1).getRateLimitStatus();
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
		assertTrue("test: 1", getUserAccountManager(TEST_USER_1).isVerified());
		assertTrue("test: 2", getUserAccountManager(TEST_USER_2).isVerified());
		assertFalse("test: 3", getUserAccountManager(TEST_USER_3_NON_EXISTENT).isVerified());
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#verifyCredential()}.
	 */
	public void testVerifyCredential() {
		try {
			assertFalse(getUserAccountManager(TEST_USER_3_NON_EXISTENT).verifyCredential());
		} catch (Exception e) {
			fail("test: 1");
		}
		//
		try {
			assertTrue("test: 2", getUserAccountManager(TEST_USER_1).verifyCredential());
			assertTrue("test: 3", getUserAccountManager(TEST_USER_2).verifyCredential());
		} catch (Exception e) {
			fail("test: 4");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).getUserAccount();
			fail("test: 1");
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			UserAccount ua = getUserAccountManager(TEST_USER_1).getUserAccount();
			//
			assertNotNull("test: 3", ua);
			assertEquals(TEST_USER_1, ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			assertTrue("test: 4", ua.size() > 0);
		} catch (Exception e) {
			fail("test: 5");
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getCredential()}.
	 */
	public void testGetCredential() {
		assertSame("test: 1", getUserCredential(TEST_USER_1), getUserAccountManager(TEST_USER_1).getCredential());
		assertSame("test: 2", getUserCredential(TEST_USER_2), getUserAccountManager(TEST_USER_2).getCredential());
		assertSame("test: 3", getUserCredential(TEST_USER_3_NON_EXISTENT), getUserAccountManager(TEST_USER_3_NON_EXISTENT).getCredential());
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#follow(UserAccount)}.
	 */
	public void testFollow() {
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			getUserAccountManager(TEST_USER_1).follow(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).follow(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).follow(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (getUserAccountManager(TEST_USER_1).isFollowing(ua)) {
				getUserAccountManager(TEST_USER_1).unfollow(ua);
			}
			//
			ua = getUserAccountManager(TEST_USER_1).follow(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue("test: 9", getUserAccountManager(TEST_USER_1).isFollowing(ua));
		} catch (Exception e) {
			fail("test: 10 -> " + e.getClass().getName());
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).follow(ua);
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).follow(new UserAccount("jdahsjkdhadkja"));
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
			getUserAccountManager(TEST_USER_1).unfollow(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).unfollow(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).unfollow(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (!getUserAccountManager(TEST_USER_1).isFollowing(ua)) {
				getUserAccountManager(TEST_USER_1).follow(ua);
			}
			//
			ua = getUserAccountManager(TEST_USER_1).unfollow(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse("test: 9", getUserAccountManager(TEST_USER_1).isFollowing(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).unfollow(ua);
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).unfollow(new UserAccount("jdahsjkdhadkja"));
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
			getUserAccountManager(TEST_USER_1).block(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).block(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).block(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (getUserAccountManager(TEST_USER_1).isBlocking(ua)) {
				getUserAccountManager(TEST_USER_1).unblock(ua);
			}
			//
			ua = getUserAccountManager(TEST_USER_1).block(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue("test: 9", getUserAccountManager(TEST_USER_1).isBlocking(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).block(new UserAccount("jdahsjkdhadkja"));
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
			getUserAccountManager(TEST_USER_1).unblock(null);
			fail("test: 1");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 2");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).unblock(new UserAccount());
			fail("test: 3");
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail("test: 4");
		}
		//
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).unblock(ua);
			fail("test: 5");
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail("test: 6");
		}
		//
		try {
			if (!getUserAccountManager(TEST_USER_1).isBlocking(ua)) {
				getUserAccountManager(TEST_USER_1).block(ua);
			}
			//
			ua = getUserAccountManager(TEST_USER_1).unblock(ua);
			//
			assertNotNull("test: 7", ua);
			assertEquals("test: 8", "twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse("test: 9", getUserAccountManager(TEST_USER_1).isBlocking(ua));
		} catch (Exception e) {
			fail("test: 10");
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).unblock(new UserAccount("jdahsjkdhadkja"));
			fail("test: 11");
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail("test: 12");
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#updateProfile(UserAccount)}.
	 */
	public void testUpdateProfile() {
		try {
			getUserAccountManager(TEST_USER_3_NON_EXISTENT).updateProfile(null);
		} catch (SecurityException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).updateProfile(null);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			getUserAccountManager(TEST_USER_1).updateProfile(new UserAccount());
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Hashtable d1 = new Hashtable();
			d1.put(MetadataSet.USERACCOUNT_NAME, "aaaaaaaaaabbbbbbbbbbc");
			getUserAccountManager(TEST_USER_1).updateProfile(new UserAccount(d1));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Hashtable d1 = new Hashtable();
			d1.put(MetadataSet.USERACCOUNT_DESCRIPTION, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			getUserAccountManager(TEST_USER_1).updateProfile(new UserAccount(d1));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		Hashtable d = new Hashtable();
		d.put(MetadataSet.USERACCOUNT_NAME, "TwAPIme" + System.currentTimeMillis());
		d.put(MetadataSet.USERACCOUNT_DESCRIPTION, "Description " + System.currentTimeMillis());
		d.put(MetadataSet.USERACCOUNT_URL, "http://www.twapime.com");
		d.put(MetadataSet.USERACCOUNT_LOCATION, "Fortaleza " + System.currentTimeMillis());
		UserAccount u = new UserAccount(d);
		//
		try {
			UserAccount nu = getUserAccountManager(TEST_USER_1).updateProfile(u);
			//
			assertEquals(u.getString(MetadataSet.USERACCOUNT_NAME), nu.getString(MetadataSet.USERACCOUNT_NAME));
			assertEquals(u.getString(MetadataSet.USERACCOUNT_DESCRIPTION), nu.getString(MetadataSet.USERACCOUNT_DESCRIPTION));
			assertEquals(u.getString(MetadataSet.USERACCOUNT_URL), nu.getString(MetadataSet.USERACCOUNT_URL));
			assertEquals(u.getString(MetadataSet.USERACCOUNT_LOCATION), nu.getString(MetadataSet.USERACCOUNT_LOCATION));
		} catch (Exception e) {
			fail();
		}
	}
}