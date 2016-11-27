package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

import ns804.bigpiph.R;


/**
 * Created by Luke747 on 6/6/16.
 */
public class MoreActivity extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener {
    String epi = "", video = "", songTitle = "", starting = "";
    RelativeLayout media, top;
    String app = "";
    ImageView dismiss;
    ArrayList<Track> track;
    int m = 0;
    String mUrl = "";
    SeekBar seekbar;
    public int seekMax;
    public static int songended = 0;
    boolean mBroadcastIsRegistered = false, mBufferBroadcastIsRegistered = false;
    String pauseExtra= "no";
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastBufferingReceiver;
    Boolean MusicPaused;
    ImageView play, fastforward, rewind, next, previous, featureAlbum;
    MediaPlayer mediaPlayer;
    MediaController control;
    Intent serviceIntent;
    ProgressDialog pdBuff = null;
    Intent seek;
    ImageView controlsV;
    Intent pausing;
    ArrayList bundle12;
    ArrayList<Track> mListItems;
    String transfer;
    Boolean pla = false;

    Context context;
    Bundle bundle, bundleIt;
    RelativeLayout episode,music,fam,store,more;
    LinearLayout about, book, livingAlbum, Website, friends, rate, feedback, facebook, instagram, snapchat, twitter, crowd;
    LinearLayout KNC, jUSt, global, logout, notification;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
        context = MoreActivity.this;
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        track = getIntent().getBundleExtra("bundle").getParcelableArrayList("track");
        play = (ImageView)findViewById(R.id.start);
        play.setImageResource(R.mipmap.ic_pause_black_24dp);
        media = (RelativeLayout)findViewById(R.id.media);
        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);
        dismiss = (ImageView)findViewById(R.id.dismiss);
        seekbar.setOnSeekBarChangeListener(this);

        MusicPaused = false;
        seek = new Intent(Music_Activity.BROADCAST_SEEKBAR);
        pausing = new Intent(Music_Activity.BROADCAST_PAUSE);

        mediaPlayer = new MediaPlayer();
        top = (RelativeLayout)findViewById(R.id.top);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent seekIntent) {
                upDateUI(seekIntent);
            }
        };

        broadcastBufferingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent bufferIntent) {
                showPD(bufferIntent);
            }
        };

        registerReceiver(broadcastBufferingReceiver, new IntentFilter(myPlayer.BROADCAST_BUFFER));
        mBufferBroadcastIsRegistered = true;
        registerReceiver(broadcastReceiver, new IntentFilter(myPlayer.BROADCAST_ACTION));
        mBroadcastIsRegistered = true;


        serviceIntent = new Intent(this, myPlayer.class);
        bundleIt = getIntent().getBundleExtra("bundle");
        navbar();
        mediaControls();
        crowd = (LinearLayout)findViewById(R.id.crowdfund);
        about = (LinearLayout)findViewById(R.id.aboutUs);
        book = (LinearLayout)findViewById(R.id.book);
        livingAlbum = (LinearLayout)findViewById(R.id.living);
        Website = (LinearLayout)findViewById(R.id.website);
        friends = (LinearLayout)findViewById(R.id.friends);
        rate = (LinearLayout)findViewById(R.id.rateReview);
        feedback = (LinearLayout)findViewById(R.id.feedback);
        facebook = (LinearLayout)findViewById(R.id.facebook);
        instagram = (LinearLayout)findViewById(R.id.instagram);
        snapchat = (LinearLayout)findViewById(R.id.snapchat);
        twitter = (LinearLayout)findViewById(R.id.twitter);
        KNC = (LinearLayout) findViewById(R.id.KNC);
        logout = (LinearLayout)findViewById(R.id.logout);
        jUSt = (LinearLayout) findViewById(R.id.just);
        global = (LinearLayout) findViewById(R.id.globalKids);
        notification = (LinearLayout)findViewById(R.id.notification);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.iamnotthem.com";
                startWebActivity(context, url);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bigpiph.com/contact/";
                startWebActivity(context, url);
            }
        });
        livingAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bigpiph.com/contact/";
                startWebActivity(context, url);
            }
        });
        Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bigpiph.com/";
                startWebActivity(context, url);
            }
        });


        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.play_store_url));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(context.getString(R.string.play_store_url)));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_SEND);
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                        "mailto","bigpiph@gmail.com", null));
                intent.setType("text/html");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"bigpiph@gmail.com"} );
                intent.putExtra(Intent.EXTRA_SUBJECT, "I Am Not Them App ");
                startActivity(Intent.createChooser(intent, "Send Email"));
                 //bigpiph@gmail.com
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/bigpiph/";
                startWebActivity(context, url);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/bigpiph/";
                startWebActivity(context, url);
            }
        });
        snapchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.snapchat.com/add/bigpiph";
                Intent nativeAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(nativeAppIntent);

                //Intent intent = new Intent(Intent.ACTION_SEND);
                //intent.setType("*/*");
                //intent.setPackage("com.snapchat.android");
                //startActivity(Intent.createChooser(intent, "Open Snapchat"));

            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/bigpiph";
                startWebActivity(context, url);
            }
        });
        KNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bigpiph.com/kncqnc/";
                startWebActivity(context, url);
            }
        });
        jUSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bigpiph.com/just";
                startWebActivity(context, url);
            }
        });
        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.globalkidsar.org/#about";
                startWebActivity(context, url);
            }
        });
        crowd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoreActivity.this, CrowdFunding.class));
                return;
                //String url = "http://www.globalkidsar.org/#about";
                //startWebActivity(context, url);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.removePreferences(context, "access_token");
                Utility.clearImageDiskCache(context);
                startActivity(new Intent(MoreActivity.this, Splash_Activity.class));
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.Settings$AppNotificationSettingsActivity");
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                    startActivity(intent);
                }else{startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS), 0);}
            }
        });










    }

    public void navbar(){
        episode = (RelativeLayout)findViewById(R.id.epiButton);
        music = (RelativeLayout)findViewById(R.id.musicTool);
        fam = (RelativeLayout)findViewById(R.id.famButton);
        store = (RelativeLayout)findViewById(R.id.shopButton);
        more = (RelativeLayout)findViewById(R.id.moreButton);
        episode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Episodes_activity.class);
                intent.putExtra("bundle",bundleIt);
                startActivity(intent);
                finish();
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Music_Activity.class);
                intent.putExtra("bundle",bundleIt);
                startActivity(intent);
                finish();
            }
        });
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Under Development",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, Family_Activity.class);
                intent.putExtra("bundle",bundleIt);
                startActivity(intent);
                finish();
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Under Development",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, Store_Activity.class);
                intent.putExtra("bundle",bundleIt);
                startActivity(intent);
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;

                //Intent intent = new Intent(context, Music_Activity.class);
                //intent.putExtra("bundle",bundleit);
                //startActivity(intent);
            }
        });
    }
    public void startWebActivity(Context context, String url){
        Intent web = new Intent(context, WebActivity.class);
        web.putExtra("url", url);
        Log.d("ITEM_WORKEX",": "+ url);
        startActivity(web);


    }
    public void onShareItem() {
        // Get access to bitmap image from view
        ImageView ivImage = (ImageView) findViewById(R.id.youtube1);
        // Get access to the URI for the bitmap
        //Uri bmpUri = Utility.getLocalBitmapUri(ivImage);
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out Big Piph");
            shareIntent.setType("*/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }
    private void togglePlayPause() {
        if(mBroadcastIsRegistered) {
            if (MusicPaused) {
                pauseExtra = "no";
                pausing.putExtra("pause", pauseExtra);
                sendBroadcast(pausing);
                play.setImageResource(R.mipmap.ic_pause_black_24dp);
                MusicPaused = false;

            } else {
                pauseExtra = "yes";
                pausing.putExtra("pause", pauseExtra);
                sendBroadcast(pausing);
                play.setImageResource(R.mipmap.ic_play_arrow_black_24dp);
                MusicPaused = true;
            }
        }
    }



    public void mediaControls(){

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    Log.d("UPDATE_UI","position: " +m);
                    Log.d("UPDATE_UI","track-size: " +track.size());
                    int n = m + 1;
                    m = n;
                    stopmyPlayer();

                    if (m < track.size()) {

                        starting = track.get(m - 1).getTitle();
                        String nUrl = track.get(m - 1).getStreamURL();
                        Log.d("UPDATE_UI","new position: " +(m+1));
                        mUrl = nUrl + "?client_id=" + Config.CLIENT_ID;
                        playAudio(mUrl);



                    }else{
                        Toast.makeText(context, "This is the last track", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception f){
                    f.printStackTrace();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int n = m - 1;
                    m = n;
                    stopmyPlayer();
                    if (m > 0) {
                        Log.d("UPDATE_UI","position: " +m);
                        starting = track.get(m - 1).getTitle();
                        String nUrl = track.get(m - 1 ).getStreamURL();
                        Log.d("UPDATE_UI","new position: " +(m));

                        mUrl = nUrl + "?client_id=" + Config.CLIENT_ID;


                        playAudio(mUrl);

                    }else{
                        Toast.makeText(context, "This is the first track", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception g){
                    g.printStackTrace();
                }
            }

        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBroadcastIsRegistered) {
                    int heightM = media.getHeight()+8;
                    media.animate().translationYBy(heightM).start();
                    //media.setVisibility(View.VISIBLE);
                    pla = false;
                    stopmyPlayer();
                    Log.d("UPDATE_UI_DoubleTap", "2: " + heightM);
                }

            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public void run() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();
        Log.d("MEDIA_DATA", "currentPosition:"+currentPosition);
        Log.d("MEDIA_DATA", "totalDuration:"+total);

        while (mediaPlayer != null && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            Log.d("MEDIA_DATA", "totalDuration:"+total);
            seekbar.setProgress(currentPosition);
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekbar, int progress,
                                  boolean fromUser) {
        try {
            if (mBroadcastIsRegistered) {
                if (fromUser){
                    int seekPos = seekbar.getProgress();
                    Log.d("Epi_UPDATE_UI",": "+seekPos);
                    seek.putExtra("seekpos", seekPos);
                    sendBroadcast(seek);
                }

            } else if (mediaPlayer == null) {
                Toast.makeText(getApplicationContext(), "Media is not running",
                        Toast.LENGTH_SHORT).show();

                Log.d("seekbar_stats","fired ");
                seekbar.setProgress(0);
            }
        } catch (Exception e) {
            Log.e("seek_bar", "" + e);
            seekbar.setEnabled(false);

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        // TODO Auto-generated method stub
        Log.d("seekbar_stats","fired ");
        Log.d("Epi_UPDATE_UI","start: "+getCurrentPosition());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        // TODO Auto-generated method stub
        Log.d("seekbar_stats","fired ");

    }

    public void stopmyPlayer(){
        try {

            if(mBroadcastIsRegistered){
                try{

                    unregisterReceiver(broadcastReceiver);
                    mBroadcastIsRegistered = false;
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("Exceptions_caught","StopPlayerFiring"+e.getClass().getName() + " " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            stopService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void playAudio(String nurl){
        serviceIntent.putExtra("sentAudioLink", nurl);
        serviceIntent.putExtra("title", starting);
        serviceIntent.putExtra("pos", m);
        Log.d("UpdateUI_play","url: "+nurl);
        Log.d("UpdateUI_play","title: "+starting);
        Log.d("UpdateUI_play","pos: "+m);
        try{
            registerReceiver(broadcastReceiver, new IntentFilter(myPlayer.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;
            startService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Exceptions_caught","playAudio"+e.getClass().getName() + " " + e.getMessage());
            Toast.makeText(getApplicationContext(),e.getClass().getName() + " " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    private void upDateUI(Intent serviceIntent){
        String counter = serviceIntent.getStringExtra("counter");
        String mediamax = serviceIntent.getStringExtra("mediamax");
        String songStatus = serviceIntent.getStringExtra("songended");
        String tit = serviceIntent.getStringExtra("title");


        if(!pla) {
            pla = true;
            int heightM = media.getHeight()+8;

            media.animate().translationYBy(-heightM).start();
        }

        m = serviceIntent.getIntExtra("pos",0);
        Log.d("UPDATE_UI_epi","counter: "+counter);
        Log.d("UPDATE_UI_epi","length: "+mediamax);
        Log.d("UPDATE_UI_epi","playing: "+songStatus);
        Log.d("UPDATE_UI_epi","number: "+m);
        int seekprogress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        seekbar.setMax(seekMax);
        songended = Integer.parseInt(songStatus);
        seekbar.setProgress(seekprogress);

        if (songended == 1) {
            int heightM = media.getHeight()+8;
            //media.setVisibility(View.GONE);
            media.animate().translationYBy(heightM).start();


            stopmyPlayer();
            pla = false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity2.activityPaused(context);
        if(mBufferBroadcastIsRegistered)
            unregisterReceiver(broadcastBufferingReceiver);
        mBufferBroadcastIsRegistered = false;

        if(mBroadcastIsRegistered){
            try{

                unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("Exceptions_caught","onPauseFiring"+e.getClass().getName() + " " + e.getMessage());
                Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        MainActivity2.activityResumed();
        if(!mBufferBroadcastIsRegistered)
            registerReceiver(broadcastBufferingReceiver, new IntentFilter(myPlayer.BROADCAST_BUFFER));
        mBufferBroadcastIsRegistered = true;
        registerReceiver(broadcastReceiver, new IntentFilter(myPlayer.BROADCAST_ACTION));
        mBroadcastIsRegistered = true;


    }
    private void showPD(Intent bufferIntent){
        String bufferValue = bufferIntent.getStringExtra("buffering");
        int bufferIntValue = Integer.parseInt(bufferValue);

        switch (bufferIntValue){
            case 0:
                if (pdBuff != null){
                    pdBuff.dismiss();
                }
                break;
            case 1:
                BufferDialogue();
                break;

        }
    }
    private void BufferDialogue(){
        String info = track.get(m - 1).getTitle();
        pdBuff = ProgressDialog.show(MoreActivity.this,"Buffering...", "Acquiring song: "+info, true);
    }


}
