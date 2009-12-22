/**
 * 
 */
package com.twitterapime.platform;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class PlatformProviderSelectorTest extends TestCase {
	/**
	 * 
	 */
	public PlatformProviderSelectorTest() {
		super("PlatformProviderSelectorTest");
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getAvailableProviders()}.
	 */
	public void testGetAvailableProviders() {
		PlatformProvider[] ps = PlatformProviderSelector.getAvailableProviders();
		assertEquals(1, ps.length);
		assertEquals(PlatformProvider.PPID_JAVA_ME, ps[0].getID());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getCurrentProvider()}.
	 */
	public void testGetCurrentProvider() {
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getCurrentProvider());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getDefaultProvider()}.
	 */
	public void testGetDefaultProvider() {
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getDefaultProvider());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#select(com.twitterapime.platform.PlatformProvider)}.
	 */
	public void testSelect() {
		PlatformProvider pd = PlatformProviderSelector.getCurrentProvider();
		//
		try {
			PlatformProviderSelector.select(null);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		PlatformProvider p = new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID);
		assertFalse(pd.equals(p));
		PlatformProviderSelector.select(p);
		assertTrue(p.equals(PlatformProviderSelector.getCurrentProvider()));
		//
		PlatformProviderSelector.select(pd); //keep original state.
	}
}