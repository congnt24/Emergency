package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

/**
 * Created by congnt24 on 24/09/2016.
 */

public class SoundUtil {
    public static SoundUtil instance;
    private MediaPlayer mediaPlayer;

    public SoundUtil() {
    }

    public static SoundUtil getInstance() {
        if (instance == null) {
            instance = new SoundUtil();
        }
        return instance;
    }

    public static void setStreamMute(Context context, boolean isMute) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mAudioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC
                        , isMute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, AudioManager.ADJUST_SAME);
            }
        } else {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
        }
    }

    public void playSound(Context context, int idRaw) {
        mediaPlayer = MediaPlayer.create(context, idRaw);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.start();
    }

    public void playSoundRepeat(Context context, int idRaw) {
        mediaPlayer = MediaPlayer.create(context, idRaw);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
