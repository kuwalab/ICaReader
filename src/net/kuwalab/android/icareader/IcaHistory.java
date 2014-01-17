package net.kuwalab.android.icareader;

import java.text.DecimalFormat;
import java.util.Arrays;

import net.kuwalab.android.util.HexUtil;

public class IcaHistory {
	/** 乗車日付 */
	public int[] date;
	/** 乗車時刻 */
	public int[] beginTime;
	/** 降車時刻 */
	public int[] endTime;
	/** 使用金額 */
	public int useMoney;
	/** 残額 */
	public int restMoney;

	public static final boolean USE = true;

	public boolean isUse() {
		return useMoney < 0;
	}

	public String getDispUseMoney() {
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		return decimalFormat.format(-useMoney);
	}

	public String getDispAddMoney() {
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		return decimalFormat.format(useMoney);
	}

	public String getDispRestMoney() {
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		return decimalFormat.format(restMoney);
	}

	public IcaHistory(byte[] historyData) {
		date = getDate(Arrays.copyOfRange(historyData, 0, 2));
		beginTime = getTime(historyData[5]);
		endTime = getTime(historyData[2]);
		useMoney = getUseMoney(Arrays.copyOfRange(historyData, 11, 13));
		restMoney = HexUtil.toInt(Arrays.copyOfRange(historyData, 13, 15));
	}

	private int[] getDate(byte[] bytes) {
		int year = (bytes[0] >>> 1) + 2000;
		int month = 0;
		if ((bytes[0] & 0x01) == 1) {
			month = month + 8;
		}
		month = month + ((bytes[1] >>> 5) & 0x07);
		int day = (bytes[1] & 0x1F);

		return new int[] { year, month, day };
	}

	private int[] getTime(byte timeByte) {
		if (timeByte == 0) {
			return null;
		}

		int time = timeByte;
		if (time < 0) {
			time = time + 256;
		}
		time = time * 10;

		int hour = time / 60;
		int minute = time % 60;

		return new int[] { hour, minute };
	}

	private int getUseMoney(byte[] bytes) {
		int use = 0;
		use = use | (bytes[0] << 8);
		use = use | bytes[1];
		if (use >= 0) {
			use = use & 0x0fff;
		}

		return use * 10;
	}
}
