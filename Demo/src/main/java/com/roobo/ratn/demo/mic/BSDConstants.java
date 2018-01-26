package com.roobo.ratn.demo.mic;

import com.roobo.ratn.demo.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixiang on 18-1-26.
 */

public class BSDConstants {
    private static Map<MicType, String> bsdMap = new HashMap<>();

    static {
        bsdMap.put(MicType.QCOM_8009_6_39, "sse321_6mic_1ref_bf_asl_aec_sdr_18nr_39mm.bsd");
        bsdMap.put(MicType.QCOM_8009_4_LINE, "Roobo_TV_4MicULA_Initial_3p21p0.bsd");
    }

    public static String getBSDFileName() {
        return bsdMap.get(BuildConfig.MIC_TYPE);
    }
}
