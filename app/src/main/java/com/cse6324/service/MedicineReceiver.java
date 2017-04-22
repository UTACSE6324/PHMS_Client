package com.cse6324.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;

/**
 * Created by hopelty on 2017/3/11.
 */

public class MedicineReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
//        if("android.alarm.demo.action".equals(intent.getAction())){
            Toast.makeText(context,"time to take medicine!(remind every "+intent.getStringExtra("days")+" days",Toast.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("PHMS")
                            .setContentText("time to take medicine!(remind every "+intent.getStringExtra("days")+" days")
//                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND);

            Intent resultIntent = new Intent(context,
                    MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
//    }
}
