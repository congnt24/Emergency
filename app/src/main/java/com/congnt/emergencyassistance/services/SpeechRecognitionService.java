package com.congnt.emergencyassistance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.congnt.emergencyassistance.EventBusEntity.EBE_Result;
import com.congnt.emergencyassistance.EventBusEntity.EBE_RmsdB;
import com.congnt.emergencyassistance.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class SpeechRecognitionService extends Service implements RecognitionListener {

    private static final String TAG = "SpeechRecognition: ";
    private static final int FOREGROUND_FLAGS = 101;
    protected AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected boolean mIsListening;
    private Notification notification;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setStreamMute(true);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2000);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        notification = new NotificationCompat.Builder(this)
                .setTicker("Listening for you")
                .setContentTitle("Listening")
                .setContentText("Speech")
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .build();
    }

    private void setStreamMute(boolean isMute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mAudioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC
                        , isMute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_MUTE, 0);
            }
        } else {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
        }
    }

    /**
     * Handle event start or stop listening for speech
     *
     * @param startOrstop: true is start, false is stop
     */
    @Subscribe
    public void onServiceEvent(EBE_StartStopService startOrstop) {
        if (startOrstop.aBoolean) {
            startListening();
            startForeground(101,
                    notification);
        } else {
            stopListening();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH);
            } else {
                stopForeground(true);
            }
        }
    }

    private void startListening() {
        setStreamMute(true);
        if (!mIsListening) {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            mIsListening = true;
            Log.d(TAG, "message start listening"); //$NON-NLS-1$
        }
    }

    private void stopListening() {
        mSpeechRecognizer.cancel();
        mIsListening = false;
        Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        setStreamMute(false);
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    /**
     * Use Evenbus instead of binder
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
        EventBus.getDefault().post(0);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        EventBus.getDefault().post(new EBE_RmsdB(rmsdB));
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        EventBus.getDefault().post(1);
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "AAAAAAAAA " + getErrorText(error));
        stopListening();
        startListening();
    }

    @Override
    public void onResults(Bundle results) {
        List<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        EventBus.getDefault().post(new EBE_Result(matches));
        stopListening();
        startListening();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}