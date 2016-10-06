package ns804.bigpiph.qualitycode.eps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import ns804.bigpiph.R;
import ns804.bigpiph.qualitycode.api.ApiRequest;
import ns804.bigpiph.qualitycode.api.EngagementService;
import ns804.bigpiph.qualitycode.api.response.EngagementResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EP7Activity extends AppCompatActivity {

    public static final String TAG = "EP7_YouGottaKnow";
    private Dialog dialog;
    View back, next;
    private static final int CHOICE_DEEP = 32;
    private static final int CHOICE_MID = 33;
    private static final int CHOICE_KICKIN = 34;
    private String selection;
    private static final String DEEP = "deep";
    private static final String MID = "mid";
    private static final String KICKIN = "kickin";
    private int choice;
    private String url = "";
    private String suggestion;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep7);
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
        dialog = new Dialog(EP7Activity.this, R.style.Engagement);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gotoFirst();
    }

    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private void gotoFirst() {
        dialog.setContentView(R.layout.ep6_dialog1);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
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
                gotoSecond();
            }
        });
        ((TextView) dialog.findViewById(R.id.text_view)).setText(getString(R.string.ep7_1));
    }

    private void gotoSecond() {
        dialog.setContentView(R.layout.ep6_dialog1);
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
                gotoThird();
            }
        });
        ((TextView) dialog.findViewById(R.id.text_view)).setText(getString(R.string.ep7_2));
    }

    private void gotoThird() {
        dialog.setContentView(R.layout.ep7_dialog3);
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
                gotoFourth();
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep7_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        choice = CHOICE_DEEP;
                        selection = DEEP;
                        break;
                    case R.id.rb2:
                        choice = CHOICE_MID;
                        selection = MID;
                        break;
                    case R.id.rb3:
                        choice = CHOICE_KICKIN;
                        selection = KICKIN;
                        break;
                }
            }
        });
    }

    private void gotoFourth() {
        dialog.setContentView(R.layout.ep7_dialog4);
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
        TextView textView = (TextView) dialog.findViewById(R.id.text_view);
        switch (choice) {
            case CHOICE_DEEP:
                textView.setText(getString(R.string.ep7_6));
                url = getString(R.string.ep7_11);
                break;
            case CHOICE_MID:
                textView.setText(getString(R.string.ep7_7));
                url = getString(R.string.ep7_12);
                break;
            case CHOICE_KICKIN:
                textView.setText(getString(R.string.ep7_8));
                url = getString(R.string.ep7_13);
                break;
        }
        dialog.findViewById(R.id.go_to_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(url);
            }
        });

        postSelection(selection);
    }

    private void gotoFifth() {
        dialog.setContentView(R.layout.ep7_final);
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
                suggestion = ((EditText) dialog.findViewById(R.id.ep7_suggestion_music)).getText().toString();
                postSongSix(suggestion);
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.gotta_know_playlist));
            }
        });
        ((EditText) dialog.findViewById(R.id.ep7_suggestion_music)).addTextChangedListener(new TextWatcher() {
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

    private void postSongSix(String songSix) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP7_songSix(EngagementService.EP7_SONG_SIX, songSix);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }
    private void postSelection(String selection) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP7_gottaKnowSelection(EngagementService.EP7_YOU_GOTTA_KNOW_SELECTION, selection);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }

}
