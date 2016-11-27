package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.adapters.SCTrackAdapter;
import ns804.bigpiph.shitcode.models.Tracks;


/**
 * Created by Luke747 on 5/20/16.
 */
public class Music_Activity extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener{
    // Anatol begin
    private int currentIndex;
    // Anatol end
    LinearLayout itune, spotif,apple, bandcamp, soundcloud, cd;
    String title = "", sound ="", art = "", itu = "", spo = "", app = "";
    Integer num;
    LinearLayout googleplay;
    int m = 0;
    String mUrl = "";
    RelativeLayout top;
    SeekBar seekbar;
    ImageView dismiss;
    public int seekMax;
    public static int songended = 0;
    boolean mBroadcastIsRegistered;
    String pauseExtra= "no";
    String starting ="";
    Boolean MusicPaused;
    ImageView play, fastforward, rewind, next, previous, featureAlbum;
    public MediaPlayer mediaPlayer;
    MediaController control;
    ImageView pic;
    TextView playing;
    Boolean pla = false;
    ExpandableHeightGridView lv ;
    ArrayList<Tracks> bundle1;
    Context context;
    RelativeLayout episode,music,fam,store,more;
    Bundle bundleIt;
    Intent serviceIntent;
    List<Tracks> tracks;
    public List<Track> mListItems;
    SCTrackAdapter mAdapter;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastBufferingReceiver;
    boolean mBufferBroadcastIsRegistered;
    ProgressDialog pdBuff = null;
    public static final String BROADCAST_SEEKBAR = "ns804.bigpiph.sendseekbar";
    public static final String BROADCAST_PAUSE = "ns804.bigpiph.pause";
    Intent seek;
    Intent pausing;
    RelativeLayout media;
    int height = 0;
    int width = 0;
    int thu = 0;
    String soundDL = "", band = "";
    Boolean setting = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        context = Music_Activity.this;
        setContentView(R.layout.music_activity);
        thu = Utility.getPrefrencesInt(context, "thumb");
        height = Utility.getPrefrencesInt(context, "height");
        width = Utility.getPrefrencesInt(context,"width");
        pic = (ImageView)findViewById(R.id.featureAlbum);
        pic.requestLayout();
        pic.getLayoutParams().height = height;
        bandcamp = (LinearLayout)findViewById(R.id.bandcamp);
        soundcloud = (LinearLayout)findViewById(R.id.soundcloud);
        googleplay = (LinearLayout)findViewById(R.id.googleplay);
        cd = (LinearLayout)findViewById(R.id.cd);
        seek = new Intent(BROADCAST_SEEKBAR);
        pausing = new Intent(BROADCAST_PAUSE);
        serviceIntent = new Intent(this, myPlayer.class);
        media = (RelativeLayout)findViewById(R.id.media);
        m = 0;
        dismiss = (ImageView)findViewById(R.id.dismiss);

