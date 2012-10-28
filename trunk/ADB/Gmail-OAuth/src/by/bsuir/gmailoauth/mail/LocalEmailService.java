package by.bsuir.gmailoauth.mail;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.RemoteViews;
import android.widget.Toast;
import by.bsuir.gmailoauth.MainActivity;
import by.bsuir.gmailoauth.R;
import by.bsuir.gmailoauth.UserData;
import by.bsuir.gmailoauth.data.DBHelper;
import by.bsuir.gmailoauth.data.DBHelper.DBColumns;
import by.bsuir.gmailoauth.util.OAuthBuilder;
import by.bsuir.gmailoauth.util.OAuthHelper;

public class LocalEmailService extends IntentService {
    private static final int NOTIFICATION_ID = 1927;
    public static String ACTION_SEND_ALL = "by.bsuir.gmailoauth.mail.ACTION_SEND_ALL";
    private final Handler mHandler;

    public LocalEmailService() {
        super(TAG);
        mHandler = new Handler();
    }

    private static final String TAG = "LocalEmailService";

    private SharedPreferences prefs;

    public boolean isSetUp() {
        return prefs.getString(UserData.PREF_KEY_TARGET_EMAIL_ADDRESS, null) != null;
    }

    public interface EmailTaskCallback {
        void emailTaskDone(Boolean result, String errorMessage);
    }

    private static class EmailTaskResponseType extends Pair<Boolean, String> {
        private EmailTaskResponseType(Boolean first, String second) {
            super(first, second);
        }

        public static EmailTaskResponseType create(Boolean first, String second) {
            return new EmailTaskResponseType(first, second);
        }

        public String toString() {
            return "" + first + ":" + second;
        }
    }

    private class EmailsSendTask {
        private final String fromEmail;
        private final String xoauthString;
        private final EmailTaskCallback callback;
        private final Cursor mCursor;
        private final NotificationManager mNotifyManager;
        private Notification noti;

        public EmailsSendTask(Cursor c, String from, String xoauthString, EmailTaskCallback callback) {
            mCursor = c;
            this.fromEmail = from;
            this.xoauthString = xoauthString;
            this.callback = callback;
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        private String getProgress(Resources rec, int curr, int max) {
            return String.format(rec.getString(R.string.notification_message), curr, max);
        }

        private RemoteViews onPreExecute() {
            long when = System.currentTimeMillis();
            noti = new Notification(R.drawable.ic_launcher, getString(R.string.notification_emails), when);
            Context context = LocalEmailService.this.getApplicationContext();
            Intent notiIntent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity(context, 0, notiIntent, 0);
            noti.flags |= Notification.FLAG_AUTO_CANCEL;
            final RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
            contentView.setTextViewText(R.id.n_message, getProgress(getResources(), 0, mCursor.getCount()));
            contentView.setProgressBar(R.id.n_progress, mCursor.getCount(), 0, false);
            noti.contentView = contentView;
            noti.contentIntent = pi;
            mNotifyManager.notify(NOTIFICATION_ID, noti);
            return contentView;
        }

        public void execute() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                if (mCursor.getCount() > 0) {
                    doTask(onPreExecute());
                }
            } else {
                showToast(getString(R.string.check_connection));
            }
        }

        protected void doTask(final RemoteViews contentView) {
            EmailTaskResponseType result;
            try {
                int mailIndex = mCursor.getColumnIndex(DBColumns.MAIL);
                int textIndex = mCursor.getColumnIndex(DBColumns.TEXT);
                int subjIndex = mCursor.getColumnIndex(DBColumns.SUBJECT);
                if (mCursor.moveToFirst()) {
                    do {
                        OAuthGMailSender sender = new OAuthGMailSender(xoauthString);
                        sender.sendMail(mCursor.getString(subjIndex), mCursor.getString(textIndex), fromEmail,
                                mCursor.getString(mailIndex));
                        contentView.setTextViewText(R.id.n_message,
                                getProgress(getResources(), mCursor.getPosition() + 1, mCursor.getCount()));
                        contentView.setProgressBar(R.id.n_progress, mCursor.getCount(), mCursor.getPosition() + 1,
                                false);
                        mNotifyManager.notify(NOTIFICATION_ID, noti);
                    } while (mCursor.moveToNext());
                }
                result = EmailTaskResponseType.create(true, null);
                showToast(String.format(getString(R.string.success), mCursor.getCount()));
            } catch (Exception e) {
                Log.e(TAG, "An error occurred while sending email: " + e, e);
                showToast(getString(R.string.error));
                result = EmailTaskResponseType.create(false, "Error sending email: " + e.getMessage());
            }
            mNotifyManager.cancel(NOTIFICATION_ID);

            Log.i(TAG, "Email sent, result: " + result);
            callback.emailTaskDone(result.first, result.second);
        }
    }

    private void showToast(final String text) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String buildXOAuth() {
        String fromEmail = prefs.getString(UserData.PREF_KEY_OAUTH_EMAIL_ADDRESS, null);
        String url = String.format(OAuthHelper.URL_SMTP_AUTH, fromEmail);
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        OAuthBuilder.get().signRequest(UserData.getAccessToken(), request);

        StringBuilder sb = new StringBuilder();
        sb.append("GET ");
        sb.append(url);
        sb.append(" ");
        Set<String> requiredParams = new HashSet<String>(Arrays.asList(OAuthConstants.NONCE, OAuthConstants.TIMESTAMP,
                OAuthConstants.SCOPE, OAuthConstants.SIGN_METHOD, OAuthConstants.SIGNATURE, OAuthConstants.VERIFIER,
                OAuthConstants.TOKEN, OAuthConstants.VERSION, OAuthConstants.CONSUMER_KEY));
        int i = 0;
        for (Entry<String, String> entry : request.getOauthParameters().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!requiredParams.contains(key))
                continue;

            if (i++ != 0)
                sb.append(",");
            sb.append(key);
            sb.append("=\"");
            sb.append(OAuthEncoder.encode(value));
            sb.append("\"");
        }
        Log.d(TAG, "xoauth encoding " + sb);

        Base64 base64 = new Base64();
        try {
            byte[] buf = base64.encode(sb.toString().getBytes("utf-8"));
            return new String(buf, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Invalid XOAUTH string: " + sb, e);
        }
        return null;
    }

    public void sendEmails(EmailTaskCallback emailTaskCallback) {
        if (!UserData.isOAuthSetUp()) {
            Log.e(TAG, "Please configure email send method.");
            return;
        }
        String xoauthString = buildXOAuth();
        Log.d(TAG, "XOAuth: " + xoauthString);
        String fromEmail = prefs.getString(UserData.PREF_KEY_OAUTH_EMAIL_ADDRESS, null);
        EmailsSendTask emailSendTask = new EmailsSendTask(DBHelper.getInstance(getApplicationContext()).getAll(),
                fromEmail, xoauthString, emailTaskCallback);
        emailSendTask.execute();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = UserData.getPrefs(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_ALL)) {
            sendEmails(new EmailTaskCallback() {
                @Override
                public void emailTaskDone(Boolean result, String errorMessage) {
                    Log.i(TAG, "Email test result: " + result + " error message: " + errorMessage);
                }
            });
        }
    }
}
