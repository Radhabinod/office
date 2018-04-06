package com.techindustan.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techindustan.office.R;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.ui.message.MessageActivity;
import com.techindustan.office.ui.teams.TeamActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.VISIBILITY_PUBLIC;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {

            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
             Log.e(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());
        }
        if (!Utilities.getBooleanPref(this, Constants.IS_LOGIN))
            return;
        if (!isOfficeTime())
            return;
        if (!Utilities.getBooleanPref(this, Constants.IS_NOTIFICATION_ENABLE, true))
            return;

        if (remoteMessage.getData() != null) {
            JSONObject obj = new JSONObject(remoteMessage.getData());
            try {
                showNotificcation(obj.getString("message"), "Message from " + obj.getString("sender_name"), null, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent it = new Intent(this, MessageActivity.class);
            it.putExtra("data", obj.toString());
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            ;
            startActivity(it);
        }
    }


    void showNotificcation(String msg, String title, Bitmap picture, JSONObject object) {

        String channelId = "techindustan_pms";
        CharSequence channelName = "tecHindustan PMS";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(VISIBILITY_PUBLIC);
            //notificationChannel.enableVibration(true);
            //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("data", object.toString());
        intent.putExtra("from_notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.Builder notif = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notification).setSound(defaultSoundUri)
                    .setAutoCancel(true);
            if (picture != null)
                notif.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture).setSummaryText(msg));
            notificationManager.notify(112, notif.build());
        }
    }

    boolean isOfficeTime() {
        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 8);
        calStart.set(Calendar.MINUTE, 30);
        calStart.set(Calendar.SECOND, 0);
        Date timeStart = calStart.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 20);
        calEnd.set(Calendar.MINUTE, 30);
        calEnd.set(Calendar.SECOND, 0);
        Date timeEnd = calEnd.getTime();

        Log.e("time", "start:" + timeStart + " end:" + timeEnd);
        return current.after(calStart.getTime()) && current.before(calEnd.getTime());

    }


}

