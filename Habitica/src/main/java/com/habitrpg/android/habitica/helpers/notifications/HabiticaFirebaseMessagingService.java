package com.habitrpg.android.habitica.helpers.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by keithholliday on 6/24/16.
 */
public class HabiticaFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("test", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d("test", "Notification Message Body: " + remoteMessage.getData().get("identifier"));

        PushNotificationManager pushNotificationManager = PushNotificationManager.getInstance(this);
        pushNotificationManager.displayNotification(remoteMessage);
    }
}
