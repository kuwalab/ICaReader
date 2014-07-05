package net.kuwalab.android.icareader;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

/**
 * ICaの残高が表示ウィジェット
 *
 * @author kuwalab
 */
public class ICaWidget extends AppWidgetProvider {
    protected static final String PREFERENCES_NAME = "ICA_DATA";
    protected static final String PREFERENCES_CONF_DATE = "conf_date";
    protected static final String PREFERENCES_REST_MONEY = "rest_money";

    public void onUpdate(Context context, AppWidgetManager awm, int[] ids) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        ICaWidget.update(context, awm, pref);
    }

    public static void update(@NonNull Context context, @NonNull AppWidgetManager awm, @NonNull SharedPreferences pref) {
        Resources res = context.getResources();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_main);
        remoteViews.setTextViewText(
                R.id.confDate,
                pref.getString(PREFERENCES_CONF_DATE,
                        res.getString(R.string.ica_widget_unconfirmed))
        );
        remoteViews.setTextViewText(
                R.id.widetRest,
                pref.getString(PREFERENCES_REST_MONEY,
                        res.getString(R.string.ica_widget_default_rest_money))
        );
        awm.updateAppWidget(new ComponentName(context, ICaWidget.class),
                remoteViews);
    }
}
