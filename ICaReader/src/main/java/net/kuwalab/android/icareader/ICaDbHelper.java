package net.kuwalab.android.icareader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ICaの履歴を保管するDB。
 */
public class ICaDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="ica_db";
    private static final int DB_VERSION = 1;

    public ICaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String CREATE_TABLE_ICA_HISTORY = "" +
            "CREATE TABLE ica_history(" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  idm TEXT NOT NULL," +
            "  raw_data TEXT NOT NULL," +
            "  year INTEGER," +
            "  month INTEGER," +
            "  date INTEGER," +
            "  ride_hour INTEGER," +
            "  ride_minute INTEGER," +
            "  drop_hour INTEGER," +
            "  drop_minute INTEGER," +
            "  use_money INTEGER," +
            "  rest_money INTEGER" +
            ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ICA_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 最初のバージョンのため何もしない
    }
}
