package com.posco.samplepush;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FMS";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String token){   //토큰을 서버로 전송
        super.onNewToken(token);
        Log.e(TAG, "onNewToken 호출됨" + token);
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage){     //수신한 메세지를 처리
        Log.d(TAG, "onMessageReceived() 호출됨.");
        String from = remoteMessage.getFrom();
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            String contents = data.get("contents");
            sendToActivity(getApplicationContext(), from, contents, null, null);
        }

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendToActivity(getApplicationContext(), from, null, title, body);
        }

//        String from = remoteMessage.getFrom();

//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();

//        Map<String, String> data = remoteMessage.getData();

//        String title = data.get("title");
//        String body = data.get("body");

//        String contents = data.get("contents");

//        Log.d(TAG, "from : " + from + ", contents : " + contents + ", title : " + title + ", body : " + body);
//        sendToActivity(getApplicationContext(), from, contents, title, body);
    }
    private void sendToActivity(Context context, String from, String contents, String title, String body){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("contents", contents);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }
}


//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (remoteMessage.getNotification() != null) {                      //포어그라운드
//            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
//        }else if (remoteMessage.getData().size() > 0) {                           //백그라운드
//            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
//            /* 백그라운드 작동 내용 */
//        }
//
//
//    }
//
//    private void sendNotification(String messageBody, String messageTitle)  {
//        Log.d("FCM Log", "알림 메시지: " + messageBody);
//
//        /* 알림의 탭 작업 설정 */
//        Intent intent = new Intent(this, MainActivity.class);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        String channelId = "Channel ID";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//
//        /* 알림 만들기 */
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(messageTitle)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setFullScreenIntent(pendingIntent, true);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        /* 새로운 인텐트로 앱 열기 */
////        Intent newintent = new Intent(this, MainActivity.class);
////        newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startActivity( newintent );
//
//        /* 채널 만들기*/
//        /* Android 8.0 이상에서 알림을 게시하려면 알림을 만들어야 함 */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelName = "Channel Name";
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}



//    private static final String TAG = "FMS";
//    public MyFirebaseMessagingService() {
//    }
//
//    @Override
//    public void onNewToken(String token){
//        super.onNewToken(token);
//        Log.e(TAG, "onNewToken 호출됨" + token);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage){
//        Log.d(TAG, "onMessageReceived() 호출됨.");
//
//        String from = remoteMessage.getFrom();
//        Map<String, String> data = remoteMessage.getData();
//        String contents = data.get("contents");
//
//        Log.d(TAG, "from : " + from + ", contents : " + contents);
//        sendToActivity(getApplicationContext(), from, contents);
//    }
//    private void sendToActivity(Context context, String from, String contents){
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("from", from);
//        intent.putExtra("contents", contents);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        context.startActivity(intent);
//    }
//}