package by.bsuir.gmailoauth.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sender_db";
    public static final String ADDRES_TABLE = "addres_table";
    private static final int VERSION = 3;
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

    public void remove(long id) {
        mDatabase.delete(ADDRES_TABLE,
                BaseColumns._ID + " = '" + id + "'", null);
    }

    public void insert(String mail, String text, String subj) {

        getWritableDatabase().insert(ADDRES_TABLE, "", getContentValues(mail, text, subj));
    }

    private ContentValues getContentValues(String mail, String text, String subj) {
        ContentValues cv = new ContentValues();
        cv.put(DBColumns.MAIL, mail);
        cv.put(DBColumns.TEXT, text);
        cv.put(DBColumns.SUBJECT, subj);
        return cv;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDB(db);
    }

    private void createDB(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + ADDRES_TABLE + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBColumns.MAIL + " TEXT, " + DBColumns.SUBJECT + " TEXT, " + DBColumns.TEXT + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (oldVersion == 1) {
                //db.setVersion(newVersion);
                Cursor c = getAll(db);
                if (c.moveToFirst()) {
                    List<ContentValues> cvs = new ArrayList<ContentValues>();
                    int mailIndex = c.getColumnIndex(DBColumns.MAIL);
                    int textIndex = c.getColumnIndex(DBColumns.TEXT);
                    do {
                        ContentValues co = new ContentValues();
                        co.put(DBColumns.MAIL, c.getString(mailIndex));
                        co.put(DBColumns.TEXT, c.getString(textIndex));
                        cvs.add(co);
                    } while (c.moveToNext());
                    db.execSQL("DROP TABLE IF EXISTS " + ADDRES_TABLE);
                    createDB(db);
                    for (ContentValues contentValues : cvs) {
                        db.insert(ADDRES_TABLE, "", contentValues);
                    }
                }
            } else {

                db.execSQL("DROP TABLE IF EXISTS " + ADDRES_TABLE);
                createDB(db);
            }
        }
    }

    public interface DBColumns {
        String MAIL = "mail";
        String TEXT = "text";
        String SUBJECT = "subject";
    }

    public void update(long itemId, String string, String string2, String subj) {
        getWritableDatabase().update(ADDRES_TABLE, getContentValues(string, string2, subj), BaseColumns._ID + " = " + itemId,
                null);
    }

    public Cursor getAll() {
        return getAll(getReadableDatabase());
    }
    
    private Cursor getAll(SQLiteDatabase db){
        return db.rawQuery("select * from " + ADDRES_TABLE, null);
    }

}
