package com.roobo.ratn.demo.source;

import android.util.Log;

import com.roobo.media.AudioChunkRecord;
import com.roobo.priority.PriorityManager;
import com.roobo.ratn.demo.BuildConfig;
import com.roobo.ratn.demo.mic.MicParam;
import com.roobo.ratn.demo.mic.MicType;
import com.roobo.vui.api.audiosource.BaseAudioGenerator;

/**
 * Created by ChaoPei on 2017/12/15.
 * 演示多路的麦阵如何处理数据。
 * 这里演示的是基于8009平台+Roobo(6麦)麦阵的数据处理。
 * (数据的读取是在Jni层上处理的，如果不是使用Roobo的平台和麦阵，需要自己处理底层的音频数据读取)
 */
public class CustomSSEAudioGenerator implements BaseAudioGenerator {
    private static final String TAG = "CustomSSEAudioGenerator";

    private AudioChunkRecord mAudioChunkRecord;

    @Override
    public int getChannelCount() {
        return MicParam.VALID_CHANNELS.length;
    }

    @Override
    public int getRefCount() {
        return 1;
    }

    @Override
    public short[][] getNextFrame() {
        short[][] channels = new short[MicParam.VALID_CHANNELS.length][MicParam.SAMPLE_RATE * MicParam.DURATION_PER_FRAME_IN_MS / 1000];
        if (!mAudioChunkRecord.getChunkData(channels)) {
            Log.e(TAG, "[getNextFrame]: getChunkData return false.");
        }
        return channels;
    }

    private void init() {
        //8009平台需要先添加录音权限,其他平台不需要调用
        if (MicType.QCOM_8009_6_39 == BuildConfig.MIC_TYPE)
            PriorityManager.getInstance().start();

        if (null == mAudioChunkRecord) {
            mAudioChunkRecord = new AudioChunkRecord();
            mAudioChunkRecord.initialize(MicParam.DEVICE,
                    MicParam.CARD,
                    MicParam.SAMPLE_RATE,
                    MicParam.PERIOD_SIZE,
                    MicParam.PERIOD_COUNT,
                    MicParam.READ_LENGTH,
                    MicParam.VALID_CHANNELS,
                    MicParam.GAIN_NORM,
                    MicParam.GAIN_REF,
                    MicParam.DURATION_PER_FRAME_IN_MS);
        }
    }

    @Override
    public void onStart() {
        init();
//        mAudioChunkRecord.startLogging();
        mAudioChunkRecord.startRecording();
    }

    @Override
    public void onStop() {
//        mAudioChunkRecord.stopLogging();
        mAudioChunkRecord.stopRecording();
    }
}