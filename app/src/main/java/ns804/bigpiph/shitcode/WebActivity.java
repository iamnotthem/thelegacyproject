package ns804.bigpiph.shitcode;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Luke747 on 6/1/16.
 */
public class WebActivity extends AppCompatActivity {

    private WebView webView;
    Context context;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = WebActivity.this;
        webView = new WebView(this);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().setJavaScriptEnabled(true);

        final AppCompatActivity activity = this;

        url = getIntent().getStringExtra("url");
        Log.d("WEBVIEW_DATA","url: "+url);
        che:

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorcode, String description, String failingUrl){
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl(url);
        setContentView(webView);
        //webView = (WebView)findViewById(R.id.web);


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
