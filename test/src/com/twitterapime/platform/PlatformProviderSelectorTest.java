/**
 * 
 */
package com.twitterapime.platform;

import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class PlatformProviderSelectorTest extends TwitterAPIMETestCase {
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
		assertEquals(2, ps.length);
		assertEquals(PlatformProvider.PPID_JAVA_ME, ps[0].getID());
		assertEquals(PlatformProvider.PPID_ANDROID, ps[1].getID());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getCurrentProvider()}.
	 */
	public void testGetCurrentProvider() {
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]);
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getCurrentProvider());
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[1]);
		assertEquals(new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID), PlatformProviderSelector.getCurrentProvider());
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getDefaultProvider()}.
	 */
	public void testGetDefaultProvider() {
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getDefaultProvider());
	}
}