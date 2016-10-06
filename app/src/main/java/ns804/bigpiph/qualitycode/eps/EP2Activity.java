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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EP2Activity extends AppCompatActivity {

    public static final String TAG = "EP2_GetLikeMe";
    private Dialog dialog;
    View back, next;
    private String txtOne = "";
    private String txtTwo = "";
    private String txtThree = "";
    private Uri mImageCaptureUri;
    private Bitmap bitmap;
    private boolean good = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep2);
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
        dialog = new Dialog(EP2Activity.this, R.style.Engagement);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gotoFirst();
    }

    private void share() {

        File file = new File(getApplicationContext().getCacheDir(), "toShare001" + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            file.setReadable(true, false);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } catch (Exception e) {}

    }

    private void share2() {
        Runnable shareRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("sharing", "begin");
                File file = new File(getApplicationContext().getCacheDir(), "toShare001" + ".png");
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

    private void gotoFirst() {
        dialog.setContentView(R.layout.ep2_dialog1);
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
                gotoSecond();
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep1_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
            }
        });
    }

    private void gotoSecond() {
        dialog.setContentView(R.layout.ep2_dialog2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(false);
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
                txtOne = ((EditText) dialog.findViewById(R.id.ep2_trait1)).getText().toString();
                txtTwo = ((EditText) dialog.findViewById(R.id.ep2_trait2)).getText().toString();
                txtThree = ((EditText) dialog.findViewById(R.id.ep2_trait3)).getText().toString();
                dialog.dismiss();
                gotoThird(false);
            }
        });

        final EditText editText1 = (EditText) dialog.findViewById(R.id.ep2_trait1);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.ep2_trait2);
        final EditText editText3 = (EditText) dialog.findViewById(R.id.ep2_trait3);

        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (!editText1.getText().toString().equals("") &&
                            !editText2.getText().toString().equals("")) {
                        next.setEnabled(true);
                    }
                } else {
                    next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void gotoThird(final boolean picTaken) {
        dialog.setContentView(R.layout.ep2_dialog3);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(false);
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
                gotoFourth();
            }
        });
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        if (picTaken) {
            if (null != bitmap) {
                dialog.findViewById(R.id.photo_content).setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.photo_content);
                imageView.setImageBitmap(bitmap);
                dialog.findViewById(R.id.camera).setVisibility(View.GONE);
            }
        }

        ((RadioGroup) dialog.findViewById(R.id.ep2_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, int checkedId) {
                if (picTaken) {
                    next.setEnabled(true);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(EP2Activity.this)
                            .setTitle("You must take a photo first")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });
    }

    private void gotoFourth() {
        dialog.setContentView(R.layout.ep2_dialog4);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(false);

        TextView textView1 = (TextView) dialog.findViewById(R.id.trait1);
        TextView textView2 = (TextView) dialog.findViewById(R.id.trait2);
        TextView textView3 = (TextView) dialog.findViewById(R.id.trait3);
        textView1.setText(txtOne);
        textView2.setText(txtTwo);
        textView3.setText(txtThree);

        if (bitmap != null) ((ImageView) dialog.findViewById(R.id.photo_content)).setImageBitmap(bitmap);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoThird(false);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    gotoThird(false);
                }
                return true;
            }
        });
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
                    gotoFifth();
                } else {
                    gotoSixth(false);
                }
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep2_rg3)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    private void gotoFifth() {
        dialog.setContentView(R.layout.ep2_dialog5);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(true);
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
                gotoFinal();
            }
        });
        dialog.findViewById(R.id.ep2_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share2();
            }
        });
    }

    private void gotoSixth(boolean picTaken) {
        dialog.setContentView(R.layout.ep2_dialog6);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        final EditText editText1 = (EditText) dialog.findViewById(R.id.ep2_trait1);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.ep2_trait2);
        final EditText editText3 = (EditText) dialog.findViewById(R.id.ep2_trait3);
        back.setEnabled(true);
        next.setEnabled(false);
        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOne = editText1.getText().toString();
                txtTwo = editText2.getText().toString();
                txtThree = editText3.getText().toString();
                showImageDialog2();
            }
        });
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
        if (picTaken) {
            dialog.findViewById(R.id.camera).setVisibility(View.GONE);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.photo_content);
            if (null != bitmap) imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            next.setEnabled(true);

            if (null != txtOne) editText1.setText(txtOne);
            if (null != txtTwo) editText2.setText(txtTwo);
            if (null != txtThree) editText3.setText(txtThree);
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
                txtOne = editText1.getText().toString();
                txtTwo = editText2.getText().toString();
                txtThree = editText3.getText().toString();
                dialog.dismiss();
                gotoFourth();
            }
        });
        ((EditText) dialog.findViewById(R.id.ep2_trait3)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (!((EditText) dialog.findViewById(R.id.ep2_trait1)).getText().toString().equals("") &&
                            !((EditText) dialog.findViewById(R.id.ep2_trait2)).getText().toString().equals("")) {
                        next.setEnabled(true);
                    }
                } else {
                    next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void gotoFinal() {
        dialog.setContentView(R.layout.ep2_final);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(true);
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSongOne(((EditText) dialog.findViewById(R.id.ep2_suggestion_music)).getText().toString());
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {}
                        return true;
                    }
                });
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.get_like_me_playlist));
            }
        });
        ((EditText) dialog.findViewById(R.id.ep2_suggestion_music)).addTextChangedListener(new TextWatcher() {
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

       //  postMemeImage(mImageCaptureUri);
    }

    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }

    protected void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EP2Activity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EP2Activity.this);
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
                        gotoThird(true);
                    }
                        // TODO: uploadAvatar(true);
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
                    gotoThird(true);
                    // TODO: uploadCover(false);
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
                        gotoSixth(true);
                    }
                    // TODO: uploadAvatar(true);
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
                    gotoSixth(true);
                    // TODO: uploadCover(false);
                    break;
                default:
                    break;
            }
        }
    }

    private void postMemeImage(Uri imageUri) {
        Log.d(TAG, "postingImage");
        String path = MediaUtils.getPath(getBaseContext(), imageUri);
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP2_memeImage(body);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {
                Log.d(TAG, "response");
            }

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void postSongOne(String songOne) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP2_songOne(EngagementService.EP2_SONG_ONE, songOne);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }
}
