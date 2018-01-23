package com.roobo.ratn.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.tts.RTTSPlayer;
import com.roobo.vui.api.tts.RTTSListener;


/**
 * Created by NOTE-026 on 2016/12/15.
 */
public class TTSActivity extends Activity implements RTTSListener {

    private static final String TAG = TTSActivity.class.getSimpleName();
    public static final String EXTRA_TTS_TYPE = "EXTRA_TTS_TYPE";

    private EditText mTts;
    private Spinner spinner;
    private RTTSPlayer.TTSType ttsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts);
        mTts = (EditText) findViewById(R.id.tts);
        Button ttsBtn = (Button) findViewById(R.id.tts_btn);
        spinner = (Spinner) findViewById(R.id.spinner);

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

        ttsType = (RTTSPlayer.TTSType) getIntent().getSerializableExtra(EXTRA_TTS_TYPE);
        String[] mItems = null;
        if (ttsType == RTTSPlayer.TTSType.TYPE_ONLINE)
            mItems = getResources().getStringArray(R.array.tts_language);
        else
            mItems = getResources().getStringArray(R.array.tts_speaker);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);

        final String[] finalMItems = mItems;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String mItem = finalMItems[pos];
                VUIApi.getInstance().setSpeaker(mItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
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
