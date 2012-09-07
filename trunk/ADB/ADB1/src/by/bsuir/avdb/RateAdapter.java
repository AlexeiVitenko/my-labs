package by.bsuir.avdb;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RateAdapter extends CursorAdapter {

	public RateAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		TextView tv = (TextView)arg0;
		long id = arg2.getLong(arg2.getColumnIndex(BaseColumns._ID));
		boolean buy = arg2.getLong(arg2.getColumnIndex(DBHelper.DBColumns.BUY_CELL)) == 1? true : false;
		double rate = arg2.getDouble(arg2.getColumnIndex(DBHelper.DBColumns.RATE));
		tv.setText("ID: " + id + ", RATE: " + rate + ", BUY: " + buy);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		TextView tv = new TextView(arg0);
		tv.setSingleLine();
		return tv;
	}
}
