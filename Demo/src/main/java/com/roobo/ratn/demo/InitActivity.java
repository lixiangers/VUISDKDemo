package com.roobo.ratn.demo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.roobo.ratn.demo.mic.MicParam;
import com.roobo.ratn.demo.mic.MicType;
import com.roobo.ratn.demo.source.CustomAndroidAudioGenerator;
import com.roobo.ratn.demo.source.CustomSSEAudioGenerator;
import com.roobo.toolkit.RError;
import com.roobo.toolkit.recognizer.RooboRecognizer;
import com.roobo.vui.api.InitListener;
import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.tts.RTTSPlayer;


public class InitActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "InitActivity";
    private RadioGroup rgMic, rgRec, rgTTSType;
    private Button init;
    private RadioButton rbStandard, rb8009;

    private RTTSPlayer.TTSType ttsType;
    private VUIApi.VUIType vuiType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        createView();
        setViewListener();
        initMicType();
    }

    /**
     * 根据Gradle中设置的Mic初始化Mic view
     */
    private void initMicType() {
        //根据当前MIC类型选择mic类型
        if (MicType.STANDARD == BuildConfig.MIC_TYPE) {
            rbStandard.setChecked(true);
            rb8009.setEnabled(false);
        } else {
            rb8009.setChecked(true);
            rbStandard.setEnabled(false);
        }
    }

    private void createView() {
        rgMic = (RadioGroup) findViewById(R.id.rg_mic);
        rgRec = (RadioGroup) findViewById(R.id.rg_rec);

        rbStandard = (RadioButton) findViewById(R.id.rb_android);
        rb8009 = (RadioButton) findViewById(R.id.rb_8009);

        init = (Button) findViewById(R.id.init);
        rgTTSType = (RadioGroup) findViewById(R.id.rg_tts);
    }

    private void setViewListener() {
        init.setOnClickListener(this);
    }

    private void init() {
        int checkedRadioButtonId = rgMic.getCheckedRadioButtonId();
        boolean useSSE = false;
        if (checkedRadioButtonId == R.id.rb_android) {
            useSSE = false;
        } else if (checkedRadioButtonId == R.id.rb_8009) {
            useSSE = true;
        }

        String wakeupGrammarFile = "test_offline";

        int recId = rgRec.getCheckedRadioButtonId();
        if (recId == R.id.rb_sustained_wakeup_cloud) { // 持续按唤醒
            vuiType = VUIApi.VUIType.AUTO;
        } else if (recId == R.id.rb_manual_vad) { // 手动控制vad
            vuiType = VUIApi.VUIType.MANUAL;
        }

        int ttsID = rgTTSType.getCheckedRadioButtonId();
        if (ttsID == R.id.rb_online) {
            ttsType = RTTSPlayer.TTSType.TYPE_ONLINE;
        } else if (ttsID == R.id.rb_offline) {
            ttsType = RTTSPlayer.TTSType.TYPE_OFFLINE;
        }

        VUIApi.InitParam.InitParamBuilder builder = new VUIApi.InitParam.InitParamBuilder();
        builder.setLanguage(RooboRecognizer.ASRLanguage.ZH).
                setUseSSE(useSSE).
                addOfflineFileName(wakeupGrammarFile).
                setTTSType(ttsType).
                setVUIType(vuiType);
        //如果使用SSE，需要设置麦克风数量，参考信号数量
        if (useSSE) {
            builder.setSseMicCount(MicParam.MIC_COUNT).
                    setSseRefCount(MicParam.REF_COUNT).
                    setBsdInSSE("sse321_6mic_1ref_bf_asl_aec_sdr_18nr_39mm.bsd").setAudioGenerator(new CustomSSEAudioGenerator());
        } else {
            builder.setAudioGenerator(new CustomAndroidAudioGenerator());
        }

        VUIApi.getInstance().init(InitActivity.this, builder.build(),
                new InitListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSucess: called");
                        reportLocation();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(ASRActivity.EXTRA_VUI_TYPE, vuiType);
                        intent.putExtra(TTSActivity.EXTRA_TTS_TYPE, ttsType);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(RError rError) {
                        Log.d(TAG, "onFail: " + rError.getFailDetail());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    private void reportLocation() {
        WifiManager wifiManager = (WifiManager) getApplication().getSystemService(Context.WIFI_SERVICE);
        VUIApi.getInstance().reportLocationInfo(wifiManager.getScanResults());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.init:
                init();
                break;
            default:
                break;
        }
    }
}
