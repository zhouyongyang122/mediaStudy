package com.zyy.mediastudy;

import android.media.MediaExtractor;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by ZY on 17/11/2.
 */

public class ZYMediaUtils {

    /**
     * 获取帧之间的相隔时间
     *
     * @param byteBuffer
     * @param mediaExtractor
     * @return
     */
    public static long getSampleTime(ByteBuffer byteBuffer, MediaExtractor mediaExtractor) {
        long sampleTime;
        mediaExtractor.readSampleData(byteBuffer, 0);
        //skip first I frame
        if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
            mediaExtractor.advance();
        }
        mediaExtractor.readSampleData(byteBuffer, 0);
        long firstVideoPTS = mediaExtractor.getSampleTime();
        mediaExtractor.advance();
        mediaExtractor.readSampleData(byteBuffer, 0);
        long SecondVideoPTS = mediaExtractor.getSampleTime();
        sampleTime = Math.abs(SecondVideoPTS - firstVideoPTS);
        Log.e("getSampleTime", "getSampleTime is " + sampleTime);
        return sampleTime;
    }
}
