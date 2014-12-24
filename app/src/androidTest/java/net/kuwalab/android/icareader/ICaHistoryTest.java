package net.kuwalab.android.icareader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ICaHistoryTest {
    @Test
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
        assertThat("年", icaHistory.getDate()[0], is(2001));
        assertThat("月", icaHistory.getDate()[1], is(1));
        assertThat("日", icaHistory.getDate()[2], is(1));
        assertThat("乗車時", icaHistory.getRideTime()[0], is(8));
        assertThat("乗車分", icaHistory.getRideTime()[1], is(0));
        assertThat("降車時", icaHistory.getDropTime()[0], is(20));
        assertThat("乗車分", icaHistory.getDropTime()[1], is(0));
        assertThat("使用金額", icaHistory.getUseMoney(), is(500));
        assertThat("残り金額", icaHistory.getRestMoney(), is(5000));
    }

    @Test
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
        assertThat("年", icaHistory.getDate()[0], is(2000));
        assertThat("月", icaHistory.getDate()[1], is(1));
        assertThat("日", icaHistory.getDate()[2], is(1));
        assertThat("乗車時", icaHistory.getRideTime()[0], is(0));
        assertThat("乗車分", icaHistory.getRideTime()[1], is(10));
        assertThat("降車時", icaHistory.getDropTime()[0], is(0));
        assertThat("乗車分", icaHistory.getDropTime()[1], is(10));
        assertThat("使用金額", icaHistory.getUseMoney(), is(-20480));
        assertThat("残り金額", icaHistory.getRestMoney(), is(0));
    }

    @Test
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
        assertThat("年", icaHistory.getDate()[0], is(2099));
        assertThat("月", icaHistory.getDate()[1], is(12));
        assertThat("日", icaHistory.getDate()[2], is(31));
        assertThat("乗車時", icaHistory.getRideTime()[0], is(23));
        assertThat("乗車分", icaHistory.getRideTime()[1], is(50));
        assertThat("降車時", icaHistory.getDropTime()[0], is(23));
        assertThat("乗車分", icaHistory.getDropTime()[1], is(50));
        assertThat("使用金額", icaHistory.getUseMoney(), is(20470));
        assertThat("残り金額", icaHistory.getRestMoney(), is(65535));
    }
}
