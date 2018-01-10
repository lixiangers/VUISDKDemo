package com.roobo.ratn.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.tts.RTTSListener;


/**
 * Created by NOTE-026 on 2016/12/15.
 */
public class TTSActivity extends Activity implements RTTSListener {

    private static final String TAG = TTSActivity.class.getSimpleName();

    private EditText mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts);
        mTts = (EditText) findViewById(R.id.tts);
        Button ttsBtn = (Button) findViewById(R.id.tts_btn);
        ttsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = mTts.getText().toString();
                VUIApi.getInstance().speak(p, TTSActivity.this);
                Toast.makeText(getApplicationContext(), "synthesis", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tts_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VUIApi.getInstance().stopSpeak();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSpeakBegin() {
        Log.d(TAG, "onSpeakBegin");
        Toast.makeText(getApplicationContext(), "speak", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted");
    }

    @Override
    public void onError(int code) {
        Log.d(TAG, "onError " + code);
    }
}
