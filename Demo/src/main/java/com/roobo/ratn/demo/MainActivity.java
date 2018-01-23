package com.roobo.ratn.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.tts.RTTSPlayer;

/**
 * Created by lixiang on 18-1-10.
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private VUIApi.VUIType vuiType;
    private RTTSPlayer.TTSType ttsType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bt_asr).setOnClickListener(this);
        findViewById(R.id.bt_semantic).setOnClickListener(this);
        findViewById(R.id.bt_tts).setOnClickListener(this);
        findViewById(R.id.bt_release).setOnClickListener(this);

        vuiType = (VUIApi.VUIType) getIntent().getSerializableExtra(ASRActivity.EXTRA_VUI_TYPE);
        ttsType = (RTTSPlayer.TTSType) getIntent().getSerializableExtra(TTSActivity.EXTRA_TTS_TYPE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_asr:
                Intent intent = new Intent(getApplicationContext(), ASRActivity.class);
                intent.putExtra(ASRActivity.EXTRA_VUI_TYPE, vuiType);
                startActivity(intent);
                break;
            case R.id.bt_semantic:
                startActivity(new Intent(getApplicationContext(), SemanticActivity.class));
                break;
            case R.id.bt_tts:
                Intent intent1 = new Intent(getApplicationContext(), TTSActivity.class);
                intent1.putExtra(TTSActivity.EXTRA_TTS_TYPE, ttsType);
                startActivity(intent1);
                break;
            case R.id.bt_release:
                VUIApi.getInstance().release();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
