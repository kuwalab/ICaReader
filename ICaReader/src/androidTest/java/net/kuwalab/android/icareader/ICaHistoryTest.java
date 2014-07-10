package net.kuwalab.android.icareader;

import junit.framework.TestCase;

public class ICaHistoryTest extends TestCase {
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
        bytes[11] = 0;
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

    public void test下限値() {
        byte[] bytes = new byte[16];
        // 2000/1/1
        bytes[0] = 0x00;
        bytes[1] = 0x21;
        // 00:00
        bytes[5] = 0x01;
        // 00:00
        bytes[2] = 0x01;
        // -20480円
        bytes[11] = 0x08;
        bytes[12] = 0x00;
        // 0円
        bytes[13] = 0x00;
        bytes[14] = (byte) 0x00;

        ICaHistory icaHistory = new ICaHistory(bytes);
        assertEquals("年", icaHistory.getDate()[0], 2000);
        assertEquals("月", icaHistory.getDate()[1], 1);
        assertEquals("日", icaHistory.getDate()[2], 1);
        assertEquals("乗車時", icaHistory.getRideTime()[0], 0);
        assertEquals("乗車分", icaHistory.getRideTime()[1], 10);
        assertEquals("降車時", icaHistory.getDropTime()[0], 0);
        assertEquals("乗車分", icaHistory.getDropTime()[1], 10);
        assertEquals("使用金額", icaHistory.getUseMoney(), -20480);
        assertEquals("残り金額", icaHistory.getRestMoney(), 0);
    }

    public void test上限値() {
        byte[] bytes = new byte[16];
        // 2099/12/31
        bytes[0] = (byte) 0xc7;
        bytes[1] = (byte) 0x9f;
        // 23:50
        bytes[5] = (byte) 0x8f;
        // 23:50
        bytes[2] = (byte) 0x8f;
        // 20470円
        bytes[11] = 0x07;
        bytes[12] = (byte) 0xff;
        // 655350円
        bytes[13] = (byte) 0xff;
        bytes[14] = (byte) 0xff;

        ICaHistory icaHistory = new ICaHistory(bytes);
        assertEquals("年", icaHistory.getDate()[0], 2099);
        assertEquals("月", icaHistory.getDate()[1], 12);
        assertEquals("日", icaHistory.getDate()[2], 31);
        assertEquals("乗車時", icaHistory.getRideTime()[0], 23);
        assertEquals("乗車分", icaHistory.getRideTime()[1], 50);
        assertEquals("降車時", icaHistory.getDropTime()[0], 23);
        assertEquals("乗車分", icaHistory.getDropTime()[1], 50);
        assertEquals("使用金額", icaHistory.getUseMoney(), 20470);
        assertEquals("残り金額", icaHistory.getRestMoney(), 65535);
    }
}
