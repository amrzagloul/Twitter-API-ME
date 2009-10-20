/*
 * PlatformProviderSelector.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.platform;

import java.util.Vector;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class PlatformProviderSelector {
	//#ifdef PP_JAVA_ME
	//#define PP_JAVA_ME = 1
	//#else
	//#define PP_JAVA_ME = 0
	//#endif

	//#ifdef PP_ANDROID
	//#define PP_ANDROID = 1
	//#else
	//#define PP_ANDROID = 0
	//#endif

	//#if PP_JAVA_ME == 0 && PP_ANDROID == 0
	//#define PP_ALL = 1
	//#else
	//#define PP_ALL = 0
	//#endif

	//#if PP_JAVA_ME == 1 || PP_ALL == 1
	/**
	 * 
	 */
	private static final PlatformProvider javameProvider =
		new PlatformProvider(
			PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
	//#endif

	//#if PP_ANDROID == 1 || PP_ALL == 1
	/**
	 * 
	 */
	private static final PlatformProvider androidProvider =
		new PlatformProvider(
			PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID);
	//#endif
	
	/**
	 * 
	 */
	//#if PP_JAVA_ME == 1 || PP_ALL == 1
	private static final PlatformProvider defaultPlatform = javameProvider;
	//#elif PP_ANDROID == 1
//@	private static final PlatformProvider defaultPlatform = androidProvider;
	//#endif

	/**
	 * 
	 */
	private static PlatformProvider currentPlatform;
	
	static {
		select(defaultPlatform);
	}

	/**
	 * 
	 */
	PlatformProviderSelector() {
	}

	/**
	 * @return
	 */
	public static PlatformProvider[] getAvailableProviders() {
		Vector v = new Vector(2);
		//#if PP_JAVA_ME == 1 || PP_ALL == 1
		v.addElement(javameProvider);
		//#endif
		//#if PP_ANDROID == 1 || PP_ALL == 1
		v.addElement(androidProvider);
		//#endif
		//
		PlatformProvider[] plats = new PlatformProvider[v.size()];
		v.copyInto(plats);
		//
		return plats;
	}

	/**
	 * @return
	 */
	public static PlatformProvider getCurrentProvider() {
		return currentPlatform;
	}

	/**
	 * @return
	 */
	public static PlatformProvider getDefaultProvider() {
		return defaultPlatform;
	}

	/**
	 * 
	 * @param pp
	 */
	public static void select(PlatformProvider pp) {
		currentPlatform = pp;
	}
}
