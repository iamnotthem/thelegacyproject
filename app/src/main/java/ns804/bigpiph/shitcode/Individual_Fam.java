package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.models.FAMILY;


/**
 * Created by Luke747 on 5/30/16.
 */
public class Individual_Fam extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener{
    String epi = "", video = "", songTitle = "", starting = "";
    int t = 0;
    RelativeLayout media;
    ImageView dismiss;
    LinearLayout itune, spotif,apple;
    String title = "", sound ="", art = "", itu = "", spo = "", app = "";
    Integer num;
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
    //public static final String BROADCAST_SEEKBAR = "ns804.bigpiph.sendseekbar";
    //public static final String BROADCAST_PAUSE = "ns804.bigpiph.pause";
    Intent serviceIntent;
    ImageView vid;
    ImageView imageViewItem, imageViewItem2, imageViewItem3, imageViewItem4;
    TextView textViewItemName, textViewItemName2, textViewItemName3, textViewItemName4;
    TextView episodenum, episodenum2, episodenum3, episodenum4;
    Bundle bundleIt;
    RelativeLayout episode,music,fam,store,more;
    ProgressDialog pdBuff = null;
    Intent seek;
    Intent pausing;
    ArrayList bundle12;
    ArrayList<Track> mListItems;
    String transfer;
    ImageView controlsV;
    ImageView pic;
    TextView name, bio, info_link, info_link2, back;
    Context context;
    String Name = "";
    String workex = "";
    String workdes = "";
    String url = "";
    String urldes = "";
    String Bio = "";
    String Pic = "";
    LinearLayout extra, extra2, border;
    Bundle bundle;
    int height = 0;
    int heightM = 0;
    int width = 0;
    int thu = 0;
    FAMILY indi;
    boolean pla = false;
    protected void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        context = Individual_Fam.this;
        setContentView(R.layout.fam_individual);
        bundle = getIntent().getBundleExtra("bundle");
        track = bundle.getParcelableArrayList("track");
        thu = Utility.getPrefrencesInt(context, "thumb");
        height = Utility.getPrefrencesInt(context, "height");
        width = Utility.getPrefrencesInt(context,"width");
        heightM = bundle.getInt("height");
        indi = bundle.getParcelable("indi");

        Name = indi.getName();
        Bio = indi.getStory();
        Pic = indi.getImage();
        urldes = indi.getUrldes();
        url = indi.getUrl();
        workdes = indi.getWorkdes();
        workex = indi.getWorkex();


        Log.d("animationFired", ": "+ heightM);

        Log.d("FAMILY_MOD", bundle.toString());


        seekbar = (SeekBar)findViewById(R.id.seekbar);

        play = (ImageView)findViewById(R.id.start);
        play.setImageResource(R.mipmap.ic_pause_black_24dp);
        media = (RelativeLayout)findViewById(R.id.media);
        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);

        seekbar.setOnSeekBarChangeListener(this);

        MusicPaused = false;
        seek = new Intent(Music_Activity.BROADCAST_SEEKBAR);
        pausing = new Intent(Music_Activity.BROADCAST_PAUSE);
        dismiss = (ImageView)findViewById(R.id.dismiss);
        mediaPlayer = new MediaPlayer();

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

        mediaControls();

        serviceIntent = new Intent(this, myPlayer.class);

        extra = (LinearLayout)findViewById(R.id.link);
        extra2 = (LinearLayout)findViewById(R.id.link22);
        info_link = (TextView)findViewById(R.id.link1);
        info_link2 = (TextView)findViewById(R.id.link2);
        border = (LinearLayout)findViewById(R.id.border66);
        back = (TextView)findViewById(R.id.back);

        pic = (ImageView)findViewById(R.id.proPic);
        name = (TextView)findViewById(R.id.name);
        bio = (TextView)findViewById(R.id.bio);


        Picasso.with(context).load(Pic).resize(width, height).into(pic);

        name.setText(Name);
        bio.setText(Bio);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(urldes != null ){
            extra.setVisibility(View.VISIBLE);
            info_link.setText(urldes);
            extra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(url.contains("youtube")){
                        Intent movie = new Intent(context, YouTubeActivity.class);
                        String furl = url.replace("https://www.youtube.com/watch?v=", " ");
                        furl = furl.trim();
                        movie.putExtra("url", furl);
                        Log.d("ITEM_WORKEX",": "+ url);
                        startActivity(movie);

                    }else {

                        Intent web = new Intent(context, WebActivity.class);
                        web.putExtra("url", url);
                        Log.d("ITEM_WORKEX",": "+ url);
                        startActivity(web);
                    }

                }
            });

        }
        if(!workdes.contains("null")){
            extra2.setVisibility(View.VISIBLE);
            info_link2.setText(workdes);
            border.setVisibility(View.VISIBLE);
            extra2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(workex.contains("youtube")){
                        Intent movie = new Intent(context, YouTubeActivity.class);
                        String furl = workex.replace("https://www.youtube.com/watch?v=", " ");
                        Log.d("ITEM_WORKEX1",": "+ furl);
                        furl = furl.trim();
                        movie.putExtra("url", furl);
                        startActivity(movie);

                    }else {

                        Intent web = new Intent(context, WebActivity.class);
                        web.putExtra("url", workex);
                        startActivity(web);
                    }


                }
            });
        }
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
                    media.setVisibility(View.GONE);
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
            media.setVisibility(View.VISIBLE);
            Log.d("animationFired", "1: " + heightM);

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
            media.setVisibility(View.GONE);


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
        pdBuff = ProgressDialog.show(Individual_Fam.this,"Buffering...", "Acquiring song: "+info, true);
    }



}
