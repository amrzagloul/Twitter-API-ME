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
		assertEquals(1, ps.length);
		//#ifdef PP_JAVA_ME
		assertEquals(PlatformProvider.PPID_JAVA_ME, ps[0].getID());
		//#endif
		//#ifdef PP_ANDROID
//@		assertEquals(PlatformProvider.PPID_ANDROID, ps[0].getID());
		//#endif
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getCurrentProvider()}.
	 */
	public void testGetCurrentProvider() {
		//#ifdef PP_JAVA_ME
		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]);
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getCurrentProvider());
		//#endif
		//#ifdef PP_ANDROID
//@		PlatformProviderSelector.select(PlatformProviderSelector.getAvailableProviders()[0]);
//@		assertEquals(new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID), PlatformProviderSelector.getCurrentProvider());
		//#endif
	}

	/**
	 * Test method for {@link com.twitterapime.platform.PlatformProviderSelector#getDefaultProvider()}.
	 */
	public void testGetDefaultProvider() {
		//#ifdef PP_JAVA_ME
		assertEquals(new PlatformProvider(PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME), PlatformProviderSelector.getDefaultProvider());
		//#endif
		//#ifdef PP_ANDROID
//@		assertEquals(new PlatformProvider(PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID), PlatformProviderSelector.getDefaultProvider());
		//#endif
	}
}
