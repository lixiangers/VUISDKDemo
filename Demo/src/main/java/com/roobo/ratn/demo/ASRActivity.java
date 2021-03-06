package com.roobo.ratn.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.roobo.ratn.demo.util.JsonUtil;
import com.roobo.toolkit.RError;
import com.roobo.toolkit.recognizer.OnAIResponseListener;
import com.roobo.vui.api.AutoTypeController;
import com.roobo.vui.api.VUIApi;
import com.roobo.vui.api.asr.RASRListener;
import com.roobo.vui.common.recognizer.ASRResult;
import com.roobo.vui.common.recognizer.EventType;

/**
 * Created by lixiang on 18-1-10.
 */

public class ASRActivity extends Activity implements View.OnClickListener {

    public static final String EXTRA_VUI_TYPE = "EXTRA_VUI_TYPE";
    public static final String TAG = "ASRActivity";
    private AutoTypeController iasrController;
    private Handler handler;
    private TextView tvResult;
    private Spinner spinner;
    private Button btnRecord;
    private boolean isRecroding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asr);
        handler = new Handler(Looper.getMainLooper());

        VUIApi.VUIType vuiType = (VUIApi.VUIType) getIntent().getSerializableExtra(EXTRA_VUI_TYPE);

        View viewAutoType = findViewById(R.id.view_auto);
        View viewManualType = findViewById(R.id.touch_start);
        tvResult = (TextView) findViewById(R.id.tv_result);
        spinner = (Spinner) findViewById(R.id.spinner);

        btnRecord = (Button) findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(this);
        findViewById(R.id.sleep).setOnClickListener(this);
        findViewById(R.id.manualWakeup).setOnClickListener(this);
        findViewById(R.id.pseudoSleep).setOnClickListener(this);
        findViewById(R.id.cancelPseudoSleep).setOnClickListener(this);
        findViewById(R.id.bt_add_grammar).setOnClickListener(this);
        viewManualType.setOnTouchListener(touchListener);

        if (VUIApi.VUIType.AUTO == vuiType) {
            viewAutoType.setVisibility(View.VISIBLE);
            viewManualType.setVisibility(View.GONE);
        } else {
            viewAutoType.setVisibility(View.GONE);
            viewManualType.setVisibility(View.VISIBLE);
        }

        updateRecordText();

        setListener();
    }

    private void updateRecordText() {
        if (isRecroding) {
            btnRecord.setTag("停止录音");
        } else
            btnRecord.setTag("开始录音");
    }

    private void setListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.asr_language);
                String language = languages[pos];
                VUIApi.getInstance().setCloudRecognizeLang(language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        VUIApi.getInstance().setASRListener(new RASRListener() {
            @Override
            public void onASRResult(final ASRResult result) {
                Log.d(TAG, "ASRResult " + (result.getResultType() == ASRResult.TYPE_OFFLINE ? "offline " : " online ") + " text " + result.getResultText());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(result.getResultText());
                    }
                });
            }

            @Override
            public void onFail(final RError message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("asr error " + message.getFailCode() + " " + message.getFailDetail());
                    }
                });
            }

            @Override
            public void onWakeUp(final String json) {
                Log.d(TAG, "onWakeup: " + json);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(json);
                        VUIApi.getInstance().stopSpeak();
                    }
                });
            }

            @Override
            public void onEvent(EventType event) {
                Log.d(TAG, "EventType: " + event);
            }
        });


        VUIApi.getInstance().setOnAIResponseListener(new OnAIResponseListener() {
            @Override
            public void onResult(final String json) {
                Log.d(TAG, "onAIResponse: " + json);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(json);
                    }
                });

                AIResponse aiResponse = JsonUtil.getObject(json, AIResponse.class);
                if (null == aiResponse) return;
                String context = aiResponse.getoutputContext();
                if (!TextUtils.isEmpty(context))
                    //设置AI上下文
                    VUIApi.getInstance().setAIContext(context);
            }

            @Override
            public void onFail(final RError rError) {
                Log.d(TAG, "onAIResponse: onFail" + rError.getFailDetail());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("AI error " + rError.getFailCode() + " " + rError.getFailDetail());
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_record:
                if (isRecroding)
                    VUIApi.getInstance().stopRecognize();
                else
                    iasrController = (AutoTypeController) VUIApi.getInstance().startRecognize();

                isRecroding = !isRecroding;
                updateRecordText();
                break;
            case R.id.sleep:
                iasrController.sleep();
                break;
            case R.id.manualWakeup:
                iasrController.manualWakeup(0);
                break;
            case R.id.pseudoSleep:
                iasrController.pseudoSleep();
                break;
            case R.id.cancelPseudoSleep:
                iasrController.cancelPseudoSleep();
                break;
            case R.id.bt_add_grammar:
                startActivity(new Intent(getApplicationContext(), GrammarActivity.class));
                break;
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //按下
                    VUIApi.getInstance().startRecognize();
                    v.setBackgroundColor(Color.RED);
                    break;
                case MotionEvent.ACTION_UP:
                    //抬起
                    VUIApi.getInstance().stopRecognize();
                    v.setBackgroundColor(Color.GREEN);
                    break;

            }
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VUIApi.getInstance().stopRecognize();
    }
}
