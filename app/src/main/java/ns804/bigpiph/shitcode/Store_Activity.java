package ns804.bigpiph.shitcode;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.adapters.StoreAdapter;
import ns804.bigpiph.shitcode.models.Episode;

import ns804.bigpiph.shitcode.models.FAMILY;
import ns804.bigpiph.shitcode.models.Product;
import ns804.bigpiph.shitcode.models.Tracks;

/**
 * Created by Luke747 on 5/26/16.
 */
public class Store_Activity extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener, ResponseInterface {

    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Episode> epis;
    ArrayList<Episode> now1;
    ArrayList<Tracks> musi;
    ArrayList<Product> mStore;
    ArrayList<FAMILY> mFam;
    String sound ="", art = "", itu = "", spo = "";
    Integer num;

    String starting = "";
    RelativeLayout media;
    boolean pla = false;
    ImageView controlsV;
    ImageView dismiss;
    String app = "";
    ArrayList<Track> track;
    int m = 0;
    RelativeLayout top;
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
    Intent serviceIntent;
    Bundle bundleIt, bundle;

    ProgressDialog pdBuff = null;
    Intent seek;
    Intent pausing;
    ArrayList bundle12;
    ArrayList<Track> mListItems;
    String transfer;

    String zs = "";
    public int id = 0;
    int height = 0;
    int width = 0;
    int thu = 0;
    public String title = "";
    public String price = "";
    public String image = "";
    public String url = "";
    public String thumb = "";
    public int order = 0;
    Context context;
    Bundle bundleit;
    ArrayList<Product> products;
    ImageView storeFeat;
    RelativeLayout episode,music,fam,store,more;


    ExpandableHeightGridView lvStore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = Store_Activity.this;

        products = getIntent().getBundleExtra("bundle").getParcelableArrayList("store");
        track = getIntent().getBundleExtra("bundle").getParcelableArrayList("track");
        thu = Utility.getPrefrencesInt(context, "thumb");
        height = Utility.getPrefrencesInt(context, "height");
        width = Utility.getPrefrencesInt(context,"width");
        bundleit = getIntent().getBundleExtra("bundle");


        setContentView(R.layout.store_activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fam_activity_swipeContainer);

