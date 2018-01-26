package com.roobo.ratn.demo.mic;

/**
 * Created by lixiang on 18-1-9.
 * 现在Demo演示是支持Android标准Mic,8009+6麦(39mm)两种型号
 */

public enum MicType {
    STANDARD(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, 0, 0),
    //8009平台上_6麦_39mm
    QCOM_8009_6_39(0, 0, 80, 3, 1, 4, 1024, 32768, 16000, 256, new byte[]{1, 7, 2, 8, 3, 9, 5}, 6, 1),
    //8009平台上_线性_4麦
    QCOM_8009_4_LINE(0, 0, 80, 3, 1, 4, 1024, 32768, 16000, 160, new byte[]{1, 7, 2, 8, 5}, 4, 1);

    MicType(int card, int device, int duration_per_frame_in_ms,
            int gain_norm, int gain_ref, int period_count,
            int period_size, int read_length, int SAMPLE_RATE, int frame_shift,
            byte[] valid_channels, int mic_count, int ref_count) {

        this.CARD = card;
        this.DEVICE = device;
        this.DURATION_PER_FRAME_IN_MS = duration_per_frame_in_ms;
        this.GAIN_NORM = gain_norm;
        this.GAIN_REF = gain_ref;
        this.PERIOD_COUNT = period_count;
        this.PERIOD_SIZE = period_size;
        this.READ_LENGTH = read_length;
        this.SAMPLE_RATE = SAMPLE_RATE;
        this.FRAME_SHIFT = frame_shift;
        this.VALID_CHANNELS = valid_channels;
        this.MIC_COUNT = mic_count;
        this.REF_COUNT = ref_count;
    }

    private int CARD = 0;
    private int DEVICE = 0;
    private int DURATION_PER_FRAME_IN_MS = 80;
    private int GAIN_NORM = 3;
    private int GAIN_REF = 1;
    private int PERIOD_COUNT = 4;
    private int PERIOD_SIZE = 1024;
    private int READ_LENGTH = 32768;
    private int SAMPLE_RATE = 16000;
    private int FRAME_SHIFT = 256;
    private byte[] VALID_CHANNELS = new byte[]{1, 7, 2, 8, 3, 9, 5};//6路麦克风信号 1路参考信号(参考信号放在最后)

    private int MIC_COUNT = 6;
    private int REF_COUNT = 1;


    public int getCARD() {
        return CARD;
    }

    public int getDEVICE() {
        return DEVICE;
    }

    public int getDURATION_PER_FRAME_IN_MS() {
        return DURATION_PER_FRAME_IN_MS;
    }

    public int getGAIN_NORM() {
        return GAIN_NORM;
    }

    public int getGAIN_REF() {
        return GAIN_REF;
    }

    public int getPERIOD_COUNT() {
        return PERIOD_COUNT;
    }

    public int getPERIOD_SIZE() {
        return PERIOD_SIZE;
    }

    public int getREAD_LENGTH() {
        return READ_LENGTH;
    }

    public int getSAMPLE_RATE() {
        return SAMPLE_RATE;
    }

    public byte[] getVALID_CHANNELS() {
        return VALID_CHANNELS;
    }

    public int getMIC_COUNT() {
        return MIC_COUNT;
    }

    public int getREF_COUNT() {
        return REF_COUNT;
    }

    public int getFRAME_SHIFT() {
        return FRAME_SHIFT;
    }
}
