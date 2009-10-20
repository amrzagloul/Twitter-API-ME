/*
 * PlatformProvider.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.platform;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class PlatformProvider {
	/**
	 * 
	 */
	public static final long PPID_JAVA_ME = 1;
	
	/**
	 * 
	 */
	static final String PPNM_JAVA_ME = "Java Micro Edition platform";
	
	/**
	 * 
	 */
	public static final long PPID_ANDROID = 2;
	
	/**
	 * 
	 */
	static final String PPNM_ANDROID = "Android platform";

	/**
	 * 
	 */
	private long id;

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 * @param id
	 * @param name
	 */
	PlatformProvider(long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return
	 */
	public long getID() {
		return id;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PlatformProvider)) {
			return false;
		} else {
			return id == ((PlatformProvider)o).id;
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
