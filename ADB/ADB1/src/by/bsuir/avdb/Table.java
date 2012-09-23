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
import android.view.View;
import android.widget.TextView;

public class Table extends ExpandableListActivity {

    public static final String MIN_RATE_EXTRA = "min_rate";
    public static final String MAX_RATE_EXTRA = "max_rate";

    public static final int RANGE_DIALOG_ID = 1000;

    public static final int MENU_RANGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper dbHelper = new DBHelper(this);
        new DBOpenTask().execute(null);
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
            View layout = getLayoutInflater().inflate(R.layout.dialog, null);
            ad.setView(layout);
            final TextView minRate = (TextView) layout.findViewById(R.id.editText1);
            final TextView maxRate = (TextView) layout.findViewById(R.id.editText2);
            ad.setPositiveButton(getString(R.string.draw), new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Table.this, HistogramActivity.class);
                    if (minRate.getText().length() >= 1)
                        intent.putExtra(MIN_RATE_EXTRA, minRate.getText().toString());
                    else {
                        intent.putExtra(MIN_RATE_EXTRA, "0.");
                    }
                    if (maxRate.getText().length() >= 1)
                        intent.putExtra(MAX_RATE_EXTRA, maxRate.getText().toString());
                    else
                        intent.putExtra(MAX_RATE_EXTRA, "100.");
                    startActivity(intent);
                }
            });
            ad.setNegativeButton(getString(R.string.cancel), new OnClickListener() {

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
