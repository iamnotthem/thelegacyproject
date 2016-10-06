package ns804.bigpiph.qualitycode.eps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class EP5Activity extends AppCompatActivity {

    public static final String TAG = "EP5_TellEm";
    private Dialog dialog;
    View back, next;
    private String suggestion;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep5);
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
        dialog = new Dialog(EP5Activity.this, R.style.Engagement);
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
        dialog.setContentView(R.layout.ep5_dialog1);
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
                gotoSecond();
            }
        });
        ((RadioGroup) dialog.findViewById(R.id.ep5_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
            }
        });
    }

    private void gotoSecond() {
        dialog.setContentView(R.layout.ep5_dialog2);
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
    }

    private void gotoThird() {
        dialog.setContentView(R.layout.ep5_dialog3);
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
        dialog.findViewById(R.id.go_to_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent("http://bigpiph.com/tell-em-tlp-contest");
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
        ((RadioGroup) dialog.findViewById(R.id.ep5_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
            }
        });
    }

    private void gotoFourth() {
        dialog.setContentView(R.layout.ep5_dialog2);
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
        ((TextView) dialog.findViewById(R.id.text_view)).setText(getString(R.string.ep5_7));
    }

    private void gotoFifth() {
        dialog.setContentView(R.layout.ep5_dialog2);
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
                gotoFinal();
            }
        });
        ((TextView) dialog.findViewById(R.id.text_view)).setText(getString(R.string.ep5_8));
    }

    private void gotoFinal() {
        dialog.setContentView(R.layout.ep5_final);
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
                suggestion = ((EditText) dialog.findViewById(R.id.ep5_suggestion_music)).getText().toString();
                postSongFour(suggestion);
                dialog.dismiss();
                finish();
            }
        });
        dialog.findViewById(R.id.go_to_web_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webIntent(getString(R.string.tell_em_playlist));
            }
        });
    }


    private void webIntent(String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(url));
        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
    }

    private void postSongFour(String songFour) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP5_songFour(EngagementService.EP5_SONG_FOUR, songFour);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }

}
