package by.bsuir.avdb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import by.bsuir.avdb.DBHelper.DBColumns;

import static by.bsuir.avdb.DBHelper.DBColumns.*;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.CursorTreeAdapter;

public class CursorQueryHandler {// extends Handler {

    private Thread mWorkerThread = new Thread();
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public CursorQueryHandler(Context context, SQLiteDatabase database) {
        mContext = context;
        mDatabase = database;
    }

    public Cursor getBaseCursor() {
        return mDatabase.query(DBHelper.TABLE_NAME, new String[] { RATE, BaseColumns._ID }, null, null, "CAST(" + RATE
                + " AS REAL)", null, null, null);
    }

    public Cursor getChildGroup(RateAdapter adapter, int position, String value) {
        Log.d("value", value);
        return mDatabase.query(DBHelper.TABLE_NAME, new String[] { BaseColumns._ID, BUY_SELL, RATE, DBColumns.COUNT}, RATE + " = "
                + value, null, BUY_SELL, null, null);
    }

    public Cursor getWithRates(String minRate, String maxRate) {
        return mDatabase.query(DBHelper.TABLE_NAME, new String[] { RATE, BaseColumns._ID, BUY_SELL, DBColumns.COUNT },
                "CAST(" + RATE + " AS REAL) >= " + minRate + " AND CAST(" + RATE + " AS REAL) <= " + maxRate, null,
                "CAST(" + RATE + " AS REAL), " + BUY_SELL, null, null, null);
    }

    public List<Operation> getOperationsList(Cursor source) {
        List<Operation> operations = new ArrayList<Operation>();
        int rateIndex = source.getColumnIndex(RATE);
        int buyIndex = source.getColumnIndex(BUY_SELL);
        int countIndex = source.getColumnIndex(COUNT);

        if (source.moveToFirst()) {
            do {
                operations.add(new Operation(new BigDecimal(source.getString(rateIndex)), source.getString(buyIndex)
                        .contains("1") ? true : false, source.getInt(countIndex)));

            } while (source.moveToNext());
        }

        return operations;
    }
}
