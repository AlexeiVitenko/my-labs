package by.bsuir.gmailoauth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sender_db";
    public static final String ADDRES_TABLE = "addres_table";
    private static final int VERSION = 1;
    private static DBHelper sHelper;

    private SQLiteDatabase mDatabase;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mDatabase = getWritableDatabase();
    }

    public static DBHelper getInstance(Context c) {
        if (sHelper == null) {
            sHelper = new DBHelper(c);
        }
        return sHelper;
    }

    public void remove(String mail, String text) {
        mDatabase.delete(ADDRES_TABLE, DBColumns.MAIL + " = '" + mail + "' AND " + DBColumns.TEXT + " = '" + text+"'", null);
    }

    public void insert(String mail, String text) {

        getWritableDatabase().insert(ADDRES_TABLE, "", getContentValues(mail, text));
    }

    private ContentValues getContentValues(String mail, String text) {
        ContentValues cv = new ContentValues();
        cv.put(DBColumns.MAIL, mail);
        cv.put(DBColumns.TEXT, text);
        return cv;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
    }

    private void createDB(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + ADDRES_TABLE + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBColumns.MAIL + " TEXT, " + DBColumns.TEXT + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP VIEW IF EXISTS " + ADDRES_TABLE);
            createDB(db);
        }
    }

    public interface DBColumns {
        String MAIL = "mail";
        String TEXT = "text";
    }

    public void update(long itemId, String string, String string2) {
        getWritableDatabase().update(ADDRES_TABLE, getContentValues(string, string2), BaseColumns._ID + " = " + itemId,
                null);
    }

    public Cursor getAll() {
        return getReadableDatabase().rawQuery("select * from "+ADDRES_TABLE, null);
    }

}
