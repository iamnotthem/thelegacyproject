package ns804.bigpiph.shitcode;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.fragment.PushEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import ns804.bigpiph.R;

//import com.ns804.dentalprotrader.adapters.AdapterOpenJobs;

public class MainActivity2 extends Application implements PushEventListener{

    public static MainActivity2 instance = null;

    public static final String IG_CLIENT_ID = "49944c8ff28b4f7fb3b940d210a216a1";
    public static final String IG_CLIENT_SECRET = "79802608fb93432a9234e1211c86a579";
    public static final String IG_CALLBACK_URL = "https://bigpiph.com";

    public static  Context context;
    private static boolean activityVisible;
    public static String topActivity="";
    public static String chaterId = "";
    public String TAG = "PushwooshSample";
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("LIFETIME", "onCreate");
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
        registerReceiver(mReceiver, intentFilter, getPackageName() + ".permission.C2D_MESSAGE", null);


    }

    BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {

            JSONObject dataObject=null;
            try {
                JSONObject mObject = new JSONObject(intent.getExtras().getString(JSON_DATA_KEY));
                Log.d("PUSHHHHHHHH",""+mObject);
                dataObject = new JSONObject(mObject.getString("title")).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String type = dataObject.optString("type", "");
            if (!Utility.getPrefrences(getBaseContext(), "UID").isEmpty()) {
                if (type.equals("new_msg") && topActivity.equals("ChatActivity"))
                    return;
                sendNotification(getResources().getString(R.string.app_name), dataObject);
            }
        }
    };

    public static Context getInstance() {
        if (null == instance) {
            instance = new MainActivity2();
            context = instance;
        }
        return instance;
    }

    public void sendNotification(String title, JSONObject data) {
        Log.d("PUSH DATA OBJECT", data.toString());
        String type,message;

        type = data.optString("type","");
        message = data.optString("message","");

        Context context = getBaseContext();
        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.tlp_logo2).setContentTitle(title)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentText(message);

        Intent resultIntent = new Intent(context, LoginActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);



        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(2, mBuilder.build());
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void doOnRegistered(String registrationId)
    {
        Log.i(TAG, "Registered for pushes: " + registrationId);
    }

    @Override
    public void doOnRegisteredError(String errorId)
    {
        Log.e(TAG, "Failed to register for pushes: " + errorId);
    }

    @Override
    public void doOnMessageReceive(String message)
    {
        Log.i(TAG, "Notification opened: " + message);
    }

    @Override
    public void doOnUnregistered(final String message)
    {
        Log.i(TAG, "Unregistered from pushes: " + message);
    }

    @Override
    public void doOnUnregisteredError(String errorId)
    {
        Log.e(TAG, "Failed to unregister from pushes: " + errorId);
    }

    public static boolean isActivityVisible() {

        return activityVisible;

    }

    public static void activityResumed() {
        activityVisible = true;
        Log.d("UpdatingCache","fired activity onResume"+activityVisible);

    }

    public static void activityPaused(Context context1) {
        activityVisible = false;
        Log.d("UpdatingCache","fired activity onPause"+ activityVisible);
        checkforNewActivity(context1);


    }



    public static void checkforNewActivity(Context context1){
        final Context context2 = context1;
        final Boolean check;
        final Handler handler = new Handler();
        Log.d("UpdatingCache","fired in function");
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (!activityVisible) {
                    if (!isActivityVisible()) {
                        Log.d("UpdatingCache", "fired in runnable");
                        final boolean check = Utility.updateDateTime(context2);
                        if (check) {
                            System.exit(0);
                        }
                        activityResumed();

                    }
                }
            }

        };
        handler.postDelayed(runnable, 4000);


    }
}