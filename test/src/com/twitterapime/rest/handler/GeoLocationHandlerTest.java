/**
 * 
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Ernandes Jr
 *
 */
public class GeoLocationHandlerTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	public GeoLocationHandlerTest() {
		super("GeoLocationHandlerTest");
	}
	
	/**
	 * Test method for {@link com.twitterapime.rest.handler.GeoLocationHandler#populate(Hashtable, String, String)}.
	 */
	public void testPopulate() {
		Hashtable sampleRef = new Hashtable();
		sampleRef.put(MetadataSet.GEOLOCATION_COUNTRY, "Brazil");
		sampleRef.put(MetadataSet.GEOLOCATION_LATITUDE, "30");
		sampleRef.put(MetadataSet.GEOLOCATION_LONGITUDE, "20");
		sampleRef.put(MetadataSet.GEOLOCATION_PLACE_FULL_NAME, "Fortaleza, CE");
		sampleRef.put(MetadataSet.GEOLOCATION_PLACE_ID, "123456");
		sampleRef.put(MetadataSet.GEOLOCATION_PLACE_NAME, "Fortaleza");
		sampleRef.put(MetadataSet.GEOLOCATION_PLACE_TYPE, "city");
		sampleRef.put(MetadataSet.GEOLOCATION_PLACE_URL, "http://api.twitter.com/123456");
		sampleRef.put(MetadataSet.GEOLOCATION_POLYGON, new String[] {"123", "456", "789", "0"});
		//
		GeoLocationHandler h = new GeoLocationHandler();
		//
		Hashtable sampleTest = new Hashtable();
		h.populate(sampleTest, "/country", "Brazil");
		h.populate(sampleTest, "/georss:point", "30 20");
		h.populate(sampleTest, "/full_name", "Fortaleza, CE");
		h.populate(sampleTest, "/id", "123456");
		h.populate(sampleTest, "/name", "Fortaleza");
		h.populate(sampleTest, "/place_type", "city");
		h.populate(sampleTest, "/url", "http://api.twitter.com/123456");
		h.populate(sampleTest, "/georss:polygon", "123 456 789 0");
		//
		assertTrue(new GeoLocation(sampleRef).equals(new GeoLocation(sampleTest)));
	}
}
