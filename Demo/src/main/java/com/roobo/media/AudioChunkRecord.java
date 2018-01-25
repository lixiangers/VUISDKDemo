package com.roobo.media;

/**
 * Created by yangliang on 17-5-9.
 */
public class AudioChunkRecord {

    private volatile long mNativePtr;

    static {
        //加载库文件
        System.loadLibrary("AudioChunkrecord-jni");
    }

    public AudioChunkRecord() {
    }

    public void initialize(int device,
                           int card,
                           int sampleRate,
                           int periodSize,
                           int periodCount,
                           int periodLength,
                           byte[] validChannels,
                           int gain_mic,
                           int gain_ref,
                           int durationPerFrameInMs) {
        native_initialize(device, card, sampleRate, periodSize, periodCount, periodLength, validChannels, gain_mic, gain_ref, durationPerFrameInMs);
    }

    public boolean startRecording() {
        return native_start();
    }

    public boolean stopRecording() {
        return native_stop();
    }

    public boolean release() {
        return native_release();
    }

    public boolean getChunkData(short[][] chunks) {
        return native_get_chunk_data(chunks);
    }

    public boolean startLogging(String fileName) {
        return native_start_save(fileName);
    }

    public boolean stopLogging() {
        return native_stop_save();
    }

    private native boolean native_start();

    private native boolean native_stop();

    private native boolean native_release();

    private native boolean native_get_chunk_data(short[][] chunks);

    private native void native_initialize(int device,
                                          int card,
                                          int sampleRate,
                                          int periodSize,
                                          int periodCount,
                                          int periodLength,
                                          byte[] validChannels,
                                          int gain_mic,
                                          int gain_ref,
                                          int durationPerFrameInMs);

    private native boolean native_start_save(String filename);

    private native boolean native_stop_save();
}
