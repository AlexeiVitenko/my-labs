package by.bsuir.gmailoauth;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import by.bsuir.gmailoauth.data.DBHelper;
import by.bsuir.gmailoauth.data.DBHelper.DBColumns;

public class MailsAdapter extends CursorAdapter{
    private DBHelper mHelper;
    private OnClickListener mListener;
    public MailsAdapter(Context context, DBHelper helper) {
        super(context, helper.getReadableDatabase().rawQuery("select * from " + DBHelper.ADDRES_TABLE, null));
        mHelper = helper;
    }

    public void setOnItemClickListener(OnClickListener listener){
        mListener = listener;
    }
       
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        final String mail = cursor.getString(cursor.getColumnIndex(DBColumns.MAIL));
        final String text = cursor.getString(cursor.getColumnIndex(DBColumns.TEXT));
        vh.text.setText(mail);
        vh.remove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                remove(mail, text);
            }
        });
        MainActivity main = ((MainActivity)context);
        final Bundle b = new Bundle();
        b.putLong("id", cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
        b.putString("mail", cursor.getString(cursor.getColumnIndex(DBColumns.MAIL)));
        b.putString("text", cursor.getString(cursor.getColumnIndex(DBColumns.TEXT)));
        view.setOnClickListener(main.new MyClickListener(b));
    }

    private void remove(String mail, String text) {
        mHelper.remove(mail, text);
        getCursor().requery();
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sender_item, null);
        view.setTag(new ViewHolder((TextView) view.findViewById(R.id.si_mail), (Button) view
                .findViewById(R.id.si_remove)));
        return view;
    }

    private static class ViewHolder {
        TextView text;
        Button remove;

        public ViewHolder(TextView text, Button remove) {
            super();
            this.text = text;
            this.remove = remove;
        }
    }

}
