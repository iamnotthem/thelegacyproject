package ns804.bigpiph.shitcode;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import ns804.bigpiph.R;

/**
 * Created by Luke747 on 7/26/16.
 */
public class CrowdFunding extends AppCompatActivity {

    Context context;
    ImageView back;
    Bundle bundle;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crowdfund);
        context = CrowdFunding.this;
        back = (ImageView)findViewById(R.id.imageView);
        bundle = getIntent().getBundleExtra("bundle");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

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
