package com.twitterapime.util;

import com.twitterapime.test.TwitterAPIMETestCase;

public class QSortTest extends TwitterAPIMETestCase {

	public QSortTest() {
		super("QSortTest");
	}

	/**
	 * Test method for {@link com.twitterapime.util.QSort#quicksort(Object[], int, int)}.
	 */
	public void testQuicksort() {
		String[] l = {"e", "d", "c", "b", "a"};
		QSort qsort = new QSort();
		qsort.quicksort(l, 0, l.length -1);
		//
		assertTrue(qsort.isAscendingSort());
		//
		assertEquals(l[0], "a");
		assertEquals(l[1], "b");
		assertEquals(l[2], "c");
		assertEquals(l[3], "d");
		assertEquals(l[4], "e");
		//
		qsort.setAscendingSortEnabled(false);
		qsort.quicksort(l, 0, l.length -1);
		//
		assertFalse(qsort.isAscendingSort());
		//
		assertEquals(l[0], "e");
		assertEquals(l[1], "d");
		assertEquals(l[2], "c");
		assertEquals(l[3], "b");
		assertEquals(l[4], "a");
	}
}