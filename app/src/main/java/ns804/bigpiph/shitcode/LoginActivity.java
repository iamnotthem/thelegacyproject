package ns804.bigpiph.shitcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pushwoosh.BasePushMessageReceiver;
import com.pushwoosh.BaseRegistrationReceiver;
import com.pushwoosh.PushManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ns804.bigpiph.R;
import ns804.bigpiph.qualitycode.api.ApiRequest;
import ns804.bigpiph.shitcode.models.Episode;
import ns804.bigpiph.shitcode.models.FAMILY;
import ns804.bigpiph.shitcode.models.Product;
import ns804.bigpiph.shitcode.models.Tracks;

/**
 * Created by Luke747 on 5/19/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseInterface {

    EditText etEmail;
    RelativeLayout main;
    EditText etPassword;
    ArrayList<Episode> now1;
    Button account, log, account1, log1;
    ArrayList<Episode> epis;
    ArrayList<Tracks> musi;
    ImageView logo;
    ArrayList<Track> mListItems;
    ArrayList<Product> store;
    TextView intro; View forgotPassword;
    ArrayList<FAMILY> fam;
    String title = "", sound ="", art = "", itu = "", spo = "", app = "";
    Integer num;
    private String TAG = "PushwooshSample";
    Context context;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LoginActivity.this;



        setContentView(R.layout.login_activity);


        //PicassoTools.clearCache(Picasso.with(context));
        if(!Utility.getPrefrences(context,"access_token").isEmpty()){
            main = (RelativeLayout)findViewById(R.id.main);
            main.setVisibility(View.INVISIBLE);
            Utility.updateDateTime(context);
            String token = Utility.getPrefrences(context,"access_token");
            episodes(token);


        }


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        etEmail = (EditText) findViewById(R.id.email2);
        etPassword = (EditText) findViewById(R.id.pass);
        account = (Button) findViewById(R.id.account);
        log = (Button) findViewById(R.id.login);
        account1 = (Button)findViewById(R.id.account1);
        log1 = (Button)findViewById(R.id.login1);
        intro = (TextView)findViewById(R.id.intro);
        logo = (ImageView)findViewById(R.id.logo);
        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent("http://iant.azurewebsites.net/Account/ForgotPassword");
            }
        });
        getScreenDimensions();

        initPushwoosh();




                //PushManager pushManager = PushManager.getInstance(this);
                //pushManager.unregisterForPushNotifications();




    }
    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account:
                checkValidations();
                if(credentialCheck() == true) {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    Utility.savePreferences(context,"email", email);
                    Utility.savePreferences(context,"password", password);
                    Calendar rightNow = Calendar.getInstance();
                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                    Utility.savePreferences(context,"Time", hour);
                    register(email, password);

                }else{
                    return;
                }


                break;
            case R.id.login:
                checkValidations();
                if(credentialCheck() == true) {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    Calendar rightNow = Calendar.getInstance();
                    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                    Utility.savePreferences(context,"Time", hour);
                    Utility.clearImageDiskCache(context);
                    Log.d("Password_failing_with", "password: "+password);
                    login(email, password);
                }else{
                    return;
                }
                break;
            case R.id.login1:
                log1.setVisibility(View.GONE);
                account1.setVisibility(View.GONE);
                account.setVisibility(View.GONE);
                intro.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                forgotPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.account1:
                log1.setVisibility(View.GONE);
                account1.setVisibility(View.GONE);
                intro.setVisibility(View.VISIBLE);
                intro.setText("Enter your information below to create your Legacy Project account.");
                intro.setTextSize(16);
                account.setVisibility(View.VISIBLE);
                log.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                forgotPassword.setVisibility(View.GONE);
                etEmail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);

            default:
                break;
        }
    }
    @Override
    public void getResponse(String result, String tag) {
        int status=0;
        String message="Server not responding";
        switch (tag){
            case "URL_LOGIN":
                Log.d("API_CALLS","results_login: "+result);
                Log.d("API_CALLS","results_tag: "+tag);
                    String a = result+=("error");
                Log.d("API_CALLS","results_tag: "+a);
                if(!a.equalsIgnoreCase("nullerror")){
                        try {

                            JSONObject jObject = new JSONObject(result);

                            String token = jObject.getString("access_token");
                            Utility.savePreferences(context, "access_token", token);


                            ApiRequest.token = token;

                            Log.d("API_CALLS", "results_login" + token);

                            Log.d("API_CALLS", "results_login" + result);

                            episodes(token);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                    Toast.makeText(context,"Incorrect Username, Password",Toast.LENGTH_SHORT).show();
                }
                break;
            case "URL_REG":
                Log.d("API_CALLS","results_register: "+result);

                try {
                    log1.setVisibility(View.GONE);
                    account1.setVisibility(View.GONE);
                    account.setVisibility(View.GONE);
                    log.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);
                    etPassword.setVisibility(View.VISIBLE);

                    JSONObject jObject = new JSONObject(result);

                    String mesage = jObject.getString("Message");


                    String mesag = "no";
                    //String mesag1 = mesag += jObject.getString("message");

                    if(!mesage.equalsIgnoreCase("Success")) {

                        JSONArray jsonArray = jObject.getJSONObject("ModelState").getJSONArray("");

                        //Toast.makeText(context, jsonArray.toString(), Toast.LENGTH_LONG).show();


                    }
                    if(mesage.equalsIgnoreCase("Success")){
                        Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_LONG).show();
                        login(Utility.getPrefrences(context,"email"), Utility.getPrefrences(context,"password"));
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "URL_EPI":
                Log.d("API_CALLS","results_epi: "+result);

                try {
                        epis = new ArrayList<>();
                    if(result.isEmpty()){
                        Toast.makeText(context, "Please log in or Create Account",Toast.LENGTH_LONG).show();
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

                    /*Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("epis", epis);
                    Intent intent = new Intent(context, Music_Activity.class);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);*/

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
                    fam = new ArrayList<>();
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

                        fam.add(z,family);



                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("epis", now1);
                    bundle.putParcelableArrayList("musi", musi);
                    bundle.putParcelableArrayList("store", store);
                    bundle.putParcelableArrayList("fam", fam);
                    bundle.putParcelableArrayList("track", mListItems);
                    Log.d("UPDATE_UI","track-size: " +mListItems.size());
                    Utility.updateDateTime(context);
                    Intent intent = new Intent(context, Episodes_activity.class);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);



                    }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "URL_STORE":
                Log.d("API_CALLS","results_STORE: "+result);
                JSONObject jObject = new JSONObject();
                store = new ArrayList<>();


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

                        store.add(i,product);




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


    String checkValidations(){
        if (!Utility.isNetworkConnected(context))
            return "No internet connection";
        if (TextUtils.isEmpty(etEmail.getText().toString().trim()))
            return "Please Enter Email";
        if (TextUtils.isEmpty(etPassword.getText().toString().trim()))
            return "Please Enter Password";
        else
            return "success";
    }
    public String register(String email, String password){

        List<HashMap<String, Object>> listParams = new ArrayList<>();
        JSONObject userObject = new JSONObject();
        try{
            userObject.put("email",email);
            userObject.put("password", password);
            HashMap mapParams = new HashMap();
            mapParams.put("email", email);
            mapParams.put("password", password);
            listParams.add(mapParams);
            String url = Constants.BASE+Constants.REG;
            new Async(context,listParams , url, "URL_REG", "Loading...", (ResponseInterface) context).execute();
        }catch (Exception e){
            Log.d("LoginFailure","email: "+email);
            Log.d("LoginFailure","password: "+password);
            e.printStackTrace();
        }

        return null;

    }

    public String login(String email, String password){

        List<HashMap<String, Object>> listParams = new ArrayList<>();

        JSONObject userObject = new JSONObject();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try{

            HashMap mapParams = new HashMap();

            userObject.put("grant_type", password);
            userObject.put("password", password);
            userObject.put("email", email);


            //String grant_type = nObject.optString("grant_type");


            params.add(new BasicNameValuePair("grant_type", password));

            params.add(new BasicNameValuePair("password",password));

            params.add(new BasicNameValuePair("username", email));


            mapParams.put("user", userObject);
            listParams.add(mapParams);
            String url = Constants.BASE+Constants.LOG;

            new Async(context, listParams, url, "URL_LOGIN", "Loading...", (ResponseInterface) context).execute();
        }catch (Exception e){
            Log.d("LoginFailure","email: "+email);
            Log.d("LoginFailure","password: "+password);
            e.printStackTrace();
        }


        return null;

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
            //String tUrl = Utility.addtoken(url, bear);
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
            //String tUrl = Utility.addtoken(url, bear);
            new AsyncTwo(context,listParams ,"URL_FAM", (ResponseInterface) context).execute(url);
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
            //String tUrl = Utility.addtoken(url, bear);
            new AsyncTwo(context,listParams ,"URL_STORE", (ResponseInterface) context).execute(url);
        }catch (Exception e){
            Log.d("LoginFailure","episodes_failed: ");

            e.printStackTrace();
        }

        return null;

    }

    public String validate_Parcel(String check){

        if(check == null){
         check = "n/a";
            return check;
        }else {
            return check;
        }
    }

    public void getScreenDimensions(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        Log.d("ScreenDimensions1", "Height:"+screenHeight);
        Log.d("ScreenDimensions1", "width:"+screenWidth);
        if(screenHeight >= 1800){

            Utility.savePreferences(context, "width", screenWidth);
            Utility.savePreferences(context, "height", screenWidth);
            Utility.savePreferences(context, "thumb", 400);
        }else if(screenHeight >= 800 && screenHeight < 1800){
            Utility.savePreferences(context, "width", screenWidth);
            Utility.savePreferences(context, "height", screenWidth);
            Utility.savePreferences(context, "thumb", 200);
        }else if(screenHeight >500 && screenHeight < 800){
            Utility.savePreferences(context, "width", screenWidth);
            Utility.savePreferences(context, "height", screenWidth);
            Utility.savePreferences(context, "thumb", 100);
        }else{
            Utility.savePreferences(context, "width", screenWidth);
            Utility.savePreferences(context, "height", screenWidth);
            Utility.savePreferences(context, "thumb", 150);

        }






    }

    public boolean credentialCheck(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(password.length() < 6){
            Toast.makeText(context,"Please make sure password is at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        Pattern emailRegex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if(!validate(email,emailRegex) == true){
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(isAlphaNumeric(password) != true){
            Toast.makeText(context, "Please on use characters A-Z or 1-9",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    public static boolean validate(String emailStr, Pattern emailRegex) {
        Matcher matcher = emailRegex.matcher(emailStr);
        return matcher.find();
    }

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

        unregisterReceivers();
    }

    private void initPushwoosh() {
        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = PushManager.getInstance(this);

        //Start push manager, this will count app open for Pushwoosh stats as well
        try {
            pushManager.onStartup(this);
        }
        catch(Exception e)
        {
            //push notifications are not available or AndroidManifest.xml is not configured properly
        }

        //Register for push!
        pushManager.registerForPushNotifications();

        checkMessage(getIntent());
    }
    BroadcastReceiver mBroadcastReceiver = new BaseRegistrationReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };

    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            //showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter, getPackageName() +".permission.C2D_MESSAGE", null);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }

    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                //	showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                //	showMessage("register");
                token = intent.getExtras().getString("REGISTER_EVENT");
                Log.d("TOKEN", token);

            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                //showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                showMessage("unregister error");
            }

            resetIntentValues();
        }
    }



    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);
    }

    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, Splash_Activity.class));
    }

    @Override
    public void onResume(){
        super.onResume();
        MainActivity2.activityResumed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity2.activityPaused(context);
    }
}
