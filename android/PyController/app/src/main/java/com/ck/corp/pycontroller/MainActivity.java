package com.ck.corp.pycontroller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private ImageButton voiceRecorderBtn;
    private TextView recordedViewer;
    private Button ipChangeBtn;
    private TextView ipAddressTxt;

    private LinearLayout voiceMode;
    private LinearLayout txtMode;
    private Button modeChanger;

    private EditText commandTxtForm;
    private Button commandTxtFormBtn;

    private String IP_Address = "192.168.1.101";
    private String TAG = "RequestData";


    private Boolean modeVoice = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        voiceRecorderBtn =  this.findViewById(R.id.voice_record);
        recordedViewer =  this.findViewById(R.id.recorded_viewer);

        ipChangeBtn = this.findViewById(R.id.ip_change);
        ipAddressTxt = this.findViewById(R.id.ip_add_txt);

        voiceMode = this.findViewById(R.id.voice_mode);
        txtMode = this.findViewById(R.id.text_mode);
        modeChanger = this.findViewById(R.id.mode_changer);

        commandTxtForm = this.findViewById(R.id.command_txt);
        commandTxtFormBtn = this.findViewById(R.id.command_set);

        onClick();
    }

    private void onClick() {
        voiceRecorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        ipChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP_Address = ipAddressTxt.getText().toString();
            }
        });

        modeChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeVoice) {
                    voiceMode.setVisibility(View.GONE);
                    txtMode.setVisibility(View.VISIBLE);
                    modeVoice = false;
                } else {
                    txtMode.setVisibility(View.GONE);
                    voiceMode.setVisibility(View.VISIBLE);
                    modeVoice = true;
                }
            }
        });

        commandTxtFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = commandTxtForm.getText().toString();
                postData(command);
                commandTxtForm.setText("");
            }
        });

    }


    private void performSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Command Plz");
        try {
            startActivityForResult(intent, 23);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Device Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 23: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> output = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    recordedViewer.setText(output.get(0));
                    postData(output.get(0));
                }
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
            performSearch();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void postData(String data) {
        Log.e(TAG, "Response . Requested one .");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://" + IP_Address + ":8000/post_data/" + data;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response ." + response + ".");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error " + error);
            }
        });
        queue.add(stringRequest);
    }

}