package com.developer.athirah.sukarela.utilities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.developer.athirah.sukarela.R;

import java.text.DateFormat;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {

    // declare static keyword
    public static final String CHANNEL = "channel";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, intent.getStringExtra(CHANNEL))
                .setSmallIcon(R.drawable.ic_place_deep_orange_600_18dp)
                .setContentTitle(intent.getStringExtra(TITLE))
                .setContentText(intent.getStringExtra(CONTENT) + DateFormat.getTimeInstance().format(new Date( )))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }


}
