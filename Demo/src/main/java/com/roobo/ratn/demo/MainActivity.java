package com.roobo.ratn.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.roobo.ratn.demo.mic.MicParam;
import com.roobo.ratn.demo.mic.MicType;
import com.roobo.ratn.demo.source.CustomAndroidAudioGenerator;
import com.roobo.ratn.demo.source.CustomSSEAudioGenerator;
import com.roobo.toolkit.RError;
import com.roobo.toolkit.recognizer.OnAIResponseListener;
import com.roobo.toolkit.recognizer.RooboRecognizer;
import com.roobo.vui.api.AutoTypeController;
import com.roobo.vui.api.IASRController;
import com.roobo.vui.api.InitListener;
import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.asr.RASRListener;
import com.roobo.vui.api.tts.BasePlayer;
import com.roobo.vui.common.recognizer.ASRResult;
import com.roobo.vui.common.recognizer.EventType;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    private RadioGroup rgMic, rgRec, mTTSType;
    private Button init, start, stop, touch_start, mTTSBtn, manualWakeup, sleep, pseudoSleep, cancelPseudoSleep;
    private View autoController;
    VUIApi mVUI = null;
    final Handler handler = new Handler();
    private BasePlayer.TTSType ttsType = BasePlayer.TTSType.TYPE_ONLINE;
    private IASRController mASRController;
    private VUIApi.VUIType vuiType;
    private Button btGrammar;
    private RadioButton rbStandard;
    private RadioButton rb8009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createView();
        setViewListener();
        resetButton();
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
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        touch_start = (Button) findViewById(R.id.touch_start);
        mTTSBtn = (Button) findViewById(R.id.tts);
        mTTSType = (RadioGroup) findViewById(R.id.rg_tts);
        autoController = findViewById(R.id.autoController);
        manualWakeup = (Button) findViewById(R.id.manualWakeup);
        sleep = (Button) findViewById(R.id.sleep);
        pseudoSleep = (Button) findViewById(R.id.pseudoSleep);
        cancelPseudoSleep = (Button) findViewById(R.id.cancelPseudoSleep);
        btGrammar = (Button) findViewById(R.id.bt_add_grammar);
    }

    private void setViewListener() {
        init.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        mTTSBtn.setOnClickListener(this);
        manualWakeup.setOnClickListener(this);
        sleep.setOnClickListener(this);
        pseudoSleep.setOnClickListener(this);
        cancelPseudoSleep.setOnClickListener(this);
        btGrammar.setOnClickListener(this);
        touch_start.setOnTouchListener(touchListener);
        mTTSType.setOnCheckedChangeListener(ttsChangedListener);
        rgMic.setOnCheckedChangeListener(onCheckedChangeListener);
        rgRec.setOnCheckedChangeListener(onCheckedChangeListener);
    }


    RadioGroup.OnCheckedChangeListener ttsChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            init.setVisibility(View.VISIBLE);
            resetButton();
            if (i == R.id.rb_online) {
                ttsType = BasePlayer.TTSType.TYPE_ONLINE;
            } else {
                ttsType = BasePlayer.TTSType.TYPE_OFFLINE;
            }
        }
    };

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            init.setVisibility(View.VISIBLE);
            resetButton();
        }
    };

    private void resetButton() {
        start.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);

        touch_start.setVisibility(View.GONE);
        mTTSBtn.setVisibility(View.GONE);
        autoController.setVisibility(View.GONE);
        btGrammar.setVisibility(View.GONE);
    }


    private void init() {
        init.setVisibility(View.GONE);
        resetButton();
        if (mVUI != null) {
            mVUI.release();
        } else {
            mVUI = mVUI.getInstance();
        }
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

        VUIApi.InitParam.InitParamBuilder builder = new VUIApi.InitParam.InitParamBuilder();
        builder.setLanguage(RooboRecognizer.ASRLanguage.ZH).
                setUseSSE(useSSE).
                addOfflineFileName(wakeupGrammarFile).
                setTTSType(ttsType).
                setVUIType(vuiType);
        //如果使用SSE，需要设置麦克风数量，参考信号数量
        if (useSSE)
            builder.setSseMicCount(MicParam.MIC_COUNT).
                    setSseRefCount(MicParam.REF_COUNT).
                    setBsdInSSE("sse321_6mic_1ref_bf_asl_aec_sdr_18nr_39mm.bsd");

        mVUI.init(MainActivity.this, builder.build(), useSSE ? new CustomSSEAudioGenerator() : new CustomAndroidAudioGenerator(),
                new InitListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSucess: called");
                        setListener();
                        reprotLocation();
                        if (VUIApi.VUIType.MANUAL == vuiType) {
                            touch_start.setVisibility(View.VISIBLE);
                            mTTSBtn.setVisibility(View.VISIBLE);
                        } else {
                            start.setVisibility(View.VISIBLE);
                            stop.setVisibility(View.VISIBLE);
                            mTTSBtn.setVisibility(View.VISIBLE);
                            btGrammar.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(RError rError) {
                        Log.d(TAG, "onFail: " + rError.getFailDetail());
                    }
                });
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //按下
                    mVUI.startRecognize();
                    v.setBackgroundColor(Color.RED);
                    break;
                case MotionEvent.ACTION_UP:
                    //抬起
                    mVUI.stopRecognize();
                    v.setBackgroundColor(Color.GREEN);
                    break;

            }
            return true;
        }
    };

    private void setListener() {
        mVUI.setASRListener(new RASRListener() {
            @Override
            public void onASRResult(final ASRResult result) {
                Log.d(TAG, "ASRResult " + (result.getResultType() == ASRResult.TYPE_OFFLINE ? "offline " : " online ") + " text " + result.getResultText());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView viewById = (TextView) findViewById(R.id.text);
                        viewById.setText(result.getResultText());
                    }
                });
            }

            @Override
            public void onFail(final RError message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView viewById = (TextView) findViewById(R.id.text);
                        viewById.setText("asr error " + message.getFailCode() + " " + message.getFailDetail());
                    }
                });
            }

            @Override
            public void onWakeUp(final String json) {
                Log.d(TAG, "onWakeup: " + json);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView viewById = (TextView) findViewById(R.id.text);
                        viewById.setText(json);
                        mVUI.stopSpeak();
                    }
                });
            }

            @Override
            public void onEvent(EventType event) {
                Log.d(TAG, "EventType: " + event);
            }
        });


        mVUI.setOnAIResponseListener(new OnAIResponseListener() {
            @Override
            public void onResult(final String json) {
                Log.d(TAG, "onAIResponse: " + json);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView viewById = (TextView) findViewById(R.id.text);
                        viewById.setText(json);
                    }
                });
            }

            @Override
            public void onFail(final RError rError) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView viewById = (TextView) findViewById(R.id.text);
                        viewById.setText("AI error " + rError.getFailCode() + " " + rError.getFailDetail());
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    private void reprotLocation() {
//        WifiManager wifiManager = (WifiManager) getApplication().getSystemService(Context.WIFI_SERVICE);
//        mVUI.reportLocationInfo(wifiManager.getScanResults());

//        mVUI.reportLocationInfo("22.5375738","113.9568349","中国","广东省","深圳市","广东省 深圳市 南山区 科技南十二路 靠近交通银行(深圳高新园支行)");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                mASRController = mVUI.startRecognize();
                if (vuiType == VUIApi.VUIType.AUTO) {
                    autoController.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tts:
                startActivity(new Intent(getApplicationContext(), TTSActivity.class));
                break;
            case R.id.stop:
                mVUI.stopRecognize();
                break;
            case R.id.init:
                init();
                break;
            case R.id.manualWakeup:
                ((AutoTypeController) mASRController).manualWakeup(AutoTypeController.CURRENT_ANGLE);
                break;
            case R.id.sleep:
                ((AutoTypeController) mASRController).sleep();
                break;
            case R.id.pseudoSleep:
                ((AutoTypeController) mASRController).pseudoSleep();
                break;
            case R.id.cancelPseudoSleep:
                ((AutoTypeController) mASRController).cancelPseudoSleep();
                break;
            case R.id.bt_add_grammar:
                startActivity(new Intent(getApplicationContext(), GrammarActivity.class));
                break;
            default:
                break;
        }
    }
}
