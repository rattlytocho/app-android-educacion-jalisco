package mx.gob.jalisco.portalsej.portalsej.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent alarmIntent = new Intent(context, NotifyService.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            long updateInterval = AlarmManager.INTERVAL_HALF_HOUR;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + updateInterval, updateInterval, pendingIntent);

            //Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }
}
