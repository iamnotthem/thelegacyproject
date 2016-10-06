package ns804.bigpiph.shitcode;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import ns804.bigpiph.R;

/**
 * Created by Luke747 on 6/2/16.
 */
public class YouTubeActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youT;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String url = "";
    Context context;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeweb);
        context = YouTubeActivity.this;
        url = getIntent().getStringExtra("url");



        youT = (YouTubePlayerView)findViewById(R.id.youT);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        youT.initialize("AIzaSyDqokwyU3G-YMoqLFnIZqBd2fmFirYmv3k", onInitializedListener);

        youT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        MainActivity2.activityPaused(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity2.activityResumed();
    }


}
