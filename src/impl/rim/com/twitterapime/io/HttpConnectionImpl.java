/*
 * HttpConnectionImpl.java
 * 01/08/2011
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.rim.com.twitterapime.io;

import java.io.IOException;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.WLANInfo;

/**
 * <p>
 * This class defines the implementation of HttpConnection for RIM platform.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class HttpConnectionImpl 
	extends	impl.javame.com.twitterapime.io.HttpConnectionImpl {
	/**
	 * <p>
	 * String of connection parameters to be appended to string connection 
	 * (URL + default parameters).
	 * </p>
	 */
	private static String appendConnectionParameters;
	
	/**
	 * <p>
	 * String of connection parameters to be appended to URL.
	 * </p>
	 */
	private static String connectionParameters;

	/**
	 * <p>
	 * Reading the service record for available connection.
	 * </p>
	 * @return ServiceRecord Currently in use.
	 */
	private static ServiceRecord getWAP2ServiceRecord() {
	    String cid;
	    String uid;
		ServiceBook sb = ServiceBook.getSB();
	    ServiceRecord[] records = sb.getRecords();
	    //
	    for (int i = records.length -1; i >= 0; i--) {
	        cid = records[i].getCid().toLowerCase();
	        uid = records[i].getUid().toLowerCase();
	        //
	        if (cid.indexOf("wptcp") != -1 
	        		&& uid.indexOf("wifi") == -1 
	        		&& uid.indexOf("mms") == -1) {
	            return records[i];
	        }
	    }
	    //
	    return null;
	}

	/**
	 * <p>
	 * Get some specific connection parameters that must be appended to a URL
	 * connection on RIM devices. Those parameters work to indicate which
	 * type of connection the device is using at that moment.
	 * </p>
	 * @return Connection parameters.
	 */
	public static String getConnectionParams() {
	    String connParams = "";
	    //
	    if (connectionParameters != null) {
	    	connParams = connectionParameters;
	    } else {
		    if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
		        connParams = ";interface=wifi"; //Connected to a WiFi access point.
		    } else {
				int coverageStatus = CoverageInfo.getCoverageStatus();
				ServiceRecord record = getWAP2ServiceRecord();
				//
				if (record != null && (coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
					// Have network coverage and a WAP 2.0 service book record
					connParams = ";deviceside=true;ConnectionUID=" + record.getUid();
				} else if ((coverageStatus & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
					// Have an MDS service book and network coverage
					connParams = ";deviceside=false";
				} else if ((coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
					// Have network coverage but no WAP 2.0 service book record
					connParams = ";deviceside=true";
				} else if ((coverageStatus & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B) {
					connParams = ";deviceside=false;ConnectionType=mds-public";
				}
		    }
		    //
		    if (appendConnectionParameters != null) {
		    	connParams += appendConnectionParameters;
		    }
	    }
	    //
	    return connParams;
	}

	/**
	 * <p>
	 * Set the connection parameters to be appended to string connection (URL +
	 * default parameters) to be requestesd. Use this method to apeend 
	 * additional parameters to the string connection.
	 * </p>
	 * @param params Parameters.
	 */
	public static void setAppendConnectionParameters(String params) {
    	if (params != null && !params.startsWith(";")) {
    		params = ";" + params;
    	}
    	//
		appendConnectionParameters = params;
	}

	/**
	 * <p>
	 * Set the connection parameters to be appended to URL to be requestesd. 
	 * This removes any additional parameters appended automatically to the
	 * string connection. 
	 * </p>
	 * @param params Parameters.
	 */
	public static void setConnectionParameters(String params) {
    	if (params != null && !params.startsWith(";")) {
    		params = ";" + params;
    	}
    	//
		connectionParameters = params;
	}

	/**
	 * @see impl.javame.com.twitterapime.io.HttpConnectionImpl#open(java.lang.String)
	 */
	public void open(String url) throws IOException {
		super.open(url + getConnectionParams());
	}
}
