/*
 * HttpConnector.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * <p>
 * This is factory class for creating new HttpConnection objects.
 * </p>
 * <p>
 * The creation of HttpConnection is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * <p>
 * The parameter string that describes the target should conform to the Http URL
 * format as described in RFC 1738.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.4
 * @since 1.0
 * @see HttpConnection
 */
public final class HttpConnector {
	//#ifdef PP_RIM
//@	/**
//@	 * <p>
//@	 * Get some specific connection parameters that must be appended to a URL
//@	 * connection on BlackBerry devices. Those parameters work to indicate which
//@	 * type of connection the device is using at that moment.
//@	 * </p>
//@	 * 
//@	 * @return Connection parameters.
//@	 */
//@	private static final String getBlackBerryConnectionParams() {
//@	    String connParams = "";
//@	    //
//@	    if (net.rim.device.api.system.WLANInfo.getWLANState() == net.rim.device.api.system.WLANInfo.WLAN_STATE_CONNECTED) {
//@	        connParams = ";interface=wifi"; //Connected to a WiFi access point.
//@	    } else {
//@			int coverageStatus = net.rim.device.api.system.CoverageInfo.getCoverageStatus();
//@			net.rim.device.api.servicebook.ServiceRecord record = getWAP2ServiceRecord();
//@			//
//@			if (record != null && (coverageStatus & net.rim.device.api.system.CoverageInfo.COVERAGE_DIRECT) == net.rim.device.api.system.CoverageInfo.COVERAGE_DIRECT) {
//@				// Have network coverage and a WAP 2.0 service book record
//@				connParams = ";deviceside=true;ConnectionUID=" + record.getUid();
//@			} else if ((coverageStatus & net.rim.device.api.system.CoverageInfo.COVERAGE_MDS) == net.rim.device.api.system.CoverageInfo.COVERAGE_MDS) {
//@				// Have an MDS service book and network coverage
//@				connParams = ";deviceside=false";
//@			} else if ((coverageStatus & net.rim.device.api.system.CoverageInfo.COVERAGE_DIRECT) == net.rim.device.api.system.CoverageInfo.COVERAGE_DIRECT) {
//@				// Have network coverage but no WAP 2.0 service book record
//@				connParams = ";deviceside=true";
//@			} else if ((coverageStatus & net.rim.device.api.system.CoverageInfo.COVERAGE_BIS_B) == net.rim.device.api.system.CoverageInfo.COVERAGE_BIS_B) {
//@				connParams = ";deviceside=false;ConnectionType=mds-public";
//@			}
//@	    }
//@	    //
//@	    return connParams;
//@	}
//@
//@	/**
//@	 * <p>
//@	 * Reading the service record for available connection.
//@	 * </p>
//@	 * 
//@	 * @return ServiceRecord Currently in use.
//@	 */
//@	private static final net.rim.device.api.servicebook.ServiceRecord getWAP2ServiceRecord() {
//@	    String cid;
//@	    String uid;
//@		net.rim.device.api.servicebook.ServiceBook sb = net.rim.device.api.servicebook.ServiceBook.getSB();
//@	    net.rim.device.api.servicebook.ServiceRecord[] records = sb.getRecords();
//@	    //
//@	    for (int i = records.length -1; i >= 0; i--) {
//@	        cid = records[i].getCid().toLowerCase();
//@	        uid = records[i].getUid().toLowerCase();
//@	        //
//@	        if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1 && uid.indexOf("mms") == -1) {
//@	            return records[i];
//@	        }
//@	    }
//@	    //
//@	    return null;
//@	}
	//#endif

	/**
	 * <p>
	 * Create and open a HttpConnection.
	 * </p>
	 * 
	 * @param url The URL for the connection.
	 * @return A new HttpConnection object.
	 * @throws IOException If an I/O error occurs.
	 * @throws IllegalArgumentException If url is null or empty.
	 */
	public static HttpConnection open(String url) throws IOException {
		if (url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("URL must not be null/empty.");
		}
		//
		final long PPID = PlatformProviderSelector.getCurrentProvider().getID();
		//
		HttpConnection conn = null;
		String userAgent = null;
		//
		//#ifdef PP_JAVA_ME
		if (PPID == PlatformProvider.PPID_JAVA_ME) {
			conn = new impl.javame.com.twitterapime.io.HttpConnectionImpl();
			userAgent =
				"Twitter API ME/1.7 (compatible; Java ME; MIDP-2.0; CLDC-1.0)";
		}
		//#else
//@		//
		//#ifdef PP_ANDROID
//@		if (PPID == PlatformProvider.PPID_ANDROID) {
//@			conn = new impl.android.com.twitterapime.io.HttpConnectionImpl();
//@			userAgent = "Twitter API ME/1.7 (compatible; Android 1.5)";
//@		}
		//#endif
		//#endif
		//
		if (conn == null) {
			throw new IllegalArgumentException("Unknown platform ID: " + PPID);
		}
		//
		//#ifdef PP_RIM
//@		url += getBlackBerryConnectionParams();
		//#endif
		//
		conn.open(url);
		conn.setRequestProperty("User-Agent", userAgent);
		//
		return conn;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private HttpConnector() {
	}
}
