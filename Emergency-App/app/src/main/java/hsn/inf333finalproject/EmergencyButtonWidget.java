package hsn.inf333finalproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

public class EmergencyButtonWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views;
        Intent intent;
        PendingIntent pendingIntent;

        for(int widgetId: appWidgetIds) {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_emergency_button);

            views.setTextViewText(R.id.btnEmergency, "Emergency");

            intent = new Intent(context, EmergencyActivity.class);
            intent.putExtra("from_widget", true);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.btnEmergency, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, views);
        }

    }
}
