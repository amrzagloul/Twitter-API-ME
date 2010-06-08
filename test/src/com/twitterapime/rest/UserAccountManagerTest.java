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
		//
		try {
			uam.signOut();
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
			assertTrue(uam.verifyCredential());
			//
			assertNotNull(uam.getRateLimitStatus());
			assertTrue(uam.getRateLimitStatus().size() > 0);
		} catch (Exception e) {
			fail(e.toString());
		}
		//
		try {
			uam.signOut();
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
		//
		try {
			uam.signOut();
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
		//
		try {
			uam.signOut();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#getUserAccount()}.
	 */
	public void testGetUserAccount() {
		Credential c = new Credential("twiterapimetest", "f00bar");
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
			System.out.println(e);
			fail();
		}
		//
		try {
			uam.signOut();
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
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#follow(UserAccount)}.
	 */
	public void testFollow() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			u.follow(null);
			fail();
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			u.follow(new UserAccount());
			fail();
		} catch (IllegalArgumentException e1) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			UserAccountManager.getInstance(new Credential("username", "password")).follow(ua);
			fail();
		} catch (SecurityException e1) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			//
			assertTrue(u.verifyCredential());
			//
			if (u.isFollowing(ua)) {
				u.unfollow(ua);
			}
			//
			ua = u.follow(ua);
			//
			assertNotNull(ua);
			assertEquals("twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue(u.isFollowing(ua));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			u.follow(ua);
			fail();
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			u.follow(new UserAccount("jdahsjkdhadkja"));
			fail();
		} catch (InvalidQueryException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			u.signOut();
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#unfollow(UserAccount)}.
	 */
	public void testUnfollow() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			try {
				u.unfollow(null);
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				u.unfollow(new UserAccount());
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				UserAccountManager.getInstance(new Credential("username", "password")).unfollow(ua);
				fail();
			} catch (SecurityException e1) {
			}
			//
			assertTrue(u.verifyCredential());
			//
			if (!u.isFollowing(ua)) {
				u.follow(ua);
			}
			//
			ua = u.unfollow(ua);
			//
			assertNotNull(ua);
			assertEquals("twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse(u.isFollowing(ua));
			//
			try {
				u.unfollow(ua);
				fail();
			} catch (InvalidQueryException e) {
			}
			//
			try {
				u.unfollow(new UserAccount("jdahsjkdhadkja"));
				fail();
			} catch (InvalidQueryException e) {
			}
		} catch (Exception e) {
			fail();
		} finally {
			try {
				u.follow(ua);
			} catch (Exception e) {
				fail();
			}
		}
		//
		try {
			u.signOut();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#block(UserAccount)}.
	 */
	public void testBlock() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			try {
				u.block(null);
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				u.block(new UserAccount());
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				UserAccountManager.getInstance(new Credential("username", "password")).block(ua);
				fail();
			} catch (SecurityException e1) {
			}
			//
			assertTrue(u.verifyCredential());
			//
			if (u.isBlocking(ua)) {
				u.unblock(ua);
			}
			//
			ua = u.block(ua);
			//
			assertNotNull(ua);
			assertEquals("twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertTrue(u.isBlocking(ua));
			//
			try {
				u.block(new UserAccount("jdahsjkdhadkja"));
				fail();
			} catch (InvalidQueryException e) {
			}
		} catch (Exception e) {
			fail();
		} finally {
			try {
				u.unblock(ua);
			} catch (Exception e) {
				fail();
			}
		}
		//
		try {
			u.signOut();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#unblock(UserAccount)}.
	 */
	public void testUnblock() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		UserAccount ua = new UserAccount("twapime");
		//
		try {
			try {
				u.unblock(null);
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				u.unblock(new UserAccount());
				fail();
			} catch (IllegalArgumentException e1) {
			}
			//
			try {
				UserAccountManager.getInstance(new Credential("username", "password")).unblock(ua);
				fail();
			} catch (SecurityException e1) {
			}
			//
			assertTrue(u.verifyCredential());
			//
			if (!u.isBlocking(ua)) {
				u.block(ua);
			}
			//
			ua = u.unblock(ua);
			//
			assertNotNull(ua);
			assertEquals("twapime", ua.getString(MetadataSet.USERACCOUNT_USER_NAME));
			//
			assertFalse(u.isBlocking(ua));
			//
			try {
				u.unblock(new UserAccount("jdahsjkdhadkja"));
				fail();
			} catch (InvalidQueryException e) {
			}
		} catch (Exception e) {
			fail();
		}
		//
		try {
			u.signOut();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.UserAccountManager#signOut()}.
	 */
	public void testSignOut() {
		Credential c = new Credential("twiterapimetest", "f00bar");
		UserAccountManager u = UserAccountManager.getInstance(c);
		//
		try {
			assertTrue(u.verifyCredential());
			assertTrue(u.isVerified());
			//
			u.signOut();
			//
			assertNotSame(u, UserAccountManager.getInstance(c));
			//
			try {
				u.verifyCredential();
				fail();
			} catch (IllegalStateException e) {
			}
		} catch (Exception e) {
			fail();
		}
	}
}