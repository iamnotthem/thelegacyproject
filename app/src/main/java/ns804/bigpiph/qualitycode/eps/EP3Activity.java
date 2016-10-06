package ns804.bigpiph.qualitycode.eps;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

import ns804.bigpiph.R;
import ns804.bigpiph.qualitycode.api.ApiRequest;
import ns804.bigpiph.qualitycode.api.EngagementService;
import ns804.bigpiph.qualitycode.api.response.EngagementResponse;
import ns804.bigpiph.shitcode.utils.ImageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EP3Activity extends AppCompatActivity {

    public static final String TAG = "EP3_Run";
    private Dialog dialog;
    View back, next;
    private static final int CHOICE_PERSON = 0;
    private static final int CHOICE_THING = 1;
    private static final int CHOICE_PLACE = 2;
    private int choice;
    private Uri mImageCaptureUri;
    private Bitmap bitmap;
    private boolean good = false;
    private String suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep3);
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
                share();
            }
        });
        dialog = new Dialog(EP3Activity.this, R.style.Engagement);
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
        dialog.setContentView(R.layout.ep3_dialog1);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(false);
        next.setEnabled(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                }
                return true;
            }
        });
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
                gotoSecond(false);
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep3_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        choice = CHOICE_PERSON;
                        break;
                    case R.id.rb2:
                        choice = CHOICE_PLACE;
                        break;
                    case R.id.rb3:
                        choice = CHOICE_THING;
                        break;
                }
            }
        });
    }

    private void gotoSecond(boolean picTaken) {
        dialog.setContentView(R.layout.ep3_dialog2);
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
                gotoFirst();
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
                gotoThird();
            }
        });
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
        if (picTaken) {
            next.setEnabled(true);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.photo_content);
            imageView.setVisibility(View.VISIBLE);
            if (null != bitmap) imageView.setImageBitmap(bitmap);
            dialog.findViewById(R.id.camera).setVisibility(View.GONE);
        }
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });
        TextView textView = (TextView) dialog.findViewById(R.id.main_text);
        switch (choice) {
            case CHOICE_PERSON:
                textView.setText(getString(R.string.ep3_5));
                break;
            case CHOICE_PLACE:
                textView.setText(getString(R.string.ep3_6));
                break;
            case CHOICE_THING:
                textView.setText(getString(R.string.ep3_7));
                break;
        }
    }

    private void gotoThird() {
        dialog.setContentView(R.layout.ep3_dialog3);
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
                dialog.dismiss();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                if (good) {
                    gotoFourth();
                } else {
                    gotoThirdB(false);
                }
            }
        });
        if (null != bitmap) ((ImageView) dialog.findViewById(R.id.frame)).setImageBitmap(bitmap);
        ((RadioGroup) dialog.findViewById(R.id.ep3_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    private void gotoThirdB(boolean picTaken) {
        dialog.setContentView(R.layout.ep3_dialog3b);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(false);
        next.setEnabled(false);
        if (picTaken) {
            next.setEnabled(true);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.photo_content);
            imageView.setVisibility(View.VISIBLE);
            if (null != bitmap) imageView.setImageBitmap(bitmap);
            dialog.findViewById(R.id.camera).setVisibility(View.GONE);
        }
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
                gotoThirdC();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoThird();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoThird();
                }
                return true;
            }
        });
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog2();
            }
        });
    }

    private void gotoThirdC() {
        dialog.setContentView(R.layout.ep3_dialog3c);
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
                dialog.dismiss();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                if (good) {
                    gotoFourth();
                } else {
                    gotoThirdB(false);
                }
            }
        });
        if (null != bitmap) ((ImageView) dialog.findViewById(R.id.frame)).setImageBitmap(bitmap);
        ((RadioGroup) dialog.findViewById(R.id.ep3_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    private void gotoFourth() {
        dialog.setContentView(R.layout.ep3_dialog4);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
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
                gotoFifth();
            }
        });
        dialog.findViewById(R.id.ep3_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void gotoFifth() {
        dialog.setContentView(R.layout.ep3_dialog5);
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
                gotoSixth();
            }
        });
        dialog.findViewById(R.id.go_to_web_instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent("https://www.instagram.com/explore/tags/runTLP/");
            }
        });
    }

    private void gotoSixth() {
        dialog.setContentView(R.layout.ep3_dialog6);
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
                gotoFifth();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoFifth();
                }
                return true;
            }
        });
        next.setEnabled(true);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestion = ((EditText) dialog.findViewById(R.id.ep3_suggestion_music)).getText().toString();
                postSongTwo(suggestion);
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.run_playlist));
            }
        });
    }

    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }

    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
    protected void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EP3Activity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EP3Activity.this);
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
                            bitmap = ImageUtils.toGrayscale(original);
                            ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                            String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                            int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                            int rotationAngle = 0;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                            Matrix matrix = new Matrix();
                            matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            bitmap = rotatedBitmap;
                        } catch (Exception e) {}
                        gotoSecond(true);
                    }
                    break;
                case 89:
                    mImageCaptureUri = data.getData();
                    if (null != dialog) dialog.dismiss();
                    try {
                        Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        bitmap = ImageUtils.toGrayscale(original);
                        ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                        int rotationAngle = 0;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap = rotatedBitmap;
                    } catch (Exception e) {}
                    gotoSecond(true);
                    break;
                case 79:
                    if (null != mImageCaptureUri) {
                        if (null != dialog) dialog.dismiss();
                        try {
                            Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                            bitmap = ImageUtils.toGrayscale(original);
                            ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                            String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                            int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                            int rotationAngle = 0;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                            if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                            Matrix matrix = new Matrix();
                            matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            bitmap = rotatedBitmap;
                        } catch (Exception e) {}
                        gotoThirdB(true);
                    }
                    break;
                case 69:
                    mImageCaptureUri = data.getData();
                    if (null != dialog) dialog.dismiss();
                    try {
                        Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                        bitmap = ImageUtils.toGrayscale(original);
                        ExifInterface exifInterface = new ExifInterface(mImageCaptureUri.getPath());
                        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orient = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                        int rotationAngle = 0;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                        if (orient == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap = rotatedBitmap;
                    } catch (Exception e) {}
                    gotoThirdB(true);
                    break;
            }
        }
    }

    private void postSongTwo(String songTwo) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP3_songTwo(EngagementService.EP3_SONG_TWO, songTwo);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }
//    private void postSongOne(String songOne) {
//        Call<EngagementResponse> call = ApiRequest.getInstance()
//                .getService().postEP2_songOne(EngagementService.EP2_SONG_ONE, songOne);
//        call.enqueue(new Callback<EngagementResponse>() {
//            @Override
//            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}
//
//            @Override
//            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
//        });
//    }
}
