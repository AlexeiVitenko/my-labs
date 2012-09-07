package by.bsuir.avdb;

import by.bsuir.avdb.DBHelper.DBColumns;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.widget.CursorTreeAdapter;

public class CursorQueryHandler extends Handler {
	
	private Thread mWorkerThread = new Thread();
	private Context mContext;
	private SQLiteDatabase mDatabase;
	
	public CursorQueryHandler(Context context, SQLiteDatabase database){
		mContext = context;
		mDatabase = database;
	}
	
	public Cursor getBaseCursor(){
		return mDatabase.query(DBHelper.TABLE_NAME, new String[] {DBColumns.RATE, BaseColumns._ID },
				null, null, DBColumns.RATE, null, null, null);
	}
	
	public Cursor getChildGroup(RateAdapter adapter, int position, float value){
		return 	mDatabase.query(DBHelper.TABLE_NAME, new String[] { BaseColumns._ID, DBColumns.BUY_CELL, DBColumns.RATE },
				DBColumns.RATE +" = "+ value, null, null, null, null);
	}
	
	private void updateChildGroup(final Cursor cursor, final CursorTreeAdapter adapter, final int groupPosition){
		post(new Runnable() {
			
			@Override
			public void run() {
				adapter.setChildrenCursor(groupPosition, cursor);
			}
		});
	}
}
