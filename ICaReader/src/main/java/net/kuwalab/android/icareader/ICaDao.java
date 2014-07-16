package net.kuwalab.android.icareader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**ß
 * ICaのDBアクセスオブジェクト
 */
public class ICaDao {
    private ICaDbHelper dbHelper;

    public ICaDao(Context context) {
        dbHelper = new ICaDbHelper(context);
    }

    private static final String ICA_HISTORY_MAX_RAW = "" +
            "SELECT raw_data FROM ica_history " +
            "WHERE idm=? " +
            "AND id=(SELECT MAX(id) FROM ica_history WHERE idm=?)";

    public String getMaxRaw(@NonNull String idm) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ICA_HISTORY_MAX_RAW, new String[]{idm, idm});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        String rawData = cursor.getString(0);
        Log.i("###########", rawData);
        cursor.close();
        db.close();

        return rawData;
    }

    private static final String ICA_HISTORY_INSERT = "" +
            "INSERT INTO ica_history(" +
            "idm,read_date,raw_data,year,month,date,ride_hour,ride_minute," +
            "drop_hour,drop_minute,use_money,rest_money) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

    public int insertICaHistory(String idm, String readDate, List<ICaHistory> icaHistoryList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            SQLiteStatement stmt = db.compileStatement(ICA_HISTORY_INSERT);
            for (ICaHistory icaHistory : icaHistoryList) {
                stmt.bindString(1, idm);
                stmt.bindString(2, readDate);
                stmt.bindString(3, icaHistory.getRawData());
                bindLongArray(stmt, 4, icaHistory.getDate(), 3);
                bindLongArray(stmt, 7, icaHistory.getRideTime(), 2);
                bindLongArray(stmt, 8, icaHistory.getDropTime(), 2);
                stmt.bindLong(11, icaHistory.getUseMoney());
                stmt.bindLong(12, icaHistory.getRestMoney());

                stmt.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();

        return 1;
    }

    private void bindLongArray(SQLiteStatement stmt, int begin, int[] values, int count) {
        if (values == null) {
            bindLongNull(stmt, begin, count);
            return;
        }
        for (int i = 0; i < count; i++) {
            stmt.bindLong(begin + i, values[i]);
        }
    }

    private void bindLongNull(SQLiteStatement stmt, int begin, int count) {
        for (int i = 0; i < count; i++) {
            stmt.bindNull(begin + i);
        }
    }
}
