/**
 * 
 */
package com.twitterapime.model;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.test.TwitterAPIMETestCase;

/**
 * @author Main
 *
 */
public class DefaultEntityTest extends TwitterAPIMETestCase {
	/**
	 * 
	 */
	private static final String KEY_ARRAY = "KEY_ARRAY";

	/**
	 * 
	 */
	private static final String KEY_DATE = "KEY_DATE";

	/**
	 * 
	 */
	private static final String KEY_INT = "KEY_INT";

	/**
	 * 
	 */
	private static final String KEY_LONG = "KEY_LONG";

	/**
	 * 
	 */
	private static final String KEY_OBJECT = "KEY_OBJECT";

	/**
	 * 
	 */
	private static final String KEY_STRING = "KEY_STRING";

	/**
	 * 
	 */
	private Hashtable dataSample;
	
	/**
	 * 
	 */
	private long now = System.currentTimeMillis();

	/**
	 * 
	 */
	public DefaultEntityTest() {
		super("DefaultEntityTest");
		//
		dataSample = new Hashtable();
		dataSample.put(KEY_ARRAY, new String[] {"Twitter", "API", "ME"});
		dataSample.put(KEY_DATE, new Date(now));
		dataSample.put(KEY_INT, new Integer(7));
		dataSample.put(KEY_LONG, new Long(14));
		dataSample.put(KEY_OBJECT, this);
		dataSample.put(KEY_STRING, "Twitter API ME");
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#hashCode()}.
	 */
	public void testHashCode() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(d.toString().hashCode(), d.hashCode());
		DefaultEntity df = new DefaultEntity();
		assertTrue(d.toString().hashCode() != df.hashCode());
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#DefaultEntity()}.
	 */
	public void testDefaultEntity() {
		DefaultEntity d = new DefaultEntity();
		assertNotNull(d.data);
		assertTrue(d.data.size() == 0);
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#DefaultEntity(java.util.Hashtable)}.
	 */
	public void testDefaultEntityHashtable() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertSame(dataSample, d.data);
		d = new DefaultEntity(null);
		assertNotNull(d.data);
		assertTrue(d.data.size() == 0);
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#setData(java.util.Hashtable)}.
	 */
	public void testSetData() {
		DefaultEntity d = new DefaultEntity();
		d.setData(dataSample);
		assertTrue(dataSample.equals(d.data));
		d.setData((Hashtable)null);
		assertNotNull(d.data);
		assertFalse(dataSample.equals(d.data));
		//
		DefaultEntity d1 = new DefaultEntity();
		d1.setData(d);
		assertTrue(d1.equals(d));
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getArray(java.lang.String)}.
	 */
	public void testGetArray() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(3, d.getArray(KEY_ARRAY).length);
		assertSame(dataSample.get(KEY_ARRAY), d.getArray(KEY_ARRAY));
		assertNull(d.getArray("WHATEVER_ARRAY"));
		//
		try {
			d.getArray(KEY_INT);
			fail();
		} catch (ClassCastException e) {
		} catch (Exception e) {
			fail();
		}
		
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getDate(java.lang.String)}.
	 */
	public void testGetDate() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(new Date(now), d.getDate(KEY_DATE));
		assertNull(d.getDate("WHATEVER_DATE"));
		//
		try {
			d.getDate(KEY_INT);
			fail();
		} catch (ClassCastException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getInt(java.lang.String)}.
	 */
	public void testGetInt() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(7, d.getInt(KEY_INT));
		assertEquals(Integer.MIN_VALUE, d.getInt("WHATEVER_INT"));
		//
		try {
			d.getInt(KEY_STRING);
			fail();
		} catch (ClassCastException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getLong(java.lang.String)}.
	 */
	public void testGetLong() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(14, d.getLong(KEY_LONG));
		assertEquals(Long.MIN_VALUE, d.getLong("WHATEVER_LONG"));
		assertEquals(now, d.getLong(KEY_DATE));
		//
		try {
			d.getLong(KEY_STRING);
			fail();
		} catch (ClassCastException e) {
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getObject(java.lang.String)}.
	 */
	public void testGetObject() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(this, d.getObject(KEY_OBJECT));
		assertNull(d.getObject("WHATEVER_OBJECT"));
		assertTrue(d.getObject(KEY_ARRAY) instanceof String[]);
		assertTrue(d.getObject(KEY_DATE) instanceof Date);
		assertTrue(d.getObject(KEY_INT) instanceof Integer);
		assertTrue(d.getObject(KEY_LONG) instanceof Long);
		assertTrue(d.getObject(KEY_STRING) instanceof String);
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#getString(java.lang.String)}.
	 */
	public void testGetString() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals("Twitter API ME", d.getString(KEY_STRING));
		assertNull(d.getString("WHATEVER_STRING"));
		//
		assertEquals("7", d.getString(KEY_INT));
		assertEquals("14", d.getString(KEY_LONG));
		assertEquals(new Date(now).toString(), d.getString(KEY_DATE));
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertTrue(d.equals(d));
		assertFalse(d.equals(null));
		DefaultEntity dc = new DefaultEntity(dataSample);
		assertTrue(d.equals(dc));
		dc.setData((Hashtable)null);
		assertFalse(d.equals(dc));
	}

	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#toString()}.
	 */
	public void testToString() {
		StringBuffer toStr = new StringBuffer();
		Enumeration keys = dataSample.keys();
		//
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			toStr.append(key + ": " + dataSample.get(key) + "\n");
		}
		assertEquals(toStr.toString(), new DefaultEntity(dataSample).toString());
		assertEquals("", new DefaultEntity().toString());
	}
	
	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#size()}.
	 */
	public void testSize() {
		DefaultEntity d = new DefaultEntity(dataSample);
		assertEquals(dataSample.size(), d.size());
		d = new DefaultEntity(null);
		assertEquals(0, d.size());	
	}
	
	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#checkEmpty(String)}.
	 */
	public void testCheckEmpty() {
		Hashtable t = new Hashtable();
		//
		t.put("a", "");
		t.put("b", "b");
		t.put("d", "    ");
		//
		DefaultEntity e = new DefaultEntity(t);
		//
		try {
			e.checkEmpty("a");
			fail();
		} catch (IllegalArgumentException e2) {
		}
		//
		try {
			e.checkEmpty("b");
		} catch (IllegalArgumentException e2) {
			fail();
		}
		//
		try {
			e.checkEmpty("c");
			fail();
		} catch (IllegalArgumentException e2) {
		}
		//
		try {
			e.checkEmpty("d");
			fail();
		} catch (IllegalArgumentException e2) {
		}
	}
	
	/**
	 * Test method for {@link com.twitterapime.model.DefaultEntity#isEmpty(String)}.
	 */
	public void testIsEmpty() {
		Hashtable t = new Hashtable();
		//
		t.put("a", "");
		t.put("b", "b");
		t.put("d", "    ");
		//
		DefaultEntity e = new DefaultEntity(t);
		//
		assertTrue(e.isEmpty("a"));
		assertFalse(e.isEmpty("b"));
		assertTrue(e.isEmpty("c"));
		assertTrue(e.isEmpty("d"));
	}
}