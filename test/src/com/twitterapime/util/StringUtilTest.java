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
		assertEquals(1259403912L, StringUtil.convertTweetDateToLong("2009-11-28 10:25:12"));
		assertEquals(1259630700L, StringUtil.convertTweetDateToLong("2009-12-01T01:25:00+00:00"));
		assertEquals(1257629403L, StringUtil.convertTweetDateToLong("Sat Nov 07 21:30:03 +0000 2009"));
		assertEquals(1245735000L, StringUtil.convertTweetDateToLong("2009-06-23X05:30:00"));
		assertEquals(1243364400L, StringUtil.convertTweetDateToLong("Tue May 26 19:00:00 +0000 2009"));
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
}