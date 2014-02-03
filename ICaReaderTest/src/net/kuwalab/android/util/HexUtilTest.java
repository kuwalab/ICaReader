package net.kuwalab.android.util;

import android.test.AndroidTestCase;

public class HexUtilTest extends AndroidTestCase {
	private static final byte x0_b0000_0000 = 0;
	private static final byte x1_b0000_0001 = 1;
	private static final byte x15_b0000_1111 = 15;
	private static final byte x16_b0001_0000 = 16;
	private static final byte xminus1_1111_1111 = -1;
	private static final byte xminus128_b0001_0000 = -128;

	private void assert16進数文字列チェック(byte[] input, String expected) {
		StringBuilder sb = new StringBuilder();
		sb.append("toHexString(");
		for (byte b : input) {
			sb.append(b).append(" ");
		}
		sb.append(")=").append(expected);
		assertEquals(sb.toString(), expected, HexUtil.toHexString(input));
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
			assertEquals(sb.toString(), expected[i], actual[i]);
		}
	}

	public void testToHexString2桁() {
		assert16進数文字列チェック(new byte[] { x0_b0000_0000 }, "00");
		assert16進数文字列チェック(new byte[] { x1_b0000_0001 }, "01");
		assert16進数文字列チェック(new byte[] { x15_b0000_1111 }, "0f");
		assert16進数文字列チェック(new byte[] { x16_b0001_0000 }, "10");
		assert16進数文字列チェック(new byte[] { xminus128_b0001_0000 }, "80");
		assert16進数文字列チェック(new byte[] { xminus1_1111_1111 }, "ff");
	}

	public void testToHexString4桁() {
		assert16進数文字列チェック(new byte[] { x0_b0000_0000, x1_b0000_0001 }, "0001");
		assert16進数文字列チェック(new byte[] { x15_b0000_1111, x16_b0001_0000 }, "0f10");
		assert16進数文字列チェック(new byte[] { x0_b0000_0000, xminus1_1111_1111 },
				"00ff");
		assert16進数文字列チェック(new byte[] { xminus128_b0001_0000, x16_b0001_0000 },
				"8010");
	}

	public void testToHexStringArray2桁() {
		assert16進数文字列チェック(new byte[] { x0_b0000_0000 }, new String[] { "00" });
		assert16進数文字列チェック(new byte[] { x1_b0000_0001 }, new String[] { "01" });
		assert16進数文字列チェック(new byte[] { x15_b0000_1111 }, new String[] { "0f" });
		assert16進数文字列チェック(new byte[] { x16_b0001_0000 }, new String[] { "10" });
		assert16進数文字列チェック(new byte[] { xminus128_b0001_0000 },
				new String[] { "80" });
		assert16進数文字列チェック(new byte[] { xminus1_1111_1111 },
				new String[] { "ff" });
	}

	public void testToHexStringArray4桁() {
		assert16進数文字列チェック(new byte[] { x0_b0000_0000, x1_b0000_0001 },
				new String[] { "00", "01" });
		assert16進数文字列チェック(new byte[] { x15_b0000_1111, x16_b0001_0000 },
				new String[] { "0f", "10" });
		assert16進数文字列チェック(new byte[] { x0_b0000_0000, xminus1_1111_1111 },
				new String[] { "00", "ff" });
		assert16進数文字列チェック(new byte[] { xminus128_b0001_0000, x16_b0001_0000 },
				new String[] { "80", "10" });
	}
}
