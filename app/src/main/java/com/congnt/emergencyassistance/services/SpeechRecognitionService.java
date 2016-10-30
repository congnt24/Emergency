package com.congnt.emergencyassistance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

public class SpeechRecognitionService extends Service implements RecognitionListener {

    public static final int START_LISTENING = 1;
    public static final int STOP_LISTENING = 2;
    private static final String TAG = "SpeechRecognition: ";
    private static final int FOREGROUND_FLAGS = 101;
    protected AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected boolean mIsListening;
    private boolean mIsStreamSolo;
    private boolean mMute = true;
    private Notification notification;
    private Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    public SpeechRecognizer getSpeechRecognizer() {
        if (mSpeechRecognizer == null) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        }
        return mSpeechRecognizer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("AAAA", "AAAAAAAAA ON START COMMAND ");
        if (MySharedPreferences.getInstance(this).isListening.load(false)) {
            startListening();
            Log.d("AAAA", "AAAAAAAAA ON START COMMAND Start Listening");
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        setStreamMute(true);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000); // value to wait
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 500);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true); //ERror from server
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);


        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notification = new NotificationCompat.Builder(this)
                .setTicker("Listening for your help")
                .setContentTitle("Emergency Assistance")
                .setContentText("Listening for your help")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, showTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher)
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
    public void onEvent(EBE_StartStopService startOrstop) {
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
//        setStreamMute(true);
        if (!mIsListening) {
            mIsListening = true;
            MySharedPreferences.getInstance(this).isListening.save(mIsListening);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                // turn off beep sound
//                if (!mIsStreamSolo && mMute) {
//                    mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
//                    mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
//                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//                    mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
//                    mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
//                    mIsStreamSolo = true;
//                }
//            }
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            Log.d(TAG, "message start listening"); //$NON-NLS-1$
        }
    }

    private void stopListening() {
        mIsListening = false;
        MySharedPreferences.getInstance(this).isListening.save(mIsListening);

//        if (!mIsStreamSolo) {
//            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
//            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
//            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
//            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
//            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
//            mIsStreamSolo = true;
//        }
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
//            mSpeechRecognizer.destroy();
//            mSpeechRecognizer=null;
        }
        Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
    }

    private void listenAgain() {
        if (mIsListening) {
            mIsListening = false;
            mSpeechRecognizer.cancel();
            startListening();
        }
    }


   /* @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AAA", "AAAA ON DESTROY");
        MySharedPreferences.getInstance(this).isListening.save(mIsListening);
        setStreamMute(false);
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }
*/

    public void sendBroadcastToReceiver(Bundle b) {
        Intent i = new Intent("com.congnt.emergencyasistance.ACCIDENT_RECEIVER");
        i.putExtras(b);
        sendBroadcast(i);
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

        Log.d(TAG, "onBind");  //$NON-NLS-1$

        return mServerMessenger.getBinder();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
//        EventBus.getDefault().post(0);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
//        EventBus.getDefault().post(new EBE_RmsdB(rmsdB));
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
//        EventBus.getDefault().post(1);
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "AAAAAAAAA " + getErrorText(error));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listenAgain();
            }
        }, 100);
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


    public class IncomingHandler extends Handler
    {
        private WeakReference<SpeechRecognitionService> mtarget;

        IncomingHandler(SpeechRecognitionService target)
        {
            mtarget = new WeakReference<SpeechRecognitionService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final SpeechRecognitionService target = mtarget.get();

            switch (msg.what)
            {
                case START_LISTENING:

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        // turn off beep sound
//                        if (!mIsStreamSolo)
//                        {
//                            mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
//                            mIsStreamSolo = true;
//                        }
                    }
                    startListening();
                    startForeground(101,
                            notification);

                    break;

                case STOP_LISTENING:
//                    if (mIsStreamSolo)
//                    {
//                        mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
//                        mIsStreamSolo = false;
//                    }
                    stopListening();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_DETACH);
                    } else {
                        stopForeground(true);
                    }
                    break;
            }
        }
    }
}