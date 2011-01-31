/**
 * 
 */
package com.twitterapime.test;

import junit.framework.TestCase;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * @author 82177082315
 *
 */
public abstract class TwitterAPIMETestCase extends TestCase {
	/**
	 * 
	 */
	private final long platform;

	/**
	 * @param name
	 * @param checkPlatform
	 */
	public TwitterAPIMETestCase(String name, long checkPlatform) {
		super(name);
		//
		PlatformProvider[] pts =
			PlatformProviderSelector.getAvailableProviders();
		//
		for (int i = 0; i < pts.length; i++) {
			if (pts[i].getID() == checkPlatform) {
				PlatformProviderSelector.select(pts[i]);
			}
		}
		//
		platform = PlatformProviderSelector.getCurrentProvider().getID();
		//
		assertEquals("Expected platform:", platform, checkPlatform);
	}

	/**
	 * 
	 */
	public TwitterAPIMETestCase() {
		this("TwitterAPIMETestCase", PlatformProvider.PPID_ANDROID);
	}

	/**
	 * @param name
	 */
	public TwitterAPIMETestCase(String name) {
		this(name, PlatformProvider.PPID_ANDROID);
	}
}
