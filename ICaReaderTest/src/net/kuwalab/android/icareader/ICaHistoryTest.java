package net.kuwalab.android.icareader;

import android.test.AndroidTestCase;

public class ICaHistoryTest extends AndroidTestCase {
	public void test通常の値() {
		byte[] bytes = new byte[16];
		// 2001/1/1
		bytes[0] = 0x02;
		bytes[1] = 0x21;
		// 8:00
		bytes[5] = 0x30;
		// 20:00
		bytes[2] = 0x78;
		// 500円
		bytes[12] = 0x32;
		// 5000円
		bytes[13] = 0x13;
		bytes[14] = (byte) 0x88;

		ICaHistory icaHistory = new ICaHistory(bytes);
		assertEquals("年", icaHistory.getDate()[0], 2001);
		assertEquals("月", icaHistory.getDate()[1], 1);
		assertEquals("日", icaHistory.getDate()[2], 1);
		assertEquals("乗車時", icaHistory.getRideTime()[0], 8);
		assertEquals("乗車分", icaHistory.getRideTime()[1], 0);
		assertEquals("降車時", icaHistory.getDropTime()[0], 20);
		assertEquals("乗車分", icaHistory.getDropTime()[1], 0);
		assertEquals("使用金額", icaHistory.getUseMoney(), 500);
		assertEquals("残り金額", icaHistory.getRestMoney(), 5000);
	}
}