        top = (RelativeLayout)findViewById(R.id.top);
        mListItems = new ArrayList<Track>();
        mediaPlayer = new MediaPlayer();
        playing = (TextView)findViewById(R.id.playing);
        //ArrayList bundle = getIntent().getBundleExtra("bundle").getParcelableArrayList("epis");



        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent serviceIntent) {
                upDateUI(serviceIntent);
            }
        };

        broadcastBufferingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent bufferIntent) {
                showPD(bufferIntent);
            }
        };




        bundle1 = getIntent().getBundleExtra("bundle").getParcelableArrayList("musi");
        setting = false;
        bundleIt = getIntent().getBundleExtra("bundle");
        navbar();
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        play = (ImageView)findViewById(R.id.start);

        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);

        featureAlbum = (ImageView)findViewById(R.id.featureAlbum);

        spotif = (LinearLayout)findViewById(R.id.spotif);
        seekbar.setOnSeekBarChangeListener(Music_Activity.this);

        play.setImageResource(R.mipmap.ic_pause_black_24dp);


        for (Object i: bundle1) {
            Tracks tra = (Tracks) i;
            title= tra.getTitle();
            sound = tra.getSoundstream();
            art = tra.getArt();
            num = tra.getId();
            spo = tra.getSpotify();
            itu = tra.getItunes();
            app = tra.getGoogleplay();
            soundDL = tra.getSoundcloud();
            band = tra.getBand();



            Track nTrack = new Track();
            nTrack.setmArtworkURL(art);
            nTrack.setmID(num);
            nTrack.setmTitle(title);
            nTrack.setmStreamURL(sound);
            nTrack.setGooleplay(app);
            nTrack.setItunes(itu);
            nTrack.setSpotify(spo);
            nTrack.setSoundcloudDown(soundDL);
            nTrack.setBand(band);
            mListItems.add(nTrack);
        }


        lv = (ExpandableHeightGridView)findViewById(R.id.lvmusic);

        lv.setExpanded(true);

        lv.setAdapter(new SCTrackAdapter(context, mListItems));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nUrl = mListItems.get(position).getStreamURL();
                mUrl = nUrl +"?client_id="+Config.CLIENT_ID;
                playing.setText(mListItems.get(position).getTitle());
                starting = mListItems.get(position).getTitle();
                m = mListItems.get(position).getID();

                // Anatol begin
                currentIndex = position;
                // Anatol end

                try {

                    //mediaPlayer.reset();
                    //mediaPlayer.setDataSource(nUrl);
                    //mediaPlayer.prepare();
                    //seekbar.setEnabled(true);
                    String albumArt = mListItems.get(position).getArtworkURL();
                    Picasso.with(context).load(albumArt).resize(width,height).centerCrop().into(pic);
                    playAudio(mUrl);
                    play.setImageResource(R.mipmap.ic_pause_black_24dp);
                    MusicPaused = false;
                    //mediaPlayer.start();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("mediaplayer_failed", "failed"+nUrl);
                }


            }
        });
        spotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(m >= 0) {
                    String itun = mListItems.get(m).getSpotify();
                    Intent itune = new Intent(context, WebActivity.class);
                    itune.putExtra("url", itun);
                    startActivity(itune);
                }else{return;}
            }
        });
        bandcamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m >= 0) {
                    String itun = mListItems.get(m).getBand();
                    Intent itune = new Intent(context, WebActivity.class);
                    itune.putExtra("url", itun);
                    startActivity(itune);
                }else{return;}

            }
        });
        soundcloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "https://soundcloud.com/bigpiph/sets/i-am-not-them-the-legacy";
                switch (currentIndex) {
                    case 0:
                        url = "https://soundcloud.com/bigpiph/1endless-summer?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 1:
                        url = "https://soundcloud.com/bigpiph/2aint-too-many?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 2:
                        url = "https://soundcloud.com/bigpiph/3get-like-me-fixed?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 3:
                        url = "https://soundcloud.com/bigpiph/4runfree-fallin?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 4:
                        url = "https://soundcloud.com/bigpiph/5why-should-i-care?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 5:
                        url = "https://soundcloud.com/bigpiph/6too-late?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 6:
                        url = "https://soundcloud.com/bigpiph/me-ft-young-red?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 7:
                        url = "https://soundcloud.com/bigpiph/8tell-em?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 8:
                        url = "https://soundcloud.com/bigpiph/9goodnight?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 9:
                        url = "https://soundcloud.com/bigpiph/10get-loose-flow?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 10:
                        url = "https://soundcloud.com/bigpiph/chills?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 11:
                        url = "https://soundcloud.com/bigpiph/12you-gotta-know?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 12:
                        url = "https://soundcloud.com/bigpiph/13hey-hey-hey?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                    case 13:
                        url = "https://soundcloud.com/bigpiph/14knowing?in=bigpiph/sets/i-am-not-them-the-legacy";
                        break;
                }
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
        googleplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m >= 0) {
                    String itun = "https://play.google.com/store/music/album/Big_Piph_I_Am_Not_Them_The_Legacy_Project?id=Bn3gzk3lkmewtb4o2qfcqdnrmgy";
                    Intent itune = new Intent(context, WebActivity.class);
                    itune.putExtra("url", itun);
                    startActivity(itune);
                }else{return;}
            }
        });
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itun = "http://www.bigpiph.com/store/sly5l83kfscxe2e8buiqnl57hbyney";
                Intent itune = new Intent(context, WebActivity.class);
                itune.putExtra("url", itun);
                startActivity(itune);
            }
        });
        top.setOnTouchListener(new OnSwipeTouchListener(Music_Activity.this) {
            public void onSwipeTop() {
                Toast.makeText(Music_Activity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                if(mBroadcastIsRegistered) {
                    if (!pla) {
                        int heightM = media.getHeight()+8;
                        //media.setVisibility(View.GONE);
                        media.animate().translationYBy(heightM).start();
                        pla = true;
                        Log.d("UPDATE_UI_DoubleTap", "1: " + heightM);
                    } else {
                        int heightM = media.getHeight()+8;
                        media.animate().translationYBy(-heightM).start();
                        //media.setVisibility(View.VISIBLE);
                        pla = false;
                        Log.d("UPDATE_UI_DoubleTap", "2: " + heightM);
                    }
                }else{


                }
            }
            public void onSwipeLeft() {
                if(mBroadcastIsRegistered) {
                    if (!pla) {
                        int heightM = media.getHeight()+8;
                        //media.setVisibility(View.GONE);
                        media.animate().translationYBy(heightM).start();
                        pla = true;
                        Log.d("UPDATE_UI_DoubleTap", "1: " + heightM);
                    } else {
                        int heightM = media.getHeight()+8;
                        media.animate().translationYBy(-heightM).start();
                        //media.setVisibility(View.VISIBLE);
                        pla = false;
                        Log.d("UPDATE_UI_DoubleTap", "2: " + heightM);
                    }
                }else{


                }
            }
            public void onSwipeBottom() {
                Toast.makeText(Music_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        mediaControls();
    }

    public void navbar() {
        episode = (RelativeLayout) findViewById(R.id.epiButton);
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
                return;
            }
        });
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Intent intent = new Intent(context, MoreActivity.class);
                intent.putExtra("bundle",bundleIt);
                startActivity(intent);
                finish();

                //Intent intent = new Intent(context, Music_Activity.class);
                //intent.putExtra("bundle",bundleit);
                //startActivity(intent);
            }
        });
    }

    private void togglePlayPause() {
        if(!playing.getText().toString().trim().equalsIgnoreCase("Select Your Track")) {
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
        }else{
            Toast.makeText(context, "Select Track From List Below", Toast.LENGTH_SHORT).show();
        }
    }

    public void mediaControls() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Anatol begin
                try {
                    stopmyPlayer();
                    if (currentIndex > mListItems.size() - 2) {
                        // Toast.makeText(context, "This is the last song.", Toast.LENGTH_SHORT).show();
                        // start over with track 1
                        playing.setText(mListItems.get(0).getTitle());
                        playAudio(mListItems.get(0).getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                        currentIndex = 0;
                    } else {
                        playing.setText(mListItems.get(currentIndex + 1).getTitle());
                        playAudio(mListItems.get(currentIndex + 1).getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                        currentIndex++;
                    }

                } catch (Exception e) { }
                // Anatol end




//
//                try {
//                    Log.d("UPDATE_UI","position: " +  m);
//
//                    stopmyPlayer();
//
//                    if (m < mListItems.size()) {
//                        starting = mListItems.get(m + 1).getTitle();
//                        String nUrl = mListItems.get(m + 1).getStreamURL();
//                        Log.d("UPDATE_UI","new position: " +(m+1));
//                        int n = m + 1;
//                        m = n;
//                        playing.setText(starting);
//
//                        mUrl = nUrl + "?client_id=" + Config.CLIENT_ID;
//                        playAudio(mUrl);
//                    } else {
//                        Toast.makeText(context, "This is the last track", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception f){
//                    f.printStackTrace();
//                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Anatol begin
                try {

                    if (currentIndex == 0) {
                        stopmyPlayer();
                        playAudio(mListItems.get(currentIndex).getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                    } else {
                        stopmyPlayer();
                        playing.setText(mListItems.get(currentIndex - 1).getTitle());
                        playAudio(mListItems.get(currentIndex - 1).getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                        currentIndex--;
                    }
                } catch (Exception e) {}
                // Anatol end


//                try {
//                    int n = m - 1;
//                    m = n;
//                    stopmyPlayer();
//                    if (m >= 0) {
//                        Log.d("UPDATE_UI","position: " +m);
//                        starting = mListItems.get(m).getTitle();
//                        String nUrl = mListItems.get(m ).getStreamURL();
//                        Log.d("UPDATE_UI","new position: " +(m-1));
//
//                        mUrl = nUrl + "?client_id=" + Config.CLIENT_ID;
//
//                        playing.setText(starting);
//                        playAudio(mUrl);
//
//                    }else{
//                        Toast.makeText(context, "This is the first track", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception g){
//                    g.printStackTrace();
//                }
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
            if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                if (fromUser){
                    int seekPos = seekbar.getProgress();
                    seek.putExtra("seekpos", seekPos);
                    Log.d("mus_UPDATE_UI",": "+seekPos);
                    sendBroadcast(seek);
                }

            } else if (mediaPlayer == null) {
                Toast.makeText(getApplicationContext(), "Media is not running",
                        Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        // TODO Auto-generated method stub

    }

    public void stopmyPlayer(){
        try {

            if(mBroadcastIsRegistered){
                try{

                    unregisterReceiver(broadcastReceiver);
                    mBroadcastIsRegistered = false;
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("Exceptions_caught","MUSstopAudio"+e.getClass().getName() + " " + e.getMessage());
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
        String notify = "BigPiph is playing";
        String messa = "Click to hide Controls";
        boolean med = true;

        Log.d("UpdateUI_play","url: "+nurl);
        Log.d("UpdateUI_play","title: "+starting);
        Log.d("UpdateUI_play","pos: "+m);

        try{
            registerReceiver(broadcastReceiver, new IntentFilter(myPlayer.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;
            startService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Exceptions_caught","MUSplayAudio"+e.getClass().getName() + " " + e.getMessage());
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
            int heightM = media.getHeight() + 8;
            Log.d("animationFired","pla = false: "+heightM + 8);

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
//            int heightM = media.getHeight();
//            //media.setVisibility(View.GONE);
//            media.animate().translationYBy(heightM+8).start();
//
//
//            stopmyPlayer();
//            pla = false;
            next.performClick();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity2.activityPaused(context);
        if(mBufferBroadcastIsRegistered){
            unregisterReceiver(broadcastBufferingReceiver);
            mBufferBroadcastIsRegistered = false;
        }

        if(mBroadcastIsRegistered) {
            try {

                unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Exceptions_caught", "MusPause" + e.getClass().getName() + " " + e.getMessage());
                Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    protected void onResume(){
        super.onResume(); if(Utility.updateDateTime(context)){finish();}
        MainActivity2.activityResumed();
        if(!mBroadcastIsRegistered) {
            registerReceiver(broadcastBufferingReceiver, new IntentFilter(myPlayer.BROADCAST_BUFFER));
            mBufferBroadcastIsRegistered = true;
            registerReceiver(broadcastReceiver, new IntentFilter(myPlayer.BROADCAST_ACTION));
            mBroadcastIsRegistered = true;

        }

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
        String info = playing.getText().toString().trim();
        pdBuff = ProgressDialog.show(Music_Activity.this,"Buffering...", "Acquiring song: "+info, true);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putBundle("bundle", bundleIt);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("bundle"));
        bundle1 = savedInstanceState.getBundle("bundle").getParcelableArrayList("epis");
        bundleIt=savedInstanceState.getBundle("bundle");
    }




}

