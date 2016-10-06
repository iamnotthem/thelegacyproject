package ns804.bigpiph.shitcode;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import ns804.bigpiph.R;

public class GcmListener extends GcmListenerService {

    private static final String TAG = "AppGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("title");
        try {
            if (!message.equals(""))
                sendNotification(message,data );
                Log.e(TAG, "Push Received");
            Log.e(TAG, "User LoggedIn :" + data);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void sendNotification(String title, Bundle data) {
        Log.d("PUSH DATA OBJECT", data.toString());
        String type,message;




        Context context = getBaseContext();
        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.tlp_logo2).setContentTitle(title)
                .setAutoCancel(true)
                .setTicker(title)
                .setContentText(title);

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


}