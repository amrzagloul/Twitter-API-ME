/**
 * 
 */
package com.twitterapime.rest;

import com.sonyericsson.junit.framework.TestCase;
import com.twitterapime.model.MetadataSet;


/**
 * @author Ernandes Jr
 *
 */
public class GeoLocationTest extends TestCase {
	/**
	 * 
	 */
	public GeoLocationTest() {
		super("GeoLocationTest");
	}
	
	public void testGeoLocation() {
		try {
			new GeoLocation((String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 1");
		}
		//
		try {
			new GeoLocation("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 2");
		}
		//
		try {
			GeoLocation l = new GeoLocation("123456");
			assertEquals("123456", l.getString(MetadataSet.GEOLOCATION_PLACE_ID));
		} catch (Exception e) {
			fail("Test 3");
		}
		//
		try {
			new GeoLocation((String)null, (String)null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 4");
		}
		//
		try {
			new GeoLocation("", "");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 5");
		}
		//
		try {
			new GeoLocation(null, "36");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 6");
		}
		//
		try {
			new GeoLocation("50", null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("Test 7");
		}
		//
		try {
			GeoLocation l = new GeoLocation("50", "36");
			assertEquals("50", l.getString(MetadataSet.GEOLOCATION_LATITUDE));
			assertEquals("36", l.getString(MetadataSet.GEOLOCATION_LONGITUDE));
		} catch (Exception e) {
			fail("Test 8");
		}
	}
}