package com.habitrpg.android.habitica.helpers.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.receivers.LocalNotificationActionReceiver;

/**
 * Created by keithholliday on 7/1/16.
 */
public class QuestInviteLocalNotification implements HabiticaLocalNotification {
    @Override
    public void notifyLocally(Context context, String title, String message) {
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_gryphon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(path);

        Resources res = context.getResources();

        Intent acceptInviteIntent = new Intent(context, LocalNotificationActionReceiver.class);
        acceptInviteIntent.setAction(res.getString(R.string.accept_quest_invite));
        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(
                context,
                3000,
                acceptInviteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.addAction(R.drawable.ic_gryphon, "Accept", pendingIntentAccept);

        Intent rejectInviteIntent = new Intent(context, LocalNotificationActionReceiver.class);
        rejectInviteIntent.setAction(res.getString(R.string.reject_quest_invite));
        PendingIntent pendingIntentReject = PendingIntent.getBroadcast(
                context,
                2000,
                rejectInviteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.addAction(R.drawable.ic_gryphon, "Reject", pendingIntentReject);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(10, notificationBuilder.build());
    }
}
