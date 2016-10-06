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
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.adapters.EpisodeAdapter;
import ns804.bigpiph.shitcode.models.Episode;
import ns804.bigpiph.shitcode.models.epiDetail;

/**
 * Created by Luke747 on 5/31/16.
 */
public class IndividualEpisodes extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener {
    String epi = "", video = "", songTitle = "", starting = "";
    String pic = "";
    Context context;
    String Extras= "";
    String imag = "";
    Toolbar toolbar;
    int t = 0;
    ImageView playFeat;
    RelativeLayout media;
    int heightM = 0;
    LinearLayout itune, spotif,apple;
    String sound ="", art = "", itu = "", spo = "", app = "";
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
    ImageView dismiss;
    ProgressDialog pdBuff = null;
    Intent seek;
    Intent pausing;
    ArrayList bundle12;
    ArrayList<Track> mListItems;
    Bundle bundle;
    Parcelable transfer;
    public String title = "";
    public String description = "";
    public String episodeNumber = "";
    public String videoURL = "";
    public String thumbNailImage = "";
    public String fullImage = "";
    public String order ="";
    public String featured ="";
    public String EpisodeExplores = "";
    ArrayList<epiDetail> episod;
    ExpandableHeightGridView lv;
    ImageView youtube1;
    RelativeLayout youtube;
    TextView cancel, share, name, more;
    int height = 0;
    int width = 0;
    int thu = 0;
    boolean pla = false;
    ImageView controlsV;
    TextView textMedia;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ind_episode);

        seekbar = (SeekBar)findViewById(R.id.seekbar);

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
        context = IndividualEpisodes.this;
        youtube1 = (ImageView)findViewById(R.id.youtube1);
        name = (TextView)findViewById(R.id.title);
        cancel = (TextView)findViewById(R.id.cancel);
        share = (TextView)findViewById(R.id.share);
        more = (TextView)findViewById(R.id.moreData);
        youtube = (RelativeLayout)findViewById(R.id.youtube);
        lv = (ExpandableHeightGridView)findViewById(R.id.episodeList);
        playFeat = (ImageView)findViewById(R.id.playbutton);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
            playFeat.setVisibility(View.GONE);
        }

        bundle = getIntent().getBundleExtra("bundle");
        heightM = bundle.getInt("height");
        track = getIntent().getBundleExtra("bundle").getParcelableArrayList("track");

        Log.d("EPISODES_DATA","bundle: "+bundle);

        Episode episode = new Episode();

        episode = bundle.getParcelable("episode");

        title = episode.getTitle();

        name.setText(title);

        description = episode.getDescription();
        episodeNumber = episode.getEpisodeNumber();
        videoURL = episode.getVideoURL();
        thumbNailImage = episode.getThumbNailImage();
        fullImage = episode.getFullImage();
        order = episode.getOrder();
        featured = episode.getFeatured();
        EpisodeExplores = episode.getEpisodeExplores();
        Log.d("ArrayInfo","adapter: "+ EpisodeExplores);
        more.setText(description);
        thu = Utility.getPrefrencesInt(context, "thumb");
        height = Utility.getPrefrencesInt(context, "height");
        width = Utility.getPrefrencesInt(context,"width");

        Picasso.with(context).load(thumbNailImage).resize(width, height).into(youtube1);

        mediaControls();
        episod = new ArrayList<>();
        JSONObject nArray; // = new JSONObject();

        try {

            JSONArray array = new JSONArray(EpisodeExplores);
            Log.d("ArrayInfo","array-size:"+array.length());

            for(int i = 0; i < array.length(); i++){

                epiDetail epi = new epiDetail();
                nArray = array.getJSONObject(i);
                String tit = nArray.getString("Title");
                String url = nArray.getString("URL");
                String descript = nArray.getString("Description");
                String thumb = nArray.getString("ThumbNailImage");

                epi.setUrl(url);
                epi.setTitle(tit);
                epi.setDescription(descript);
                epi.setThumb(thumb);

                episod.add(i, epi);
                //episod.add(epi);


                Log.d("ArrayInfo","adapter: "+ episod);
                ;
            }


            lv.setAdapter(new EpisodeAdapter(context, episod));
            lv.setExpanded(true);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String url = episod.get(position).getUrl();
                    if (url.contains("youtube")){
                        Intent movie = new Intent(context, YouTubeActivity.class);
                        String furl = url.replace("https://www.youtube.com/watch?v=", " ");
                        furl = furl.trim();
                        movie.putExtra("url", furl);
                        startActivity(movie);
                        Log.d("ITEM_WORKEX",": "+ furl);

                    }else{
                        Intent web = new Intent(context, WebActivity.class);
                        web.putExtra("url", url);
                        startActivity(web);}


                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }



        Log.d("API_call", ": "+bundle);
    youtube.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent movie = new Intent(context, YouTubeActivity.class);
            String furl = videoURL.replace("https://www.youtube.com/watch?v=", " ");
            furl = furl.trim();
            movie.putExtra("url", furl);
            startActivity(movie);
        }
    });
    youtube1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent movie = new Intent(context, YouTubeActivity.class);
            String furl = videoURL.replace("https://www.youtube.com/watch?v=", " ");
            furl = furl.trim();
            movie.putExtra("url", furl);
            startActivity(movie);
        }
    });
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            playFeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent movie = new Intent(context, YouTubeActivity.class);
                    String furl = videoURL.replace("https://www.youtube.com/watch?v=", " ");
                    furl = furl.trim();
                    movie.putExtra("url", furl);
                    startActivity(movie);
                }

            });
        }


    share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onShareItem(youtube1);
        }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    }

    public void onShareItem(View v) {
        // Get access to bitmap image from view
        ImageView ivImage = (ImageView) findViewById(R.id.youtube1);
        // Get access to the URI for the bitmap
        Uri bmpUri = Utility.getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            String mess = "Don’t be selfish. You’re better than that. Tell a friend…";
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, videoURL);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
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
            //media.animate().translationYBy(-heightM).start();
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
        pdBuff = ProgressDialog.show(IndividualEpisodes.this,"Buffering...", "Acquiring song: "+info, true);
    }


}
