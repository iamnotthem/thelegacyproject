package ns804.bigpiph.shitcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ns804.bigpiph.R;

/**
 * Created by Luke747 on 5/19/16.
 */
public class Splash_Activity extends AppCompatActivity{
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = Splash_Activity.this;


        setContentView(R.layout.splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    isStoragePermissionGranted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        }).start();
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions","Permission is granted");
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                return true;
            } else {

                Log.v("Permissions","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permissions", "Permission is granted");
            startActivity(new Intent(context, LoginActivity.class));
            finish();
            return true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            } else {
                    Utility.toastMessage(context, "Permission not granted");

            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
