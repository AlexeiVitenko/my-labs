package by.bsuir.avdb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Table extends ExpandableListActivity {

	public static final int RANGE_DIALOG_ID = 1000;

	public static final int MENU_RANGE = 100;

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

	class DBOpenTask extends AsyncTask<Void, Void, SQLiteDatabase> {

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

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case RANGE_DIALOG_ID:
			final AlertDialog.Builder ad = new AlertDialog.Builder(this);
			ad.setTitle(R.string.choose_range);
			ad.setView(getLayoutInflater().inflate(R.layout.dialog, null));
			ad.setPositiveButton(getString(R.string.draw),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(Table.this,
									HistogramActivity.class));
						}
					});
			ad.setNegativeButton(getString(R.string.cancel),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			return ad.create();
		default:
			return super.onCreateDialog(id, args);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_RANGE, 0, R.string.draw_histogrm);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_RANGE:
			showDialog(RANGE_DIALOG_ID);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
