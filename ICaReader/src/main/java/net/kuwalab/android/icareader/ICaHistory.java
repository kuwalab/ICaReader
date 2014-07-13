package net.kuwalab.android.icareader;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import net.kuwalab.android.util.HexUtil;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * ICaの乗車履歴<br>
 * 結構データが欠落している事がある。
 *
 * @author kuwalab
 */
public class ICaHistory implements Parcelable {
    /**
     * 乗車日付
     */
    private int[] date;
    /**
     * 乗車時刻
     */
    private int[] rideTime;
    /**
     * 降車時刻
     */
    private int[] dropTime;
    /**
     * 使用金額
     */
    private int useMoney;
    /**
     * 残額
     */
    private int restMoney;

    public static final boolean USE = true;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(date);
        dest.writeIntArray(rideTime);
        dest.writeIntArray(dropTime);
        dest.writeInt(useMoney);
        dest.writeInt(restMoney);
    }

    public static final Parcelable.Creator<ICaHistory> CREATOR = new Parcelable.Creator<ICaHistory>() {
        @Override
        public ICaHistory createFromParcel(Parcel source) {
            return new ICaHistory(source);
        }

        @Override
        public ICaHistory[] newArray(int size) {
            return new ICaHistory[size];
        }
    };

    private ICaHistory(Parcel in) {
        in.readIntArray(date);
        in.readIntArray(rideTime);
        in.readIntArray(dropTime);
        useMoney = in.readInt();
        restMoney = in.readInt();
    }

    /**
     * 乗車日付の取得。<br>
     * データが怪しい場合はnull。
     *
     * @return 乗車日付
     */
    public int[] getDate() {
        return date;
    }

    /**
     * 乗車時刻の取得。<br>
     * データが怪しい場合はnull
     *
     * @return 乗車時刻
     */
    public int[] getRideTime() {
        return rideTime;
    }

    /**
     * 降車時刻の取得<br>
     * データが怪しい場合はnull
     *
     * @return 降車時刻
     */
    public int[] getDropTime() {
        return dropTime;
    }

    /**
     * 運賃もしくは、積み増し金額の取得
     *
     * @return 運賃もしくは積み増し金額
     */
    public int getUseMoney() {
        return useMoney;
    }

    /**
     * 残金の取得
     *
     * @return 残金
     */
    public int getRestMoney() {
        return restMoney;
    }

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

    public ICaHistory(@NonNull byte[] historyData) {
        date = analyzeDate(Arrays.copyOfRange(historyData, 0, 2));
        rideTime = analyzeTime(historyData[5]);
        dropTime = analyzeTime(historyData[2]);
        useMoney = analyzeUseMoney(Arrays.copyOfRange(historyData, 11, 13));
        restMoney = HexUtil.toInt(Arrays.copyOfRange(historyData, 13, 15));
    }

    private int[] analyzeDate(@NonNull byte[] bytes) {
        int year = ((bytes[0] >>> 1) & 0x7f) + 2000;
        int month = 0;
        if ((bytes[0] & 0x01) == 1) {
            month = month + 8;
        }
        month = month + ((bytes[1] >>> 5) & 0x07);
        int day = (bytes[1] & 0x1F);

        return new int[]{year, month, day};
    }

    private int[] analyzeTime(byte timeByte) {
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

        return new int[]{hour, minute};
    }

    private int analyzeUseMoney(@NonNull byte[] bytes) {
        int use = 0;
        byte high = (byte) (bytes[0] & 0x0f);
        // 5bit目が1ならマイナス
        boolean isMinus = (high >= (byte) 0x08);
        use = use | (high << 8);
        int row = bytes[1] & 0x00ff;
        use = use | row;
        use = use & 0x0fff;
        // マイナスの場合上位ビットを1にする
        if (isMinus) {
            use = use | 0xfffff000;
        }

        return use * 10;
    }
}
