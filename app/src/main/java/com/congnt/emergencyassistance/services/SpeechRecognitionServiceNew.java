package com.congnt.emergencyassistance.services;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Locale;

public class SpeechRecognitionServiceNew extends BaseForegroundService implements RecognitionListener {
    private static final String TAG = "SpeechRecognition: ";
    protected AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected boolean mIsListening;
    private boolean mIsStreamSolo;
    private boolean mMute = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (MySharedPreferences.getInstance(this).isListening.load(false)) {
            startListening();
        }
        return START_STICKY;
    }

    /**
     * Handle event start or stop listening for speech
     *
     * @param item: true is start, false is stop
     */
    @Subscribe
    public void onEvent(EBE_StartStopService item) {
        if (item.getValue()) {
            startListening();
        } else {
            stopListening();
        }
    }

    @Override
    protected void initOnCreate() {
        ticker = getString(R.string.notifi_ticker_recognition);
        title = getString(R.string.notifi_title_recognition);
        text = getString(R.string.notifi_text_recognition);
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        setStreamMute(true);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
        Log.d(TAG, "onCreate: " + Locale.getDefault().toString() + Locale.getDefault().getCountry());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000); // value to wait
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 500);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true); //ERror from server
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

    }

    @Override
    protected void initStart() {
        mIsListening = true;
        MySharedPreferences.getInstance(this).isListening.save(mIsListening);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    @Override
    protected void initStop() {
        mIsListening = false;
        MySharedPreferences.getInstance(this).isListening.save(mIsListening);
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
        }
    }

    @Override
    protected int getFlagForeground() {
        return 100;
    }

    private void listenAgain() {
        if (mIsListening) {
            mIsListening = false;
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initStart();
                }
            }, 100);
        }
    }

    public void sendBroadcastToReceiver(Bundle b) {
        Intent i = new Intent("com.congnt.emergencyasistance.ACCIDENT_RECEIVER");
        i.putExtras(b);
        sendBroadcast(i);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AAA", "AAAA ON DESTROY");
        MySharedPreferences.getInstance(this).isListening.save(mIsListening);
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
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "AAAAAAAAA " + getErrorText(error));
        listenAgain();
    }

    @Override
    public void onResults(Bundle results) {
        List<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d("AAAA", matches.get(0));
//        EventBus.getDefault().post(new EBE_Result(matches));
        sendBroadcastToReceiver(results);
        listenAgain();
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