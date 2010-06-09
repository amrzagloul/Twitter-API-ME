/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;

/**
 * @author Main
 *
 */
public class CredentialTest extends TestCase {
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
			new Credential(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential("", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential(null, "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			new Credential("", null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("twitterapime", "password");
			assertEquals("twitterapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertEquals("password", c.getString(MetadataSet.CREDENTIAL_PASSWORD));
			assertFalse(c.hasXAuthCredentials());
		} catch (Exception e) {
			fail();
		}
		//
		try {
			Credential c = new Credential("twitterapime", "password", "1234567890", "0987654321");
			assertEquals("twitterapime", c.getString(MetadataSet.CREDENTIAL_USERNAME));
			assertEquals("password", c.getString(MetadataSet.CREDENTIAL_PASSWORD));
			assertEquals("1234567890", c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY));
			assertEquals("0987654321", c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET));
			assertTrue(c.hasXAuthCredentials());
			//
			c = new Credential("twitterapime", "password", "", "");
			assertNull(c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY));
			assertNull(c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET));
			assertFalse(c.hasXAuthCredentials());
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.rest.Credential#getBasicHttpAuthCredential()}.
	 */
	public void testGetBasicHttpAuthCredential() {
		Credential c = new Credential("twitterapime", "password");
		assertEquals("twitterapime:password", c.getBasicHttpAuthCredential());
	}
}