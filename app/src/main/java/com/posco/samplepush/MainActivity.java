package com.posco.samplepush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    // Channel에 대한 id 생성
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;

    // Notification에 대한 ID 생성
    private static final int NOTIFICATION_ID = 0;



    private static final String TAG = "FMS";
    static RequestQueue requestQueue;
    private static final String TAG2 = "MyApp";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();


                        // Log and toast
                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        createNotificationChannel();



//=====================================웹뷰==============================================
        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());
        //webView.setDownloadListener(new DownloadListener(){...});  // 파일 다운로드 설정

        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부

        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
//        webview.addJavascriptInterface(new AndroidBridge(), "android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부

        //캐시 사용 여부
        webView.getSettings().setAppCacheEnabled(false);
//캐시 설정
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

// -- LOAD_CACHE_ELSE_NETWORK : 캐시 기간만료 시 네트워크 접속
// -- LOAD_CACHE_ONLY : 캐시만 불러옴 (네트워크 사용 X)
// -- LOAD_DEFAULT : 기본 모드, 캐시 사용, 기간 만료 시 네트워크 사용
// -- LOAD_NO_CACHE : 캐시모드 사용안함
// -- LOAD_NORMAL : 기본모드 캐시 사용 @Deprecated

        webView.loadUrl("http://203.228.62.17");
//===================================웹뷰==============================================

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//
//                        // Log and toast
//                        @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    //채널을 만드는 메소드
    public void createNotificationChannel() {
        //notification manager 생성
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                    ,"Test Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }
    // Notification Builder를 만드는 메소드
    private NotificationCompat.Builder getNotificationBuilder(String contents, String title, String body) {

//        Log.d("debug", contents+"");
//        Log.d("debug", title+"");
//        Log.d("debug", body+"");
        if(contents == null){
            NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                    .setContentTitle("FCM에서 보낸 메세지 제목 : " + title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_android);
            return notifyBuilder;
        } else if(title == null || body == null){
            NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                    .setContentTitle("애뮬레이터에서 보낸메세지")
                    .setContentText(contents)
                    .setSmallIcon(R.drawable.ic_android);
            return notifyBuilder;
        }
//        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
//                .setContentTitle("메세지 제목 : " + title)
//                .setContentText("애뮬레이터 메세지 : " + contents + "  FCM 메세지 : " + body)
//                .setSmallIcon(R.drawable.ic_android);
//        return notifyBuilder;
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent){

        if (intent != null){
            processIntent(intent);
        }
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent){
        String from = intent.getStringExtra("from");
        String contents = intent.getStringExtra("contents");
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        if(from == null){
            return;
        }
//        if (from == null){
//            contents = null;
//            sendNotification(contents, title, body);
//        } else if(from != null){
//            title = null;
//            body = null;
//            sendNotification(contents, title, body);
//
//        }
        sendNotification(contents, title, body);

//        sendNotification(contents, title, body);
    }
    public void sendNotification(String contents, String title, String body){
        // Builder 생성
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(contents, title, body);
        // Manager를 통해 notification 디바이스로 전달
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }


    @Override
    public void onBackPressed() {
        Log.i(TAG2, "백버튼을 눌렀어요");
        if(webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}