package basket.amatoripescara.it.amatoripescara;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by aless on 08/08/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Firebase", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        showNotification(remoteMessage.getNotification().getTitle() ,remoteMessage.getNotification().getBody());
    }

    public void showNotification(String title, String message){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setSound(uri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 500, 500})
                .setSmallIcon(R.drawable.logo_notify);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
