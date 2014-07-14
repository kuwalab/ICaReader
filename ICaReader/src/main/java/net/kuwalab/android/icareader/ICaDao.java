package net.kuwalab.android.icareader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.IOException;
import java.util.List;

/**
 * ICaのDBアクセスオブジェクト
 */
public class ICaDao {
    private ICaDbHelper dbHelper;

    public ICaDao(Context context) {
        dbHelper = new ICaDbHelper(context);
    }

    private static final String ICA_HISTORY_INSERT = "" +
            "INSERT INTO ica_history(" +
            "idm,raw_data,year,month,date,ride_hour,ride_minute," +
            "drop_hour,drop_minute,use_money,rest_money) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

    public int insertICaHistory(String idm, List<ICaHistory> icaHistoryList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            SQLiteStatement stmt = db.compileStatement(ICA_HISTORY_INSERT);
            for (ICaHistory icaHistory : icaHistoryList) {
                stmt.bindString(1, idm);
                stmt.bindString(2, icaHistory.getRawData());
                bindLongArray(stmt, 3, icaHistory.getDate(), 3);
                bindLongArray(stmt, 6, icaHistory.getRideTime(), 2);
                bindLongArray(stmt, 8, icaHistory.getDropTime(), 2);
                stmt.bindLong(10, icaHistory.getUseMoney());
                stmt.bindLong(11, icaHistory.getRestMoney());

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
        for (int i = 0; i < count;  i++) {
            stmt.bindLong(begin + i, values[i]);
        }

    }

    private void bindLongNull(SQLiteStatement stmt, int begin, int count) {
        for (int i = 0; i < count; i++) {
            stmt.bindNull(begin + i);
        }
    }
}
