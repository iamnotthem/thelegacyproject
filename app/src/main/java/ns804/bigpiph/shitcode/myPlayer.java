package ns804.bigpiph.shitcode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Luke747 on 6/22/16.
 */
public class myPlayer extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener
        {

    public MediaPlayer mediaPlayer = new MediaPlayer();
    String sentAudioLink = "";
    String seekpos = "";
    int intSeekpos;
    int m = 0;
    int mediaPosition;
    int mediaMax;
    private final Handler handler = new Handler();
    private static int songended;
    public static final String BROADCAST_ACTION = "ns804.bigpiph.seekprogress";
    private static final int NOTIFICATION_ID = 1;
    public static final String BROADCAST_BUFFER = "ns804.bigpiph.broadcastbuffer";
    Intent bufferIntent;
    Intent seekIntent;
    String title = "";
    private boolean isPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;




    public void onCreate(){

        bufferIntent = new Intent(BROADCAST_BUFFER);
        seekIntent = new Intent(BROADCAST_ACTION);


        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.reset();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        phoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber){
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            isPausedInCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if(mediaPlayer != null){
                            if(isPausedInCall){
                                isPausedInCall = false;
                                playMedia();
                            }
                        }
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        registerReceiver(broadcastReceiver,new IntentFilter(Music_Activity.BROADCAST_SEEKBAR));

        registerReceiver(receiver, new IntentFilter(Music_Activity.BROADCAST_PAUSE));

        //registerReceiver(broadcastReceiver1,new IntentFilter(Episodes_activity.BROADCAST_SEEKBAR));

        //registerReceiver(receiver1, new IntentFilter(Episodes_activity.BROADCAST_PAUSE));

        sentAudioLink  = intent.getExtras().getString("sentAudioLink");
        title = intent.getExtras().getString("title");
        m = intent.getExtras().getInt("pos");
        mediaPlayer.reset();
        if(!mediaPlayer.isPlaying()){
            try{
                songended = 0;
                mediaPlayer.setDataSource(sentAudioLink);
                sendBufferingBroadcast();
                mediaPlayer.prepareAsync();

            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        setupHandler();

        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            unregisterReceiver(receiver);
            unregisterReceiver(broadcastReceiver);
            //unregisterReceiver(receiver1);
            // unregisterReceiver(broadcastReceiver1);
            handler.removeCallbacks(sendUpdatesToUI);
            mediaPlayer.release();

            if(phoneStateListener != null){
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }

        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int arg1){

    }

    @Override
    public void  onSeekComplete(MediaPlayer mp){
        if(!mediaPlayer.isPlaying()){
            playMedia();
        }
    }
    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2){

        return false;
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra){
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mp){
        sendBufferCompleteBroadcast();
        playMedia();

    }
    @Override
    public void onCompletion(MediaPlayer mp){
        songended = 1;

        stopMedia();
        stopSelf();
    }
    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    public void playMedia(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }
    public void stopMedia(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }
    public void pauseMedia(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    private void sendBufferingBroadcast(){
        bufferIntent.putExtra("buffering", "1");
        sendBroadcast(bufferIntent);
    }
    private void sendBufferCompleteBroadcast(){
        bufferIntent.putExtra("buffering", "0");
        sendBroadcast(bufferIntent);
    }
    private void setupHandler(){
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);// 1 second
    }
    private Runnable sendUpdatesToUI = new Runnable() {
        @Override
        public void run() {
            LogMediaPosition();
            handler.postDelayed(this, 1000); // 2 seconds
        }
    };
    private void LogMediaPosition(){
        if(mediaPlayer.isPlaying()){
            mediaPosition = mediaPlayer.getCurrentPosition();

            mediaMax = mediaPlayer.getDuration();
            if((mediaMax - 1000) <= mediaPosition){
                songended = 1;
            }
            seekIntent.putExtra("counter",String.valueOf(mediaPosition));
            seekIntent.putExtra("mediamax",String.valueOf(mediaMax));
            seekIntent.putExtra("songended", String.valueOf(songended));
            seekIntent.putExtra("title", String.valueOf(title));
            seekIntent.putExtra("pos", m);

            sendBroadcast(seekIntent);
        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSeekPosition(intent);

        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent pausing) {
            playpause(pausing);
            Log.d("PLAYPAUSE","FIRED");

        }
    };


    private void updateSeekPosition(Intent intent){
        int seekPos = intent.getIntExtra("seekpos", 0);
        if(mediaPlayer.isPlaying()){
            handler.removeCallbacks(sendUpdatesToUI);
            mediaPlayer.seekTo(seekPos);
            setupHandler();
        }
    }
    private void playpause(Intent intent){
        String action = intent.getStringExtra("pause");
        if(action.equalsIgnoreCase("yes")){
            if(mediaPlayer.isPlaying()) {
                handler.removeCallbacks(sendUpdatesToUI);
                mediaPlayer.pause();

            }
        }else{
            mediaPlayer.start();
            setupHandler();

        }
    }
}
