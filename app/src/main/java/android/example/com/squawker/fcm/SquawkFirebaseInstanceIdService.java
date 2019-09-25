package android.example.com.squawker.fcm;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class SquawkFirebaseInstanceIdService extends FirebaseMessagingService {

    private final String LOG_TAG = SquawkFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(LOG_TAG, "refreshed token: "+token);
    }
}
