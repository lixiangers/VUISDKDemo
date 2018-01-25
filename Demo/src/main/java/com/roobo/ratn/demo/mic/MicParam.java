package com.roobo.ratn.demo.mic;

/**
 * Created by lixiang on 18-1-9.
 * 这里参数是基于R3399平台+Roobo(6麦)麦阵的参数。
 */

public class MicParam {
    public static final int CARD = 1;
    public static final int DEVICE = 0;
    public static final int DURATION_PER_FRAME_IN_MS = 80;
    public static final int GAIN_NORM = 3;
    public static final int GAIN_REF = 1;
    public static final int PERIOD_COUNT = 4;
    public static final int PERIOD_SIZE = 1024;
    public static final int READ_LENGTH = 32768;
    public static final int SAMPLE_RATE = 16000;
    public static final byte[] VALID_CHANNELS = new byte[]{1, 7, 2, 8, 3, 9, 4};//6路麦克风信号 1路参考信号(参考信号放在最后)

    public static final int MIC_COUNT = 6;
    public static final int REF_COUNT = 1;
}
