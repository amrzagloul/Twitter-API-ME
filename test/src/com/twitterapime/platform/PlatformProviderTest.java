/**
 * 
 */
package com.twitterapime.platform;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class PlatformProviderTest extends TestCase {
	/**
	 *
	 */
	public PlatformProviderTest() {
		super("PlatformProviderTest");
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProvider#hashCode()}.
	 */
	public void testHashCode() {
		PlatformProvider p1 = new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
		PlatformProvider p2 = new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID);
		assertEquals(PlatformProvider.PPID_JAVA_ME, p1.hashCode());
		assertEquals(PlatformProvider.PPID_ANDROID, p2.hashCode());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProvider#getID()}.
	 */
	public void testGetID() {
		PlatformProvider p = new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
		assertEquals(PlatformProvider.PPID_JAVA_ME, p.getID());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProvider#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		PlatformProvider p1 = new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
		PlatformProvider p2 = new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
		PlatformProvider p3 = new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID);
		assertTrue(p1.equals(p1));
		assertTrue(p1.equals(p2));
		assertTrue(p2.equals(p1));
		assertFalse(p1.equals(p3));
		assertFalse(p3.equals(p1));
		assertFalse(p1.equals(null));
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProvider#toString()}.
	 */
	public void testToString() {
		PlatformProvider p = new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
		assertEquals(PlatformProvider.PPNM_JAVA_ME, p.toString());
	}
}