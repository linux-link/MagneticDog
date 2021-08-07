package com.wujia.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

public abstract class MediaPlayerAdapter implements IMediaPlayerAction {
    private static final String TAG = BuildConfig.LIBRARY_PACKAGE_NAME + AudioFocusHelper.class.getSimpleName();

    private boolean mAudioNoisyReceiverRegistered = false;
    private static final IntentFilter AUDIO_NOISY_INTENT_FILTER = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private final BroadcastReceiver mAudioNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (isPlaying()) {
                    pause();
                }
            }
        }
    };

    private final Context mAppContext;
    private final AudioManager mAudioManager;
    private boolean mPlayOnAudioFocus = false;
    private final AudioFocusHelper mAudioFocusHelper = new AudioFocusHelper();

    public MediaPlayerAdapter(Context context) {
        mAppContext = context.getApplicationContext();
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void play() {
        if (mAudioFocusHelper.requestAudioFocus()) {
            registerAudioNoisyReceiver();
            onPlay();
        }
    }

    @Override
    public void pause() {
        if (!mPlayOnAudioFocus){
            mAudioFocusHelper.abandonAudioFocus();
        }
        unregisterAudioNoisyReceiver();
        onPause();
    }

    @Override
    public void stop() {
        mAudioFocusHelper.abandonAudioFocus();
        unregisterAudioNoisyReceiver();
        onStop();
    }

    private void registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            mAppContext.registerReceiver(mAudioNoisyReceiver, AUDIO_NOISY_INTENT_FILTER);
            mAudioNoisyReceiverRegistered = true;
        }
    }

    private void unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            mAppContext.unregisterReceiver(mAudioNoisyReceiver);
            mAudioNoisyReceiverRegistered = false;
        }
    }

    public final class AudioFocusHelper {

        public boolean requestAudioFocus() {
            final int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }

        public void abandonAudioFocus() {
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }

        private final AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                Log.i(TAG, "onAudioFocusChange:" + focusChange);
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        if (mPlayOnAudioFocus && !isPlaying()) {
                            play();
                        } else if (isPlaying()) {
                            setVolume(MEDIA_VOLUME_DEFAULT);
                        }
                        mPlayOnAudioFocus = false;
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        mAudioManager.abandonAudioFocus(this);
                        mPlayOnAudioFocus = false;
                        stop();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // 暂时失去了音频焦点，但你可以小声地继续播放音频（低音量）而不是完全扼杀音频
                        setVolume(MEDIA_VOLUME_DUCK);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // 暂时失去了音频焦点
                        if (isPlaying()) {
                            mPlayOnAudioFocus = true;
                            pause();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }


}