        ScrollView mScrollView = (ScrollView) findViewById(R.id.scrol);
        mScrollView.smoothScrollTo(0,0);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        episodes(Utility.getPrefrences(context, "access_token"));
                    }
                }, 0);
            }
        });

        seekbar = (SeekBar)findViewById(R.id.seekbar);
        top = (RelativeLayout)findViewById(R.id.top);
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

        storeFeat = (ImageView) findViewById(R.id.storeFeat);
        navbar();
        mediaControls();
        for (Object i : products) {
            Product eps = (Product) i;
            id = eps.getId();
            title = eps.getTitle();
            image = eps.getImage();
            if (eps.getFeature().equalsIgnoreCase("true")) {
                zs = eps.getUrl();
                Picasso.with(context).load(image).resize(width,height).into(storeFeat);

            }
        }

            lvStore = (ExpandableHeightGridView) findViewById(R.id.lvStore);
            lvStore.setExpanded(true);

            lvStore.setAdapter(new StoreAdapter(context, products));

            lvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!Utility.isNetworkConnected(context)) {
                        Utility.toastMessage(context,"No internet connection");
                        return;
                    }
                    Intent web = new Intent(context, WebActivity.class);
                    String url = products.get(position).getUrl();
                    web.putExtra("url", url);
                    startActivity(web);
                }
            });


        storeFeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(context, WebActivity.class);
                Log.d("url_description",": "+ zs);
                String url = zs;
                web.putExtra("url", url);
                startActivity(web);
            }
        });

        top.setOnTouchListener(new OnSwipeTouchListener(Store_Activity.this) {
            public void onSwipeTop() {
                Toast.makeText(Store_Activity.this, "top", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Store_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
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
                intent.putExtra("bundle",bundleit);
                startActivity(intent);
                finish();
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Music_Activity.class);
                intent.putExtra("bundle",bundleit);
                startActivity(intent);
                finish();
            }
        });
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Under Development",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, Family_Activity.class);
                intent.putExtra("bundle",bundleit);
                startActivity(intent);
                finish();
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Under Development",Toast.LENGTH_LONG).show();
                return;

            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoreActivity.class);
                intent.putExtra("bundle",bundleit);
                startActivity(intent);
                finish();

                //Intent intent = new Intent(context, Music_Activity.class);
                //intent.putExtra("bundle",bundleit);
                //startActivity(intent);
            }
        });
    }

    public String episodes(String token){

        List<HashMap> listParams = new ArrayList<>();

        try{

            HashMap mapParams = new HashMap();

            String bear = "bearer " + token;
            Log.d("Episodes_Failed","episodes_failed: "+token);
            Log.d("Episodes_Failed","episodes_failed: "+bear);
            mapParams.put("Authorization", bear);
            listParams.add(mapParams);
            Log.d("Episodes_Failed","episodes_failed: "+listParams);
            String url = Constants.BASE+Constants.EPI;
            new AsyncTwo(context,listParams ,"URL_EPI", (ResponseInterface) context).execute(url);
        }catch (Exception e){
            Log.d("LoginFailure","episodes_failed: ");
            e.printStackTrace();
        }
        return null;
    }

    public String music(String token){

        List<HashMap> listParams = new ArrayList<>();

        try{

            HashMap mapParams = new HashMap();

            String bear = "bearer " + token;
            Log.d("Episodes_Failed","episodes_failed: "+token);
            Log.d("Episodes_Failed","episodes_failed: "+bear);
            mapParams.put("Authorization", bear);
            listParams.add(mapParams);
            Log.d("Episodes_Failed","episodes_failed: "+listParams);
            String url = Constants.BASE+Constants.MUS;
            //String tUrl = Utility.addtoken(url, bear);
            new AsyncTwo(context,listParams ,"URL_MUS", (ResponseInterface) context).execute(url);
        }catch (Exception e){
            Log.d("LoginFailure","episodes_failed: ");

            e.printStackTrace();
        }
        return null;
    }

    public String store(String token){

        List<HashMap> listParams = new ArrayList<>();

        try{

            HashMap mapParams = new HashMap();

            String bear = "bearer " + token;
            Log.d("Episodes_Failed","episodes_failed: "+token);
            Log.d("Episodes_Failed","episodes_failed: "+bear);
            mapParams.put("Authorization", bear);
            listParams.add(mapParams);
            Log.d("Episodes_Failed","episodes_failed: "+listParams);
            String url = Constants.BASE+Constants.STORE;
            new AsyncTwo(context,listParams ,"URL_STORE", (ResponseInterface) context).execute(url);
        }catch (Exception e){
            Log.d("LoginFailure","episodes_failed: ");
            e.printStackTrace();
        }

        return null;
    }

    public String fam(String token){

        List<HashMap> listParams = new ArrayList<>();

        try{

            HashMap mapParams = new HashMap();

            String bear = "bearer " + token;
            Log.d("Episodes_Failed","episodes_failed: "+token);
            Log.d("Episodes_Failed","episodes_failed: "+bear);
            mapParams.put("Authorization", bear);
            listParams.add(mapParams);
            Log.d("Episodes_Failed","episodes_failed: "+listParams);
            String url = Constants.BASE+Constants.FAM;
            new AsyncTwo(context,listParams ,"URL_FAM", (ResponseInterface) context).execute(url);
        }catch (Exception e){
            Log.d("LoginFailure","episodes_failed: ");

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void getResponse(String result, String tag) {
        int status=0;
        String message="Server not responding";
        switch (tag){
            case "URL_EPI":
                Log.d("API_CALLS","results_epi: "+result);

                try {
                    epis = new ArrayList<>();
                    if(result.isEmpty()){
                        Toast.makeText(context, "Couldn't refresh data.",Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++){
                        JSONObject jObject = jArray.getJSONObject(i);
                        String epinum = "", title ="", videoUrl = "", descript ="", thumb ="", fullimage = "", order ="", feature ="";
                        JSONArray epidsodes = null;
                        epinum = jObject.getString("EpisodeNumber");
                        title = jObject.getString("Title");
                        videoUrl = jObject.getString("VideoURL");
                        descript = jObject.getString("Description");
                        thumb = jObject.getString("ThumbNailImage");
                        order = jObject.getString("Order");
                        fullimage = jObject.getString("FullImage");
                        epidsodes = jObject.getJSONArray("EpisodeExplores");
                        feature = jObject.getString("Featured");

                        Episode now = new Episode();

                        validate_Parcel(descript);
                        now.setEpisodeNumber(validate_Parcel(epinum));
                        now.setDescription(validate_Parcel(descript));
                        now.setFeatured(validate_Parcel(feature));
                        now.setFullImage(validate_Parcel(fullimage));
                        now.setOrder(validate_Parcel(order));
                        now.setThumbNailImage(validate_Parcel(thumb));
                        now.setVideoURL(validate_Parcel(videoUrl));
                        now.setTitle(validate_Parcel(title));
                        now.setEpisodeExplores(epidsodes.toString());
                        Log.d("EPISODES_DATA",": "+epinum);
                        Log.d("EPISODES_DATA",": "+order);
                        Log.d("EPISODES_DATA",": "+descript);
                        Log.d("EPISODES_DATA",": "+epinum);
                        Log.d("EPISODES_DATA",": "+epidsodes);
                        epis.add(i,now);
                    }
                    now1 = new ArrayList();
                    while(epis.size() > 0) {
                        int max = 0;
                        int order_min = 1000;
                        int index_min = 1000;
                        for (int i = 0; i < epis.size(); i++) {
                            Episode eps = epis.get(i);

                            try {
                                max = Integer.parseInt(eps.getOrder().toString());
                                Log.d("Order_Number",": "+ max);
                            } catch(NumberFormatException nfe) {
                                System.out.println("Could not parse " + nfe);
                            }

                            if(max < order_min ){
                                order_min = max;
                                index_min = i;
                            }
                        }
                        now1.add(epis.get(index_min));
                        epis.remove(epis.get(index_min));
                        Log.d("SortingNewList","order: "+now1);
                    }
                    music(Utility.getPrefrences(context,"access_token"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "URL_MUS":
                Log.d("API_CALLS","results_MUS: "+result);

                try {
                    musi = new ArrayList<>();
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++){
                        JSONObject jObject = jArray.getJSONObject(i);
                        int id = 0;
                        String art = "", title ="", soundstream = "", soundcloud ="", itunes ="", google = "", band ="", spotify ="";
                        JSONArray epidsodes = null;
                        art = jObject.getString("CoverArt");
                        title = jObject.getString("Title");
                        id = jObject.getInt("Id");
                        soundcloud = jObject.getString("SoundCloudURL");
                        soundstream = jObject.getString("SoundCloudStreamURL");
                        itunes = jObject.getString("iTunesURL");
                        google = jObject.getString("GooglePlayURL");
                        band = jObject.getString("BandCampURL");
                        spotify = jObject.getString("SpotifyURL");

                        Tracks now = new Tracks();

                        now.setTitle(validate_Parcel(title));
                        now.setArt(validate_Parcel(art));
                        now.setId(id);
                        now.setSoundcloud(validate_Parcel(soundcloud));
                        now.setSoundstream(validate_Parcel(soundstream));
                        now.setItunes(validate_Parcel(itunes));
                        now.setGoogleplay(validate_Parcel(google));
                        now.setBand(validate_Parcel(band));
                        now.setSpotify(validate_Parcel(spotify));

                        musi.add(i,now);
                    }
                    store(Utility.getPrefrences(context, "access_token"));
                    mListItems = new ArrayList<>();
                    for (Object i: musi) {


                        Tracks tra = (Tracks) i;
                        title= tra.getTitle();
                        sound = tra.getSoundstream();
                        art = tra.getArt();
                        num = tra.getId();
                        spo = tra.getSpotify();
                        itu = tra.getItunes();
                        app = tra.getGoogleplay();


                        Track nTrack = new Track();
                        nTrack.setmID(num);
                        nTrack.setmTitle(title);
                        nTrack.setmStreamURL(sound);
                        nTrack.setGooleplay(app);
                        nTrack.setItunes(itu);
                        nTrack.setSpotify(spo);
                        mListItems.add(nTrack);
                        Log.d("TracksInfo", "title: "+title);
                        Log.d("TracksInfo", "sound: "+sound);
                        Log.d("TracksInfo", "spotify: "+spo);
                        Log.d("TracksInfo", "number: "+num);

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "URL_FAM":
                Log.d("API_CALLS","results_FAM: "+result);

                try {
                    mFam = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jObject = new JSONObject();
                    for (int z = 0; z < jsonArray.length(); z++){

                        jObject = jsonArray.getJSONObject(z);
                        int id = 0;
                        String name = "";
                        String story = "";
                        String workex = "";
                        String workdes = "";
                        String url = "";
                        String urldes = "";
                        String thumb ="";
                        String image = "";
                        int order = 0;
                        String feature = "";
                        id = jObject.getInt("Id");
                        name = jObject.getString("Name");
                        story = jObject.getString("Story");
                        workex = jObject.getString("WorkExampleURL");
                        workdes = jObject.getString("WorkExampleDescription");
                        url = jObject.getString("URL");
                        urldes = jObject.getString("URLDescription");
                        thumb = jObject.getString("ThumbNailImage");
                        image = jObject.getString("FullImage");
                        order = jObject.getInt("Order");
                        feature = jObject.getString("Featured");

                        FAMILY family = new FAMILY();

                        family.setFeature(feature);
                        family.setId(id);
                        family.setName(name);
                        family.setStory(story);
                        family.setWorkex(workex);
                        family.setWorkdes(workdes);
                        family.setUrl(url);
                        family.setUrldes(urldes);
                        family.setThumb(thumb);
                        family.setImage(image);
                        family.setOrder(order);

                        mFam.add(z,family);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("epis", now1);
                    bundle.putParcelableArrayList("musi", musi);
                    bundle.putParcelableArrayList("store", mStore);
                    bundle.putParcelableArrayList("fam", mFam);
                    bundle.putParcelableArrayList("track", mListItems);
                    Log.d("UPDATE_UI","track-size: " +mListItems.size());
                    Utility.updateDateTime(context);

                    Intent intent = new Intent(context, Store_Activity.class);
                    intent.putExtra("bundle",bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    this.finish();

                    mSwipeRefreshLayout.setRefreshing(false);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "URL_STORE":
                Log.d("API_CALLS","results_STORE: "+result);
                JSONObject jObject = new JSONObject();
                mStore = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for(int i = 0; i < jsonArray.length(); i++){
                        int id = 0;
                        String title = "";
                        Double price = 0.00;
                        String image = "";
                        String url = "";
                        String feature = "";
                        String thumb = "";
                        int order = 0;
                        jObject = jsonArray.getJSONObject(i);

                        id = jObject.getInt("Id");
                        title = jObject.getString("Name");
                        price = jObject.getDouble("Price");
                        image = jObject.getString("FullImage");
                        url = jObject.getString("URL");
                        thumb = jObject.getString("ThumbNailImage");
                        feature = jObject.getString("Featured");
                        order = jObject.getInt("Order");

                        Product product = new Product();

                        product.setTitle(validate_Parcel(title));
                        product.setFeature(validate_Parcel(feature));
                        product.setId(id);
                        product.setPrice(price);
                        product.setImage(validate_Parcel(image));
                        product.setOrder(order);
                        product.setUrl(validate_Parcel(url));
                        product.setThumb(validate_Parcel(thumb));

                        mStore.add(i,product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fam(Utility.getPrefrences(context, "access_token"));
                break;
            default:
                break;
        }
    }

    public String validate_Parcel(String check){

        if(check == null){
            check = "n/a";
            return check;
        }else {
            return check;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putBundle("bundle", bundleit);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("bundle"));
        products = savedInstanceState.getBundle("bundle").getParcelableArrayList("store");
        bundleit=savedInstanceState.getBundle("bundle");
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
        pdBuff = ProgressDialog.show(Store_Activity.this,"Buffering...", "Acquiring song: "+info, true);
    }
    @Override
    public void onStop(){
        super.onStop();
        Utility.updateDateTime(context);
        Log.d("OnStop_called","fired");
    }


}
