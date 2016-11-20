package ns804.bigpiph.qualitycode.eps;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import ns804.bigpiph.R;
import ns804.bigpiph.qualitycode.api.ApiRequest;
import ns804.bigpiph.qualitycode.api.EngagementService;
import ns804.bigpiph.qualitycode.api.response.EngagementResponse;
import ns804.bigpiph.qualitycode.utils.MediaUtils;
import ns804.bigpiph.shitcode.utils.ImageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EP6Activity extends AppCompatActivity {

    public static final String TAG = "EP6_GetLoose";
    private Dialog dialog;
    View back, next;
    private Uri mImageCaptureUri;
    private Bitmap bitmap;
    private boolean good = false;
    private String suggestion;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep6);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog = new Dialog(EP6Activity.this, R.style.Engagement);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // do nothing
                }
                return true;
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gotoFirst();
    }

    private void gotoFirst() {
        dialog.setContentView(R.layout.ep6_dialog1);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                }
                return true;
            }
        });
        back.setEnabled(false);
        next.setEnabled(true);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                gotoSecond();
            }
        });
    }
    private void gotoSecond() {
        dialog.setContentView(R.layout.ep6_dialog2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoFirst();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoFirst();
                }
                return true;
            }
        });
        next.setEnabled(false);
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                showImageDialog();
            }
        });
    }
    private void gotoThird() {
        dialog.setContentView(R.layout.ep6_dialog3);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoSecond();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoSecond();
                }
                return true;
            }
        });
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                if (good) {
                    gotoFourth();
                } else {
                    gotoThirdB();
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep6_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        good = true;
                        break;
                    case R.id.rb2:
                        good = false;
                        break;
                }
            }
        });
        if (bitmap != null) ((ImageView) dialog.findViewById(R.id.photo_content)).setImageBitmap(bitmap);
    }
    private void gotoThirdB() {
        dialog.setContentView(R.layout.ep6_dialog2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoFirst();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoFirst();
                }
                return true;
            }
        });
        next.setEnabled(false);
        ((TextView) dialog.findViewById(R.id.text_view)).setText(getString(R.string.ep6_6));
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                showImageDialog2();
            }
        });
    }
    private void gotoThirdC() {
        dialog.setContentView(R.layout.ep6_dialog3c);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoSecond();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoSecond();
                }
                return true;
            }
        });
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                if (good) {
                    gotoFourth();
                } else {
                    gotoThirdB();
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep6_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        good = true;
                        break;
                    case R.id.rb2:
                        good = false;
                        break;
                }
            }
        });

        ImageView photo = (ImageView) dialog.findViewById(R.id.photo_content);
        if (bitmap != null) photo.setImageBitmap(bitmap);

        photo.setDrawingCacheEnabled(true);
        photo.buildDrawingCache();
        bitmap = Bitmap.createBitmap(photo.getDrawingCache());
        photo.setDrawingCacheEnabled(false);

    }
    private void gotoFourth() {
        dialog.setContentView(R.layout.ep6_dialog4);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(false);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                if (good) {
                    gotoFifthA();
                } else {
                    gotoFifthB();
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep6_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        good = true;
                        break;
                    case R.id.rb2:
                        good = false;
                        break;
                }
            }
        });
    }
    private void gotoFifthA() {
        dialog.setContentView(R.layout.ep6_dialog5a);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoFourth();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoFourth();
                }
                return true;
            }
        });
        next.setEnabled(true);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                goToSixth();
            }
        });
        dialog.findViewById(R.id.go_to_web_instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Share
                shareToIG();
            }
        });
    }
    private void gotoFifthB() {
        dialog.setContentView(R.layout.ep6_dialog5b);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoFourth();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoFourth();
                }
                return true;
            }
        });
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                if (good) {
                    gotoFifthA();
                } else {
                    goToSixth();
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep6_rg3)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        good = true;
                        break;
                    case R.id.rb2:
                        good = false;
                        break;
                }
            }
        });
    }
    private void goToSixth() {
        dialog.setContentView(R.layout.ep6_final);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(false);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestion = ((EditText) dialog.findViewById(R.id.ep6_suggestion_music)).getText().toString();
                postSongFive(suggestion);
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.get_loose_playlist));
            }
        });
        ((EditText) dialog.findViewById(R.id.ep6_suggestion_music)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    next.setEnabled(false);
                } else {
                    next.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }

    protected void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EP6Activity.this);
        final String[] items = new String[]{"Take new photo", "Pick from gallery"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        takePicture(99);
                        break;
                    case 1:
                        selectPicture(89);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    protected void showImageDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EP6Activity.this);
        final String[] items = new String[]{"Take new photo", "Pick from gallery"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        takePicture(79);
                        break;
                    case 1:
                        selectPicture(69);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void takePicture(int i) {
        mImageCaptureUri = Uri.fromFile(new File(
                getApplicationContext().getExternalCacheDir(), String.valueOf(System.currentTimeMillis()
                + ".jpg")));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);

        try {
            startActivityForResult(intent, i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException securityException) {
            //
        }
    }
    private void selectPicture(int i) {
        Intent intent = null;
        try {
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, i);
            } else {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, i);
            }
        } catch (SecurityException securityException2) {
            //
        }
    }
    @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 99:
                    if (null != mImageCaptureUri) {
                        if (null != dialog) dialog.dismiss();
                        try {
                            Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                            ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                            String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                            int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                            int rotationAngle = 0;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                            Matrix matrix = new Matrix();
                            matrix.setRotate(rotationAngle, (float) original.getWidth() / 2, (float) original.getHeight() / 2);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                            bitmap = x(rotatedBitmap);
                        } catch (Exception e) {}
                        gotoThird();
                    }
                    break;
                case 89:
                    mImageCaptureUri = data.getData();
                    if (null != dialog) dialog.dismiss();
                    try {
                        Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                        int rotationAngle = 0;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle, (float) original.getWidth() / 2, (float) original.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                        bitmap = x(rotatedBitmap);
                    } catch (Exception e) {}
                    gotoThird();
                    break;
                case 79:
                    if (null != mImageCaptureUri) {
                        if (null != dialog) dialog.dismiss();
                        try {
                            Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                            ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                            String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                            int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                            int rotationAngle = 0;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                            Matrix matrix = new Matrix();
                            matrix.setRotate(rotationAngle, (float) original.getWidth() / 2, (float) original.getHeight() / 2);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                            bitmap = x(rotatedBitmap);
                        } catch (Exception e) {}
                        gotoThirdC();
                    }
                    break;
                case 69:
                    mImageCaptureUri = data.getData();
                    if (null != dialog) dialog.dismiss();
                    try {
                        Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                        int rotationAngle = 0;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle, (float) original.getWidth() / 2, (float) original.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
                        bitmap = x(rotatedBitmap);
                    } catch (Exception e) {}
                    gotoThirdC();
                    break;
            }
        }
    }


    private Bitmap x(Bitmap bitmap) {
        int i = 4;
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / i,
                bitmap.getHeight() / i, true);
    }

    private void shareToIG() {
//        File file = new File(getApplicationContext().getCacheDir(), "toShare001" + ".png");
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            fos.close();
//            file.setReadable(true, false);
//
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
//            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            shareIntent.setType("image/*");
//            startActivity(Intent.createChooser(shareIntent, "Share Image"));
//        } catch (Exception e) {}


        Runnable shareRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("sharing", "begin");
                File file = new File(getApplicationContext().getCacheDir(), "toShare003" + ".png");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    file.setReadable(true, false);

                    final Intent shareIntent = new Intent();
                    Log.d("sharing", "firing intent");
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    shareIntent.setType("image/*");
                    Log.d("sharing", "firing intent done");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(Intent.createChooser(shareIntent, "Share Image"));
                        }
                    });

                } catch (Exception e) {}
            }
        };
        shareRunnable.run();

    }


    private void postSongFive(String songFive) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP6_songFive(EngagementService.EP6_SONG_FIVE, songFive);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }

}
