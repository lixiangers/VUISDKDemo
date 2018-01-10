package com.roobo.ratn.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lixiang on 18-1-10.
 */

public class SemanticActivity extends Activity implements View.OnClickListener {

    private EditText etQuery;
    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semantic);

        etQuery = (EditText) findViewById(R.id.ed_query);
        tvResult = (TextView) findViewById(R.id.tv_result);
        findViewById(R.id.bt_semantic).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_semantic:
                // TODO: 18-1-10 text 语义查询
                break;
        }
    }
}
