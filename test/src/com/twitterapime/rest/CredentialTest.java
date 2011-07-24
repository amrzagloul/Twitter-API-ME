/**
 * 
 */
package com.twitterapime.rest;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.test.TwitterAPIMETestCase;
import com.twitterapime.xauth.Token;

/**
 * @author Main
 *
 */
public class CredentialTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public CredentialTest() {
		super("CredentialTest");
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Credential#Credential(java.lang.String, java.lang.String)}.
	 */
	public void testCredential() {
		try {
			new Credential((String)null, (String)null, (String)null, (String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential("", "", "", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential(null, "", "", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential("", null, "", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential(null, null, (Token)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential(null, null, new Token("1", "2"));
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential("AA", "BB", "", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("twitterapime", "password", "1234567890", "0987654321");
			assertEquals("twitterapime", c.getUsernameOrEmail());
			assertEquals("twitterapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertEquals("password", c.getString(MetadataSet.CREDENTIAL_PASSWORD));
			assertEquals("1234567890", c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY));
			assertEquals("0987654321", c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET));
			assertTrue(c.hasUsername());
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Token token = new Token("654321", "654789");
			Credential c = new Credential("1234567890", "0987654321", token);
			assertEquals("1234567890", c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY));
			assertEquals("0987654321", c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET));
			assertSame(token, c.getAccessToken());
			assertTrue(c.hasUsername());
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("twitterapime@twapime.com", "password", "1234567890", "1234567890");
			assertEquals("twitterapime@twapime.com", c.getString(MetadataSet.CREDENTIAL_EMAIL));
			assertEquals("twitterapime@twapime.com", c.getUsernameOrEmail());
			assertEquals("password", c.getString(MetadataSet.CREDENTIAL_PASSWORD));
			assertNull(c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertFalse(c.hasUsername());
			//
			c.setUsername("twapime");
			assertEquals("twapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("twitterapime@twapime.com", "password", "1234567890", "1234567890");
			assertEquals("twitterapime@twapime.com", c.getString(MetadataSet.CREDENTIAL_EMAIL));
			assertEquals("twitterapime@twapime.com", c.getUsernameOrEmail());
			assertEquals("password", c.getString(MetadataSet.CREDENTIAL_PASSWORD));
			assertNull(c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertFalse(c.hasUsername());
			//
			c.setUsername("twapime");
			assertEquals("twapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("1234567890", "1234567890", new Token("654321", "654789"));
			assertNull(c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertFalse(c.hasUsername());
			//
			c.setUsername("twapime");
			assertEquals("twapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
		} catch (Exception e) {
			fail();
		}
	}
}
