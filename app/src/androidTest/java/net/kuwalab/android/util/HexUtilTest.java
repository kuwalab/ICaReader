package net.kuwalab.android.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class HexUtilTest {
    private static final byte x0_b0000_0000 = 0;
    private static final byte x1_b0000_0001 = 1;
    private static final byte x15_b0000_1111 = 15;
    private static final byte x16_b0001_0000 = 16;
    private static final byte xminus1_1111_1111 = -1;
    private static final byte xminus128_b1000_0000 = -128;

    private void assert16進数文字列チェック(byte[] input, String expected) {
        StringBuilder sb = new StringBuilder();
        sb.append("toHexString(");
        for (byte b : input) {
            sb.append(b).append(" ");
        }
        sb.append(")=").append(expected);
        assertThat(sb.toString(), HexUtil.toHexString(input), is(expected));
    }

    private void assert16進数文字列チェック(byte[] input, String[] expected) {
        StringBuilder sb = new StringBuilder();
        sb.append("toHexString(");
        for (byte b : input) {
            sb.append(b).append(" ");
        }
        sb.append(")=");
        for (String str : expected) {
            sb.append(str).append(" ");
        }

        String[] actual = HexUtil.toHexStringArray(input);
        for (int i = 0; i < actual.length; i++) {
            assertThat(sb.toString(), actual[i], is(expected[i]));
        }
    }

    private void assertIntチェック(byte[] input, int expected) {
        StringBuilder sb = new StringBuilder();
        sb.append("toInt(");
        for (byte b : input) {
            sb.append(b).append(" ");
        }
        sb.append(")=").append(expected);
        assertThat(sb.toString(), HexUtil.toInt(input), is(expected));
    }

    @Test
    public void testToHexString() {
        assertThat(HexUtil.toHexString(x0_b0000_0000), is("00"));
        assertThat(HexUtil.toHexString(x1_b0000_0001), is("01"));
        assertThat(HexUtil.toHexString(x15_b0000_1111), is("0f"));
        assertThat(HexUtil.toHexString(x16_b0001_0000), is("10"));
        assertThat(HexUtil.toHexString(xminus128_b1000_0000), is("80"));
        assertThat(HexUtil.toHexString(xminus1_1111_1111), is("ff"));
    }

    @Test
    public void testToHexString2桁() {
        assert16進数文字列チェック(new byte[]{x0_b0000_0000}, "00");
        assert16進数文字列チェック(new byte[]{x1_b0000_0001}, "01");
        assert16進数文字列チェック(new byte[]{x15_b0000_1111}, "0f");
        assert16進数文字列チェック(new byte[]{x16_b0001_0000}, "10");
        assert16進数文字列チェック(new byte[]{xminus128_b1000_0000}, "80");
        assert16進数文字列チェック(new byte[]{xminus1_1111_1111}, "ff");
    }

    @Test
    public void testToHexString4桁() {
        assert16進数文字列チェック(new byte[]{x0_b0000_0000, x1_b0000_0001}, "0001");
        assert16進数文字列チェック(new byte[]{x15_b0000_1111, x16_b0001_0000}, "0f10");
        assert16進数文字列チェック(new byte[]{x0_b0000_0000, xminus1_1111_1111},
                "00ff");
        assert16進数文字列チェック(new byte[]{xminus128_b1000_0000, x16_b0001_0000},
                "8010");
    }

    @Test
    public void testToHexStringArray2桁() {
        assert16進数文字列チェック(new byte[]{x0_b0000_0000}, new String[]{"00"});
        assert16進数文字列チェック(new byte[]{x1_b0000_0001}, new String[]{"01"});
        assert16進数文字列チェック(new byte[]{x15_b0000_1111}, new String[]{"0f"});
        assert16進数文字列チェック(new byte[]{x16_b0001_0000}, new String[]{"10"});
        assert16進数文字列チェック(new byte[]{xminus128_b1000_0000},
                new String[]{"80"});
        assert16進数文字列チェック(new byte[]{xminus1_1111_1111},
                new String[]{"ff"});
    }

    @Test
    public void testToHexStringArray4桁() {
        assert16進数文字列チェック(new byte[]{x0_b0000_0000, x1_b0000_0001},
                new String[]{"00", "01"});
        assert16進数文字列チェック(new byte[]{x15_b0000_1111, x16_b0001_0000},
                new String[]{"0f", "10"});
        assert16進数文字列チェック(new byte[]{x0_b0000_0000, xminus1_1111_1111},
                new String[]{"00", "ff"});
        assert16進数文字列チェック(new byte[]{xminus128_b1000_0000, x16_b0001_0000},
                new String[]{"80", "10"});
    }

    @Test
    public void testToInt() {
        assertThat(HexUtil.toInt(x0_b0000_0000), is(0));
        assertThat(HexUtil.toInt(x1_b0000_0001), is(1));
        assertThat(HexUtil.toInt(x15_b0000_1111), is(15));
        assertThat(HexUtil.toInt(x16_b0001_0000), is(16));
        assertThat(HexUtil.toInt(xminus128_b1000_0000), is(128));
        assertThat(HexUtil.toInt(xminus1_1111_1111), is(255));
    }

    @Test
    public void testToIntArray1桁() {
        assertIntチェック(new byte[]{x0_b0000_0000}, 0);
        assertIntチェック(new byte[]{x1_b0000_0001}, 1);
        assertIntチェック(new byte[]{x15_b0000_1111}, 15);
        assertIntチェック(new byte[]{x16_b0001_0000}, 16);
        assertIntチェック(new byte[]{xminus128_b1000_0000}, 128);
        assertIntチェック(new byte[]{xminus1_1111_1111}, 255);
    }

    @Test
    public void testToIntArray2桁() {
        assertIntチェック(new byte[]{x0_b0000_0000, x1_b0000_0001}, 1);
        assertIntチェック(new byte[]{x1_b0000_0001, x1_b0000_0001}, 257);
        assertIntチェック(new byte[]{x1_b0000_0001, xminus128_b1000_0000}, 384);
        assertIntチェック(new byte[]{xminus128_b1000_0000, x1_b0000_0001},
                (int) Math.pow(2, 15) + 1);
    }
}
