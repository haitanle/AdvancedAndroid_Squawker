package android.example.com.squawker.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkFirebaseInstanceIdService extends FirebaseMessagingService {

    private final String LOG_TAG = SquawkFirebaseInstanceIdService.class.getSimpleName();
    private final String CHANNEL_ID = "fcm";
    private final int NOTIFICATION_ID = 8384;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(LOG_TAG, "refreshed token: "+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       insertSquawk(remoteMessage);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle("Notification")
                .setContentText(remoteMessage.getData().get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void insertSquawk(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String author = data.get("author");
        String message = data.get("message");
        String date = data.get("date");
        String authorKey = data.get("authorKey");

        ContentValues values = new ContentValues();
        values.put(SquawkContract.COLUMN_DATE, date);
        values.put(SquawkContract.COLUMN_AUTHOR, author);
        values.put(SquawkContract.COLUMN_AUTHOR_KEY, authorKey);
        values.put(SquawkContract.COLUMN_MESSAGE, message);

        getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, values);
    }
}
