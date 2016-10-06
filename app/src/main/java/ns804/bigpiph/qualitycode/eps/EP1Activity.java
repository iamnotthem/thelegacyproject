package ns804.bigpiph.qualitycode.eps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class EP1Activity extends AppCompatActivity {

    public static final String TAG = "EP1_ItBegins";
    private Dialog dialog;
    View back, next;
    private String userName = "";
    private String name = "";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep1);
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
    }

    @Override public void onResume() {
        super.onResume();
        dialog = new Dialog(EP1Activity.this, R.style.Engagement);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gotoFirst();
    }

    private void gotoFirst() {
        dialog.setContentView(R.layout.ep1_dialog1);
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
        ((RadioGroup) dialog.findViewById(R.id.ep1_rg1)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                if (checkedId == R.id.rb1) name = "Lisa";
                else if (checkedId == R.id.rb2) name = "Marcus";
                else if (checkedId == R.id.rb3) name = "Taylor";
            }
        });
    }

    private void gotoSecond() {
        dialog.setContentView(R.layout.ep1_dialog2);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        back.setEnabled(true);
        next.setEnabled(false);
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
        ((RadioGroup) dialog.findViewById(R.id.ep1_rg2)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
                if (checkedId == R.id.rb1) {
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {}
                            return true;
                        }
                    });
                    next.setOnClickListener(null);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        dialog.dismiss();
                        goToFourth();
                        }
                    });
                } else if (checkedId == R.id.rb2){
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {}
                            return true;
                        }
                    });
                    next.setOnClickListener(null);
                    next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        goToThird();
                    }
                });
                }
            }
        });
        postHostName(name);
    }

    private void goToThird() {
        dialog.setContentView(R.layout.ep1_dialog3);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.special)).setText(String.format(getString(R.string.ep1_12), name));
        back.setEnabled(true);
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
                goToFourth();
            }
        });
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
        ((RadioGroup) dialog.findViewById(R.id.ep1_rg3)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                next.setEnabled(true);
            }
        });
    }

    private void goToFourth() {
        dialog.setContentView(R.layout.ep1_dialog4);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();
        final EditText nameEdit = (EditText) dialog.findViewById(R.id.ep1_name);
        back.setEnabled(true);
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
                userName = nameEdit.getText().toString();
                dialog.dismiss();
                gotoFinal();
            }
        });
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
        nameEdit.addTextChangedListener(nameWatcher);
    }

    private void gotoFinal() {
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {}
                return true;
            }
        });
        ((TextView) findViewById(R.id.activity_ep1_name)).setText(userName);
        findViewById(R.id.opacity_layer).setVisibility(View.GONE);
        findViewById(R.id.activity_ep1_finale).setVisibility(View.VISIBLE);
        findViewById(R.id.ep1_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        findViewById(R.id.next_back).setVisibility(View.GONE);
        findViewById(R.id.cancel).setVisibility(View.GONE);
        postUserName(userName);
    }

    private void exit() { finish(); }

    private void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out Big Piph");
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    private void postHostName(String hostName) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP1_hostName(EngagementService.EP1_HOST_NAME, hostName);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }

    private void postUserName(String userName) {
        Call<EngagementResponse> call = ApiRequest.getInstance()
                .getService().postEP1_userName(EngagementService.EP1_USER_NAME, userName);
        call.enqueue(new Callback<EngagementResponse>() {
            @Override
            public void onResponse(Call<EngagementResponse> call, Response<EngagementResponse> response) {}

            @Override
            public void onFailure(Call<EngagementResponse> call, Throwable t) {}
        });
    }

    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) next.setEnabled(false);
            else next.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {
            //
        }
    };
}