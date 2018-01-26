package com.roobo.ratn.demo.source;

import android.util.Log;

import com.roobo.media.AudioChunkRecord;
import com.roobo.priority.PriorityManager;
import com.roobo.ratn.demo.BuildConfig;
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
        return BuildConfig.MIC_TYPE.getVALID_CHANNELS().length;
    }

    @Override
    public int getRefCount() {
        return 1;
    }

    @Override
    public short[][] getNextFrame() {
        MicType micType = BuildConfig.MIC_TYPE;

        short[][] channels = new short[micType.getVALID_CHANNELS().length][micType.getSAMPLE_RATE() * micType.getDURATION_PER_FRAME_IN_MS() / 1000];
        if (!mAudioChunkRecord.getChunkData(channels)) {
            Log.e(TAG, "[getNextFrame]: getChunkData return false.");
        }
        return channels;
    }

    private void init() {
        //8009平台需要先添加录音权限,其他平台不需要调用
        PriorityManager.getInstance().start();

        if (null == mAudioChunkRecord) {
            mAudioChunkRecord = new AudioChunkRecord();
            MicType micType = BuildConfig.MIC_TYPE;
            mAudioChunkRecord.initialize(micType.getDEVICE(),
                    micType.getCARD(),
                    micType.getSAMPLE_RATE(),
                    micType.getPERIOD_SIZE(),
                    micType.getPERIOD_COUNT(),
                    micType.getREAD_LENGTH(),
                    micType.getVALID_CHANNELS(),
                    micType.getGAIN_NORM(),
                    micType.getGAIN_REF(),
                    micType.getDURATION_PER_FRAME_IN_MS());
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