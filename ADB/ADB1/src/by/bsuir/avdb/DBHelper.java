package by.bsuir.avdb;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static float[][] RATES = { { 2f, 0.1f }, { 4f, 0.2f }, { 10f, 0.5f }, { 20f, 1f }, { 100f, 5f } };
	private static int VERSION = 18;
	private static final String DB_NAME = "rates_db";
	static final String TABLE_NAME = "rates_name";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {
		Log.d("create table", "table created");
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBColumns.RATE + " TEXT, " + DBColumns.BUY_CELL + " INTEGER);";
		db.execSQL(sql);
		fillDatabase(db);
	}

	private void fillDatabase(SQLiteDatabase db) {
		if (RATES.length > 0) {
			for (int i = 0; i < RATES.length; i++) {
				BigDecimal border = new BigDecimal(String.valueOf(RATES[i][0]));
				BigDecimal bd;
				if (i == 0) {
					bd = new BigDecimal(0);
				} else
					bd = new BigDecimal(RATES[i - 1][0]);
				BigDecimal delta = new BigDecimal(String.valueOf(RATES[i][1]));
				Random r = new Random();
				while (bd.compareTo(border) < 1) {
					int c = r.nextInt(100);
					Log.d("count",""+ c);
					for (int j = 0; j < c; j++) {
						ContentValues cv = new ContentValues();
						cv.put(DBColumns.RATE, bd.toString());
						cv.put(DBColumns.BUY_CELL, r.nextInt(2));
						db.insert(TABLE_NAME, null, cv);
					}
					bd = bd.add((delta));
				}
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion) {
			return;
		}
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		createTables(db);
	}

	static interface DBColumns {
		static final String RATE = "rate";
		static final String BUY_CELL = "buy_cell";
		
	}
}
