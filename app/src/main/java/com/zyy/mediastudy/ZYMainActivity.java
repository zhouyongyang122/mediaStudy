package com.zyy.mediastudy;

import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

/**
 * Created by ZY on 17/11/1.
 */

public class ZYMainActivity extends ZYBaseActivity implements View.OnClickListener {

    static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "mediaStudy";

    Button btn_muxer_video;

    Button btn_muxer_audio;

    Button btn_combine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zy_activity_main);

        btn_muxer_video = (Button) findViewById(R.id.btn_muxer_video);
        btn_muxer_video.setOnClickListener(this);

        btn_muxer_audio = (Button) findViewById(R.id.btn_muxer_audio);
        btn_muxer_audio.setOnClickListener(this);

        btn_combine = (Button) findViewById(R.id.btn_combine);
        btn_combine.setOnClickListener(this);

        File file = new File(SDCARD_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    void muxerVidoe() {
        try {
            ZYMediaInfo videoInfo = new ZYMediaInfo(SDCARD_PATH + "/input.mp4", "video/");
            MediaMuxer mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output_video.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeTrackIndex = mediaMuxer.addTrack(videoInfo.getFormat());
            mediaMuxer.start();
            videoInfo.startMuxer(mediaMuxer, writeTrackIndex);
            mediaMuxer.stop();
            mediaMuxer.release();
            videoInfo.release();
            Log.e("muxerMedia", "finish");
        } catch (IOException e) {
            Log.e("muxerMedia-error: ", e.getMessage());
        }
    }

    void muxerAudio() {
        try {
            ZYMediaInfo videoInfo = new ZYMediaInfo(SDCARD_PATH + "/input.mp4", "audio/");
            MediaMuxer mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output_audio.mp3", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeAudioIndex = mediaMuxer.addTrack(videoInfo.getFormat());
            mediaMuxer.start();
            videoInfo.startMuxer(mediaMuxer, writeAudioIndex);
            mediaMuxer.stop();
            mediaMuxer.release();
            videoInfo.release();
            Log.e("muxerAudio", "finish");
        } catch (IOException e) {
            Log.e("muxerAudio-error: ", e.getMessage());
        }
    }

    void combineVideoAndAudio() {
        try {
            ZYMediaInfo videoInfo = new ZYMediaInfo(SDCARD_PATH + "/output_video.mp4", "video/");
            ZYMediaInfo audioInfo = new ZYMediaInfo(SDCARD_PATH + "/output_audio.mp3", "audio/");
            MediaMuxer mediaMuxer = new MediaMuxer(SDCARD_PATH + "/output.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeVideoTrackIndex = mediaMuxer.addTrack(videoInfo.getFormat());
            int writeAudioTrackIndex = mediaMuxer.addTrack(audioInfo.getFormat());
            mediaMuxer.start();
            videoInfo.startMuxer(mediaMuxer, writeVideoTrackIndex);
            audioInfo.startMuxer(mediaMuxer, writeAudioTrackIndex);
            mediaMuxer.stop();
            mediaMuxer.release();
            videoInfo.release();
            audioInfo.release();
        } catch (IOException e) {
            Log.e("comVAndA-error: ", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_muxer_video:
                //视频分离
                muxerVidoe();
                break;
            case R.id.btn_muxer_audio:
                //音频分离
                muxerAudio();
                break;
            case R.id.btn_combine:
                //音频，视频合成
                combineVideoAndAudio();
                break;
        }
    }
}
