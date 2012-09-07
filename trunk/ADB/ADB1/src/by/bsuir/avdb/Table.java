package by.bsuir.avdb;

import by.bsuir.avdb.DBHelper.DBColumns;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.CursorTreeAdapter;

public class Table extends ExpandableListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBHelper dbHelper = new DBHelper(this);
		new DBOpenTask().execute(null);
		Log.d("model", Build.MODEL + "  " + Build.VERSION.SDK);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static final int TOKEN_GROUP = 0;
	private static final int TOKEN_CHILD = 1;

	private class DBOpenTask extends AsyncTask<Void, Void, SQLiteDatabase> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(Table.this);
			pd.setTitle(R.string.opening_db);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected SQLiteDatabase doInBackground(Void... params) {
			DBHelper dbHelper = new DBHelper(Table.this);
			return dbHelper.getWritableDatabase();
		}

		@Override
		protected void onPostExecute(SQLiteDatabase result) {
			CursorQueryHandler c = new CursorQueryHandler(Table.this, result);
			RateAdapter ra = new RateAdapter(Table.this, c.getBaseCursor(), c);
			setListAdapter(ra);
			pd.dismiss();
		}
	}
}
