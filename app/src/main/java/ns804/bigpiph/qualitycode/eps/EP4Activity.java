package ns804.bigpiph.qualitycode.eps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EP4Activity extends AppCompatActivity {

    public static final String TAG = "EP4_TooLate";
    private Dialog dialog;
    View back, next;
    private static final int CHOICE_PAY_FORWARD = 0;
    private static final int CHOICE_COMPLIMENTS = 1;
    private static final int CHOICE_CHANGE = 2;
    private String selection;
    private static final String FORWARD = "pay it forward";
    private static final String COMPLIMENTS = "compliments";
    private static final String CHANGE = "change";
    private int choice;
    private String suggestion;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep4);
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
        dialog = new Dialog(EP4Activity.this, R.style.Engagement);
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
        dialog.setContentView(R.layout.ep4_dialog1);
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
    }

    private void gotoSecond() {
        dialog.setContentView(R.layout.ep4_dialog2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        ((RadioGroup) dialog.findViewById(R.id.ep4_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                switch (checkedId) {
                    case R.id.rb1:
                        choice = CHOICE_PAY_FORWARD;
                        selection = FORWARD;
                        break;
                    case R.id.rb2:
                        choice = CHOICE_COMPLIMENTS;
                        selection = COMPLIMENTS;
                        break;
                    case R.id.rb3:
                        choice = CHOICE_CHANGE;
                        selection = CHANGE;
                        break;
                }
            }
        });
    }

    private void gotoThird() {
        dialog.setContentView(R.layout.ep4_dialog3);
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
                gotoFourth();
            }
        });
        TextView textView = (TextView) dialog.findViewById(R.id.content_textfield);
        switch (choice) {
            case CHOICE_PAY_FORWARD:
                textView.setText(getString(R.string.ep4_6));
                break;
            case CHOICE_COMPLIMENTS:
                textView.setText(getString(R.string.ep4_7));
                break;
            case CHOICE_CHANGE:
                textView.setText(getString(R.string.ep4_8));
                break;
        }
        final TextView counter = (TextView) dialog.findViewById(R.id.counter);
        CountDownTimer timer = new CountDownTimer(86400000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter.setText(formatTimer(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                counter.setText("done");
            }
        }.start();
    }

    private void gotoFourth() {
        dialog.setContentView(R.layout.ep4_dialog4);
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
                suggestion = ((EditText) dialog.findViewById(R.id.ep4_suggestion_music)).getText().toString();
                postSongThree(suggestion);
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.too_late_playlist));
            }
        });

        postSelection(selection);
    }

    private String formatTimer(long millies) {
        StringBuilder builder = new StringBuilder();
        int hours = (int) millies / 1000 / 3600;
        int remainder = (int) millies / 1000 - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        builder.append(toD(hours));
        builder.append(":");
        builder.append(toD(minutes));
        builder.append(":");
        builder.append(toD(seconds));
        return builder.toString();
    }
    private String toD(int i) {
        String result = "00";
        if (i == 0) {
            result = "00";
        } else if (i < 10) {
            result = "0" + String.valueOf(i);
        } else {
            result = String.valueOf(i);
        }
        return result;
    }

    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }

    private void postSongThree(String songThree) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP4_songThree(EngagementService.EP4_SONG_THREE, songThree);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }
    private void postSelection(String selection) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP4_tooLateSelection(EngagementService.EP4_TOO_LATE_SELECTION, selection);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }
}
