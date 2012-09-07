package by.bsuir.avdb;

import by.bsuir.avdb.DBHelper.DBColumns;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;

public class Table extends ListActivity {

	private SQLiteDatabase mDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBHelper dbHelper = new DBHelper(this);
		mDB = dbHelper.getWritableDatabase();
		RateAdapter ra = new RateAdapter(this, mDB.query(DBHelper.TABLE_NAME,
				new String[] {
					BaseColumns._ID,
					DBColumns.BUY_CELL,
					DBColumns.RATE
				}, null, null, null, null, null));
		setListAdapter(ra);
	}
	
	@Override
	protected void onDestroy() {
		mDB.close();
		super.onDestroy();
	}
}
