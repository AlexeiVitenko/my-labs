package by.bsuir.gmailoauth;

import by.bsuir.gmailoauth.mail.LocalEmailService;
import by.bsuir.gmailoauth.mail.LocalEmailService.EmailTaskCallback;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_RECEIVER = "ACTION_WIDGET_RECEIVER";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent active = new Intent(context, MyWidgetProvider.class);
        active.setAction(ACTION_WIDGET_RECEIVER);
        active.putExtra("sad", "sdf");
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget_button, actionPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
            if (LocalEmailService.get() == null) {
                context.startService(new Intent(context, LocalEmailService.class));
            } else {
                LocalEmailService.get().sendEmails(new EmailTaskCallback() {
                    @Override
                    public void emailTaskDone(Boolean result, String errorMessage) {
                        Log.i("", "Email test result: " + result + " error message: " + errorMessage);
                    }
                });
            }
        }
    };
}
