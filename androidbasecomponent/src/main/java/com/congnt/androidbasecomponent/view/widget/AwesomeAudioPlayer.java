package com.congnt.androidbasecomponent.view.widget;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.congnt.androidbasecomponent.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by Cong on 2/1/2015.
 */
public class AwesomeAudioPlayer {
    public static final int AUDIO_UPDATE_TIME = 100;
    public static final String TAG = AwesomeAudioPlayer.class.getSimpleName();
    private static AwesomeAudioPlayer audioCong;
    public Context mcontext;
    MediaMetadataRetriever metadataRetriever; //get audio info
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler handleUpdateSeekBar;
    //    private Uri uri;
    private View btnPlay, btnPause;
    private boolean autoScroll = true;
    private long totalTime = 0;
    /*
    Indicate the current run-time of the audio player
     */
    private TextView tvTotaltime, tvTitle;

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (seekBar == null) return;
            if (handleUpdateSeekBar != null && mediaPlayer != null) {
                int currentTime = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentTime);
                updateRunTime(currentTime);
                handleUpdateSeekBar.postDelayed(this, AUDIO_UPDATE_TIME);
            }
        }
    };


    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            int currentTime = 0;
            seekBar.setProgress(currentTime);
            updateRunTime(currentTime);
            setPlayable();

        }
    };

    public static AwesomeAudioPlayer getInstance() {
        if (audioCong == null) {
            audioCong = new AwesomeAudioPlayer();
        }
        return audioCong;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("path", cursor.getString(column_index));
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //---------------------Play pause----------------------
    public void play() {
        if (mediaPlayer == null) {
            throw new IllegalStateException("MediaPlayer cannot be null");
        }
        if (mediaPlayer.isPlaying())
            return;
        if (btnPlay == null) {
            throw new IllegalStateException("btnPlay cannot be null");
        }
        handleUpdateSeekBar.postDelayed(updateProgress, AUDIO_UPDATE_TIME);
        mediaPlayer.start();
        setPausable();// Play gone Pause visitble
    }

    private void setPausable() {
        Log.d(TAG, "Pausable");
        if (btnPlay != null) {
            Log.d(TAG, "Play gone");
            btnPlay.setVisibility(View.GONE);
        }
        if (btnPause != null) {
            Log.d(TAG, "Pause visitble");
            btnPause.setVisibility(View.VISIBLE);
        }
    }

    public void pause() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d(TAG, "pause() start");
            setPlayable();
        } else {
            setPlayable();
        }
    }

    private void setPlayable() {
        if (btnPause != null) {
            btnPause.setVisibility(View.GONE);
        }
        if (btnPlay != null) {
            btnPlay.setVisibility(View.VISIBLE);
        }
    }

    public AwesomeAudioPlayer init(Context context, Uri uri) {
        if (uri == null) {
            throw new NullPointerException("Uri cannot be null");
        }
        if (audioCong == null) {
            audioCong = new AwesomeAudioPlayer();
        }
        mcontext = context;
        autoScroll = true;
        handleUpdateSeekBar = new Handler();
        initPlayer(context, uri);
        initEvents();

        return this;
    }

    //---------Playautio from File
    public AwesomeAudioPlayer init(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        if (uri == null) {
            throw new NullPointerException("Uri cannot be null");
        }
        if (audioCong == null) {
            audioCong = new AwesomeAudioPlayer();
        }
        mcontext = context;
        autoScroll = true;
        handleUpdateSeekBar = new Handler();
        initPlayer(context, uri);
        initEvents();
        //init audio info
        if (metadataRetriever == null) {
            metadataRetriever = new MediaMetadataRetriever();
        }

        metadataRetriever.setDataSource(context, uri);
        if (tvTitle != null)
            tvTitle.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));


        return this;
    }

    //---Play audio from URL---
   /* public AudioCong init(Context context, String url) {
        if (url.equals(""))
            return this;
        if (audioCong == null) {
            audioCong = new AudioCong();
        }
        mcontext = context;
//        new downloadFile().execute(url);
        init(context, AudioDownloader.getInstance().init(context, url).startDownload());

        return this;
    }*/

    private void initEvents() {
        initMediaSeekBar();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

    }

    private void initPlayer(Context context, Uri uri) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(onCompletion);
    }

    //---------Set Default View-----------
    public AwesomeAudioPlayer setDefaultUi(ViewGroup container, LayoutInflater inflater) {
        if (container == null) {
            throw new NullPointerException("Container or Inflater cannot be null");
        }
        if (inflater == null) {
            throw new IllegalArgumentException("Container or Inflater cannot be null");
        }
        View rootView = inflater.inflate(R.layout.layout_awesome_audio, container, true);
        View playView = rootView.findViewById(R.id.play);
        View pauseView = rootView.findViewById(R.id.pause);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.media_seekbar);
        TextView playbackTime = (TextView) rootView.findViewById(R.id.playback_time);
        setPlayView(playView);
        setPauseView(pauseView);
        setSeekBarView(seekBar);
        setPlaybackTime(playbackTime);
        return this;
    }

    public AwesomeAudioPlayer setTitleView(TextView title) {
        if (title == null) {
            throw new NullPointerException("Title cannot be null");
        }
        tvTitle = title;
        return this;
    }

    public AwesomeAudioPlayer setPlaybackTime(TextView playbackTime) {
        if (playbackTime == null) {
            throw new NullPointerException("TextView cannot be null");
        }
        tvTotaltime = playbackTime;
        return this;
    }

    public AwesomeAudioPlayer setPlayView(View play) {
        if (play == null) {
            throw new NullPointerException("PlayView cannot be null");
        }
        btnPlay = play;
        return this;
    }

    public AwesomeAudioPlayer setPauseView(View pause) {
        if (pause == null) {
            throw new NullPointerException("PlayView cannot be null");
        }
        btnPause = pause;
        return this;
    }

    private AwesomeAudioPlayer setSeekBarView(SeekBar seekB) {
        if (seekB == null) {
            throw new NullPointerException("SeekBar cannot be null");
        }
        seekBar = seekB;
        return this;
    }

    private void initMediaSeekBar() {
        if (seekBar == null) return;
        totalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) totalTime);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                updateRunTime(seekBar.getProgress());
            }
        });
    }

    private void updateRunTime(int i) {
        if (tvTotaltime == null) {
            return;
        }
        if (mediaPlayer != null) {
            int min = (int) TimeUnit.MILLISECONDS.toMinutes(i);
            int min2 = (int) TimeUnit.MILLISECONDS.toMinutes(totalTime);
            tvTotaltime.setText(String.format("%02d:%02d/%02d:%02d", min, TimeUnit.MILLISECONDS.toSeconds(i) - min * 60, min2, TimeUnit.MILLISECONDS.toSeconds(totalTime) - 60 * min2));
        }
    }

    //Release mediaPlayer
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}