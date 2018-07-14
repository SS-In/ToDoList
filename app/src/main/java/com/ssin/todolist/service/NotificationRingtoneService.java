package com.ssin.todolist.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class NotificationRingtoneService extends Service {
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private int playedTimes;

    public static final String EXTRA_RING = "ring";

    public static final int RING_ONCE = 0;
    public static final int RING_FIVE_TIMES = 1;
    public static final int RING_NONSTOP = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mediaPlayer.setDataSource(this, ringtone);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int freq = intent.getIntExtra(NotificationRingtoneService.EXTRA_RING, 1);
        Log.d("INFO", "Freq: " + freq);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playedTimes++;
                if (playedTimes < freq) {
                    mediaPlayer.start();
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                } else {
                    playedTimes = 0;
                }
            }
        });
        mediaPlayer.start();

        return super.onStartCommand(intent, flags, startId);
    }
}
