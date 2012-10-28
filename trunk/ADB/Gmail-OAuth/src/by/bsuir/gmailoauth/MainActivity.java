package by.bsuir.gmailoauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import by.bsuir.gmailoauth.data.DBHelper;
import by.bsuir.gmailoauth.mail.LocalEmailService;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int NEW_FRIEND = 1400;
    private static final int EDIT_FRIEND = 1401;
    private SharedPreferences prefs;
    private TextView oauthResult;
    private EditText oauthEmail;
    private Button sendEmail;
    private DBHelper mHelper;
    private ListView mRec;
    private MailsAdapter mAdapter;
    private Button configureOAuth;
    private BroadcastReceiver mInternetStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                checkInternetState();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = UserData.getPrefs(this);
        setContentView(R.layout.main);
        oauthEmail = (EditText) findViewById(R.id.oauthemailtextbox);
        mHelper = DBHelper.getInstance(getApplicationContext());
        sendEmail = (Button) this.findViewById(R.id.saveemailbutton);
        mRec = (ListView) findViewById(R.id.m_list);
        mAdapter = new MailsAdapter(this, mHelper);
        mRec.setAdapter(mAdapter);
        oauthResult = (TextView) findViewById(R.id.oauthresult);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleSendEmailButton();
            }
        });

        configureOAuth = (Button) this.findViewById(R.id.configoauthbutton);
        configureOAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleConfigureOAuthButton();
            }
        });

        final Button clearOAuth = (Button) this.findViewById(R.id.clearoauthbutton);
        clearOAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleClearOAuthButton();
            }
        });
    }

    private void checkInternetState() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            sendEmail.setEnabled(true);
            configureOAuth.setEnabled(true);
        } else {
            sendEmail.setEnabled(false);
            configureOAuth.setEnabled(false);
        }
    }

    class MyClickListener implements View.OnClickListener {
        private Bundle b;

        public MyClickListener(Bundle b) {
            super();
            this.b = b;
        }

        @Override
        public void onClick(View v) {
            showDialog(EDIT_FRIEND, b);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mInternetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initUI() {
        if (UserData.isOAuthSetUp()) {
            oauthResult.setText(R.string.oauth_set_up);
            oauthEmail.setText(prefs.getString(UserData.PREF_KEY_OAUTH_EMAIL_ADDRESS, null));
            oauthEmail.setEnabled(false);
        } else {
            oauthResult.setText(R.string.oauth_not_set_up);
            oauthEmail.setText(null);
            oauthEmail.setEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mInternetStateReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    public void addRec(View v) {
        showDialog(NEW_FRIEND);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        if (id == EDIT_FRIEND) {
            final EditText mail = (EditText) dialog.findViewById(R.id.ed_mail);
            mail.setText(args.getString("mail"));
            mail.setTag(args.getString("mail"));
            final EditText text = (EditText) dialog.findViewById(R.id.ed_text);
            text.setText(args.getString("text"));
            text.setTag(args.getString("text"));
            final EditText subj = (EditText) dialog.findViewById(R.id.ed_subject);
            subj.setText(args.getString("subj"));
            subj.setTag(args.getString("subj"));
            dialog.findViewById(R.id.ed_id).setTag(args.getLong("id"));
        } else if (id == NEW_FRIEND) {
            final EditText mail = (EditText) dialog.findViewById(R.id.ed_mail);
            mail.setText("");
            final EditText text = (EditText) dialog.findViewById(R.id.ed_text);
            text.setText("");
            final EditText subj = (EditText) dialog.findViewById(R.id.ed_subject);
            subj.setText("");
        }
        super.onPrepareDialog(id, dialog, args);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle params) {
        AlertDialog.Builder adb = null;
        EditText mail1 = null;
        EditText text1 = null;
        EditText subj1 = null;
        View v = null;
        if (id == NEW_FRIEND || id == EDIT_FRIEND) {
            adb = new Builder(this);
            View layout = getLayoutInflater().inflate(R.layout.edit_dialog, null);
            mail1 = (EditText) layout.findViewById(R.id.ed_mail);
            text1 = (EditText) layout.findViewById(R.id.ed_text);
            subj1 = (EditText) layout.findViewById(R.id.ed_subject);
            v = layout.findViewById(R.id.ed_id);
            adb.setTitle(R.string.add_recepient);
            adb.setView(layout);
            adb.setNegativeButton(android.R.string.cancel, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        switch (id) {
        case NEW_FRIEND: {
            final EditText mail = mail1;
            final EditText text = text1;
            final EditText subj = subj1;
            adb.setPositiveButton(R.string.add, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mHelper.insert(mail.getText().toString(), text.getText().toString(), subj.getText().toString());
                    mAdapter.getCursor().requery();
                    mAdapter.notifyDataSetChanged();
                }
            });

            return adb.create();
        }
        case EDIT_FRIEND: {
            final EditText mail = mail1;
            final EditText text = text1;
            final EditText subj = subj1;
            final View v1 = v;
            adb.setPositiveButton(R.string.add, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mHelper.update((Long) v1.getTag(), mail.getText().toString(), text.getText().toString(), subj
                            .getText().toString());
                    mAdapter.getCursor().requery();
                    mAdapter.notifyDataSetChanged();
                }
            });
            return adb.create();
        }
        default:
            return super.onCreateDialog(id);
        }

    }

    private void clearCredentials() {
        final Editor edit = prefs.edit();
        edit.remove(UserData.PREF_KEY_OAUTH_ACCESS_TOKEN);
        edit.remove(UserData.PREF_KEY_OAUTH_ACCESS_TOKEN_SECRET);
        edit.remove(UserData.PREF_KEY_OAUTH_EMAIL_ADDRESS);
        edit.commit();
    }

    private void handleSendEmailButton() {
        startService(new Intent(LocalEmailService.ACTION_SEND_ALL));
    }

    private void handleConfigureOAuthButton() {
        startActivity(new Intent().setClass(getApplicationContext(), OAuthActivity.class));
    }

    private void handleClearOAuthButton() {
        clearCredentials();
        initUI();
    }
}
