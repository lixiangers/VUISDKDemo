package com.roobo.ratn.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roobo.ratn.demo.grammar.GrammarManager;
import com.roobo.toolkit.RError;
import com.roobo.vui.api.IOfflineRecognizerController;
import com.roobo.vui.api.VUIApi;
import com.roobo.vui.common.vocon.LoadGrammarListener;

import java.util.List;

/**
 * Created by lixiang on 18-1-8.
 * 只有采用　VUIApi.VUIType.AUTO方式才能添加离线Grammar。
 */

public class GrammarActivity extends Activity {

    private LayoutInflater inflater;
    private ListView listView;
    private MyArrayAdapter myArrayAdapter;
    private IOfflineRecognizerController offlineRecognizerController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        listView = (ListView) findViewById(R.id.list_view);
        inflater = LayoutInflater.from(getApplicationContext());


        myArrayAdapter = new MyArrayAdapter(getApplicationContext(), R.layout.view_grammar, GrammarManager.getInstance().getViewModels());
        listView.setAdapter(myArrayAdapter);

        loadGrammar();
    }

    private void loadGrammar() {
        //已经load　过就不再load
        offlineRecognizerController = VUIApi.getInstance().getOfflineRecognizerController();

        if (GrammarManager.getInstance().isLoaded() || null == offlineRecognizerController)
            return;

        offlineRecognizerController = VUIApi.getInstance().getOfflineRecognizerController();
        offlineRecognizerController.loadGrammars(GrammarManager.getInstance().getBnfGrammars(), new LoadGrammarListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "加载离线Grammar成功", Toast.LENGTH_SHORT).show();
                GrammarManager.getInstance().addViewModels();
            }

            @Override
            public void onFail(RError error) {
                Toast.makeText(getApplicationContext(), error.getFailDetail(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        myArrayAdapter.notifyDataSetChanged();
    }

    private class MyArrayAdapter extends ArrayAdapter<GrammarManager.GrammarViewModel> {
        private final int resourceId;

        MyArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(resourceId, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.cbEnable = (CheckBox) convertView.findViewById(R.id.ck_enable);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cbEnable.setOnCheckedChangeListener(null);

            final GrammarManager.GrammarViewModel item = getItem(position);
            holder.tvName.setText(item.getBnfGrammar().getName());
            holder.cbEnable.setChecked(item.isEnable());

            holder.cbEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        offlineRecognizerController.enableGrammar(item.getBnfGrammar().getName()); //enable　grammar
                        item.setEnable(true);
                        Log.d(InitActivity.TAG, "enableGrammar:"+item.getBnfGrammar().getName());
                    } else {
                        offlineRecognizerController.disableGrammar(item.getBnfGrammar().getName()); //disable grammar
                        item.setEnable(false);
                        Log.d(InitActivity.TAG, "disableGrammar:"+item.getBnfGrammar().getName());
                    }
                }
            });
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView tvName;
        CheckBox cbEnable;
    }
}