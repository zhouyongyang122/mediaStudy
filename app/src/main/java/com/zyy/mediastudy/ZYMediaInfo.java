package com.zyy.mediastudy;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.nio.ByteBuffer;

/**
 * Created by ZY on 17/11/2.
 */

public class ZYMediaInfo {

    MediaExtractor mExtractor;

    MediaFormat mFormat;

    String mDataSourcePath;

    String mMimeType;

    int mTrackIndex = -1;

    boolean mHasError;

    private ZYMediaInfo() {

    }

    public ZYMediaInfo(String dataSourcePath, String mimeType) {
        mDataSourcePath = dataSourcePath;
        mMimeType = mimeType;
        init();
    }

    public void init() {
        if (mExtractor == null) {
            try {
                mExtractor = new MediaExtractor();
                mExtractor.setDataSource(mDataSourcePath);
                int trackCount = mExtractor.getTrackCount();
                for (int i = 0; i < trackCount; i++) {
                    mFormat = mExtractor.getTrackFormat(i);
                    String mimeType = mFormat.getString(MediaFormat.KEY_MIME);
                    if (mimeType.startsWith(mMimeType)) {
                        mTrackIndex = i;
                        break;
                    }
                }
                if (mTrackIndex < 0) {
                    mHasError = true;
                    return;
                }
            } catch (Exception e) {
                mHasError = true;
                mExtractor = null;
            }
        }
    }

    @SuppressWarnings("WrongConstant")
    public boolean startMuxer(MediaMuxer mediaMuxer, int writeTrackIndex) {
        init();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        mExtractor.selectTrack(mTrackIndex);
        ByteBuffer byteBuffer = ByteBuffer.allocate(500 * 1024);

        long sampleTime = ZYMediaUtils.getSampleTime(byteBuffer, mExtractor);
        mExtractor.unselectTrack(mTrackIndex);
        mExtractor.selectTrack(mTrackIndex);
        while (true) {
            int readVideoSampleSize = mExtractor.readSampleData(byteBuffer, 0);
            if (readVideoSampleSize < 0) {
                break;
            }
            bufferInfo.size = readVideoSampleSize;
            bufferInfo.presentationTimeUs += sampleTime;
            bufferInfo.offset = 0;
            bufferInfo.flags = mExtractor.getSampleFlags();
            mediaMuxer.writeSampleData(writeTrackIndex, byteBuffer, bufferInfo);
            mExtractor.advance();
        }
        return mHasError;
    }

    public void release() {
        if (mExtractor != null) {
            mExtractor.release();
            mExtractor = null;
        }
    }

    public MediaExtractor getExtractor() {
        return mExtractor;
    }

    public MediaFormat getFormat() {
        if (mFormat == null) {
            init();
        }
        return mFormat;
    }

    public String getDataSourcePath() {
        return mDataSourcePath;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public int getTrackIndex() {
        return mTrackIndex;
    }

    public boolean isHasError() {
        return mHasError;
    }
}
