package by.bsuir.avdb;

import org.w3c.dom.Text;

import by.bsuir.avdb.DBHelper.DBColumns;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

public class RateAdapter extends CursorTreeAdapter {

	private CursorQueryHandler mQueryHandler;
	
	public RateAdapter(Context context, Cursor c, CursorQueryHandler handler) {
		super(c, context);
		mQueryHandler = handler;
		Log.d("cursor", "" + c.getCount());
	}

	/*
	 * @Override public void bindView(View arg0, Context arg1, Cursor arg2) { TextView tv = (TextView)arg0; long id =
	 * arg2.getLong(arg2.getColumnIndex(BaseColumns._ID)); boolean buy =
	 * arg2.getLong(arg2.getColumnIndex(DBHelper.DBColumns.BUY_CELL)) == 1? true : false; double rate =
	 * arg2.getDouble(arg2.getColumnIndex(DBHelper.DBColumns.RATE)); tv.setText( "ID: " + id + ", RATE: " +
	 * String.format("%.2f", rate) + ", BUY: " + buy); }
	 * 
	 * @Override public View newView(Context arg0, Cursor arg1, ViewGroup arg2) { TextView tv = new TextView(arg0);
	 * tv.setSingleLine(); tv.setPadding(10, 10, 10, 10); return tv; }
	 */
	@Override
	protected void finalize() throws Throwable {
		getCursor().close();
		super.finalize();
	}
	
	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		return mQueryHandler.getChildGroup(this, groupCursor.getPosition(), groupCursor.getString(groupCursor.getColumnIndex(DBColumns.RATE)));
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
		TextView tv = new TextView(context);
		tv.setSingleLine();
		tv.setPadding(100, 10, 10, 10);
		return tv;
	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
		((TextView)view).setText("" + cursor.getString(cursor.getColumnIndex(DBColumns.RATE)));
	}

	@Override
	protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
		TextView tv = new TextView(context);
		tv.setSingleLine();
		tv.setPadding(10, 10, 10, 10);
		return tv;
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
		TextView tv = (TextView) view;
		Log.d("cursor", "!" + cursor.getCount());
	//	long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
		boolean buy = cursor.getLong(cursor.getColumnIndex(DBHelper.DBColumns.BUY_CELL)) == 1 ? true : false;
		tv.setText("RATE: " + cursor.getString(cursor.getColumnIndex(DBHelper.DBColumns.RATE)) + ", BUY: " + buy);
	}
}
