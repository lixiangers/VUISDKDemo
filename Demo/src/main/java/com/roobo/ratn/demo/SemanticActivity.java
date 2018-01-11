package com.roobo.ratn.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.roobo.toolkit.RError;
import com.roobo.toolkit.recognizer.OnAIResponseListener;
import com.roobo.vui.api.VUIApi;

/**
 * Created by lixiang on 18-1-10.
 */

public class SemanticActivity extends Activity implements View.OnClickListener, OnAIResponseListener {

    public static final String TAG = "SemanticActivity";
    private EditText etQuery;
    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semantic);

        etQuery = (EditText) findViewById(R.id.ed_query);
        tvResult = (TextView) findViewById(R.id.tv_result);
        findViewById(R.id.bt_semantic).setOnClickListener(this);

        VUIApi.getInstance().setOnAIResponseListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_semantic:
                String text = etQuery.getText().toString();
                if (TextUtils.isEmpty(text))
                    return;
                VUIApi.getInstance().aiQuery(text);
                break;
        }
    }

    @Override
    public void onResult(final String s) {
        Log.d(TAG, "onResult:"+s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResult.setText(s);
            }
        });
    }

    @Override
    public void onFail(final RError rError) {
        Log.d(TAG, "Error:" + rError.getFailDetail());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvResult.setText(rError.getFailDetail());
            }
        });
    }
}
