package umn.cs5115.kiwi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NotificationReceiver", "Got notification intent!");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        notif.setSmallIcon(android.R.drawable.ic_delete)
        .setContentTitle("Huehuehue")
        .setContentText("Huehuehuehuehuehue")
        .setTicker("Yo there")
        .setAutoCancel(true);
        manager.notify(1000, notif.build());
    }

}
