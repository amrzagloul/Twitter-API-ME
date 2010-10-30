/**
 * 
 */
package com.twitterapime.util;

import com.sonyericsson.junit.framework.TestCase;

/**
 * @author Main
 *
 */
public class StringUtilTest extends TestCase {
	/**
	 *
	 */
	public StringUtilTest() {
		super("StringUtilTest");
	}

	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#convertTweetDateToLong(java.lang.String)}.
	 */
	public void testConvertTweetDateToLong() {
		try {
			StringUtil.convertTweetDateToLong(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		try {
			StringUtil.convertTweetDateToLong("");
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals(1259403912000l, StringUtil.convertTweetDateToLong("2009-11-28 10:25:12"));
		assertEquals(1259630700000l, StringUtil.convertTweetDateToLong("2009-12-01T01:25:00+00:00"));
		assertEquals(1257629403000l, StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009"));
		assertEquals(1245735000000l, StringUtil.convertTweetDateToLong("2009-06-23X05:30:00"));
		assertEquals(1243364400000l, StringUtil.convertTweetDateToLong("Tue May 26 19:00:00 +0000 2009"));
	}

	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#split(java.lang.String, char)}.
	 */
	public void testSplit() {
		try {
			StringUtil.split(null, '|');
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		String[] vs = new String[] {"Twitter", "API", "ME"};
		String[] vst = StringUtil.split("Twitter|API|ME", '|');
		assertEquals(vs.length, vst.length);
		assertEquals(vs[0], vst[0]);
		assertEquals(vs[1], vst[1]);
		assertEquals(vs[2], vst[2]);
		//
		vst = StringUtil.split("Twitter", '|');
		assertEquals(1, vst.length);
		assertEquals("Twitter", vst[0]);
		//
		vst = StringUtil.split("Twitter API ME", '|');
		assertEquals(1, vst.length);
		assertEquals("Twitter API ME", vst[0]);
		//
		vst = StringUtil.split("", '|');
		assertEquals(1, vst.length);
		//
		vst = StringUtil.split("|", '|');
		assertEquals(2, vst.length);
		//
		vst = StringUtil.split("|Twitter|API|ME|", '|');
		assertEquals(5, vst.length);
		assertEquals("", vst[0]);
		assertEquals("", vst[4]);
	}

	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#formatTweetID(java.lang.String)}.
	 */
	public void testFormatTweetID() {
		//tag:search.twitter.com,2005:6866331107
		try {
			StringUtil.formatTweetID(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals("6866331107", StringUtil.formatTweetID("tag:search.twitter.com,2005:6866331107"));
		assertEquals("6866331107", StringUtil.formatTweetID("6866331107"));
	}

	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#removeTags(java.lang.String)}.
	 */
	public void testRemoveTags() {
		try {
			StringUtil.removeTags(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		String tag1 = "<a>Twitter API ME</a>";
		String tag2 = "<a href=\"http://www.twitterapime.com\">Twitter API ME</a>";
		String tag3 = "<a href=\"http://www.twitterapime.com\"/>";
		String tag4 = "<a href=\"http://www.twitterapime.com\" target=\"_blank\">Twitter API ME</a>";
		String tag5 = "<a href=\"http://www.twitterapime.com\" target=\"_blank\"><b>Twitter API ME</b></a>";
		assertEquals("Twitter API ME", StringUtil.removeTags(tag1));
		assertEquals("Twitter API ME", StringUtil.removeTags(tag2));
		assertEquals("", StringUtil.removeTags(tag3));
		assertEquals("Twitter API ME", StringUtil.removeTags(tag4));
		assertEquals("Twitter API ME", StringUtil.removeTags(tag5));
	}

	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#splitTweetAuthorNames(java.lang.String)}.
	 */
	public void testSplitTweetAuthorNames() {
		try {
			StringUtil.splitTweetAuthorNames(null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		String[] nms = new String[] {"twitterapime", "Twitter API ME"};
		String[] nmst = StringUtil.splitTweetAuthorNames("twitterapime (Twitter API ME)");
		assertEquals(nms.length, nmst.length);
		assertEquals(nms[0], nmst[0]);
		assertEquals(nms[1], nmst[1]);
		//
		nmst = StringUtil.splitTweetAuthorNames("twitterapime");
		assertEquals(2, nmst.length);
		assertEquals("twitterapime", nmst[0]);
		assertEquals("twitterapime", nmst[1]);
	}
	
	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#zeroPad(int, int)}. 
	 */
	public void testZeroPad() {
		assertEquals("1", StringUtil.zeroPad(1, -1));
		assertEquals("1", StringUtil.zeroPad(1, 0));
		assertEquals("1", StringUtil.zeroPad(1, 1));
		assertEquals("01", StringUtil.zeroPad(1, 2));
		assertEquals("001", StringUtil.zeroPad(1, 3));
	}
	
	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#encode(String, String)}. 
	 */
	public void testEncode() {
		try {
			StringUtil.encode(null, null);
			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
		//
		assertEquals("", StringUtil.encode("", null));
		assertEquals("%21*%22%27%28%29%3B%40%2B%24%2C%25%23%5B%5D", StringUtil.encode("!*\"\'();@+$,%#[]", null));
		assertEquals("%22twitter%20api%20me%22", StringUtil.encode("\"twitter api me\"", null));
		assertEquals("twitter%26param1%3Dtwitter%20api%26param2%3Dme", StringUtil.encode("twitter&param1=twitter api&param2=me", null));
	}
	
	/**
	 * Test method for {@link com.twitterapime.util.StringUtil#isEmpty(String).
	 */
	public void testIsEmpty() {
		assertTrue(StringUtil.isEmpty(null));
		assertTrue(StringUtil.isEmpty(""));
		assertFalse(StringUtil.isEmpty("a"));
		assertFalse(StringUtil.isEmpty("dajsdhkajd"));
	}
}