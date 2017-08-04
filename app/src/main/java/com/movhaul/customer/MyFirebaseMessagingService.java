package com.movhaul.customer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
/*
*firebase push notification page
*
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "tag";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the tf
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.e(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //Log.e("tag","body::"+remoteMessage.getNotification().getBody());
        Log.e("tag","title::"+remoteMessage.getNotification().getTitle());
        Log.e("tag","string::"+remoteMessage.getNotification().toString());
        Log.e(TAG, "Data:str " + remoteMessage.toString());
        Log.e(TAG, "Data:typ " + remoteMessage.getMessageType());
        Log.e(TAG, "Data:gda " + remoteMessage.getData().toString());
        Log.e(TAG, "Data:collky " + remoteMessage.getCollapseKey());
        /*if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendNotification(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }*/
       // sendNotification(remoteMessage.getNotification().getBody());
        Log.e("tag","asdf: "+ remoteMessage.getNotification().getBody());
        if(remoteMessage.getNotification().getBody().toString().contains("Job Started")){
            send_notification1(remoteMessage.getNotification().getBody());
        }
        else {
            send_notification(remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        //String title = null,message=null;
     /*   try {
            //getting the json data
            JSONObject data = messageBody.getJSONObject("data");
            //parsing json data
             title = data.getString("title");
             message = data.getString("message");
            Log.e("tag","data: "+title+message);
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }*/
        String name = null,title = null,body = null;
        try {
            JSONObject jsonObject = new JSONObject(messageBody);
             name = jsonObject.getString("customer_name");
             title = jsonObject.getString("title");
             body = jsonObject.getString("body");
        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
        }
        Intent intent = new Intent(this, DashboardNavigation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.truck_icon,3)
                .setContentTitle("Driver Bidded")
                .setContentText(title +" by "+name)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void send_notification(String data) {
        Log.e("tag","data: "+ data);
        String name = null,title = null,body = null,message = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
           // name = jsonObject.getString("customer_name");
            if(jsonObject.has("title")) {
                title = jsonObject.getString("title");
                body = jsonObject.getString("body");
                Log.e("tag", "msg" + jsonObject.toString());
                Log.e("tag", "til" + title);
                Log.e("tag", "body" + body);
            }
            else{
                message = jsonObject.toString();
                Log.e("tag", "msg" + message);
            }
        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
            message = data;
        }
        if(message== null) {
            Intent resultIntent = new Intent(this, MyJobs.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.truck_icon);
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(0, mNotifyBuilder.build());
        }
        else{
           // Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.movhaul.customer"));
           // resultIntent.setAction(Intent.ACTION_MAIN);
           // resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.movhaul.customer"));
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Movhaul")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.truck_icon);
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(0, mNotifyBuilder.build());
        }
    }
    private void send_notification1(String data) {
        Log.e("tag","data: "+ data);
        String name = null,title = null,body = null,message = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            // name = jsonObject.getString("customer_name");
            if(jsonObject.has("title")) {
                title = jsonObject.getString("title");
                body = jsonObject.getString("body");
                Log.e("tag", "msg" + jsonObject.toString());
                Log.e("tag", "til" + title);
                Log.e("tag", "body" + body);
            }
            else{
                message = jsonObject.toString();
                Log.e("tag", "msg" + message);
            }
        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
            message = data;
        }
        if(message== null) {
            Intent resultIntent = new Intent(this, DashboardNavigation.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.truck_icon);
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(0, mNotifyBuilder.build());
        }
        else{
            // Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.movhaul.customer"));
            // resultIntent.setAction(Intent.ACTION_MAIN);
            // resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.movhaul.customer"));
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Movhaul")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.truck_icon);
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(0, mNotifyBuilder.build());
        }
    }
}