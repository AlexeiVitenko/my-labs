package by.bsuir.gmailoauth;

import by.bsuir.gmailoauth.mail.LocalEmailService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_RECEIVER = "ACTION_WIDGET_RECEIVER";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent active = new Intent(context, MyWidgetProvider.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        active.putExtra("sad", "sdf");
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] ni = cm.getAllNetworkInfo();
        if (ni != null) {
            boolean flag = false;
            ;
            for (NetworkInfo networkInfo : ni) {
                if (networkInfo.isConnected()) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                remoteViews.setViewVisibility(R.id.widget_button, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_button_disabled, View.INVISIBLE);
                remoteViews.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);
            } else {
                remoteViews.setViewVisibility(R.id.widget_button, View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.widget_button_disabled, View.VISIBLE);
            }
        }
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
            context.startService(new Intent(LocalEmailService.ACTION_SEND_ALL));
        } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(new ComponentName(context.getPackageName(), MyWidgetProvider.class.getName())));
        }
    };
}
