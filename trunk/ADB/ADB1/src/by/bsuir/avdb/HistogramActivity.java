package by.bsuir.avdb;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class HistogramActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CursorQueryHandler cqh = new CursorQueryHandler(this, new DBHelper(this).getReadableDatabase());
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cqh.getWithRates("1.0", "10.0"), null, null);
		setListAdapter(sca);
	}
}
