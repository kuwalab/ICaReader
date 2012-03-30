package net.kuwalab.android.util;


public class HexUtil {
	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < bytes.length; i++) {
			sb.append(toHexString(bytes[i]));
		}

		return sb.toString();
	}

	public static String[] toHexStringArray(byte[] bytes) {
		String[] result = new String[bytes.length];

		for (int i = 0; i < bytes.length; i++) {
			result[i] = toHexString(bytes[i]);
		}

		return result;
	}

	public static String toHexString(byte oneByte) {
		StringBuilder sb = new StringBuilder();

		String hex = Integer.toHexString(oneByte);
		if (hex.length() == 1) {
			sb.append("0");
		} else if (hex.length() > 2) {
			hex = hex.substring(hex.length() - 2, hex.length());
		}
		sb.append(hex);

		return sb.toString();
	}

	public static int toInt(byte[] bytes) {
		int result = 0;
		int base = 1;

		for (int i = bytes.length - 1; i >= 0; i--) {
			result = result + (toInt(bytes[i]) * base);
			base = base * 256;
		}

		return result;
	}

	public static int toInt(byte b) {
		int result = b;

		if (result < 0) {
			result = result + 256;
		}

		return result;
	}
}
