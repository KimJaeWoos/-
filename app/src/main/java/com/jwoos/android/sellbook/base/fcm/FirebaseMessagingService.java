package com.jwoos.android.sellbook.base.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.utils.Dlog;
import com.jwoos.android.sellbook.intro.SplashActivity;

import java.util.Random;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("book_id"));
    }

    private void showNotification(String message,String book_id) {

        Intent i = new Intent(this, SplashActivity.class);
        Dlog.d("노티데이터 : " + book_id);
        i.putExtra("book_id",book_id);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("셀북")
                .setContentText(message)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Random random = new Random();
        manager.notify(random.nextInt(),builder.build());
    }


}
