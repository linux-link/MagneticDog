package com.wujia.player;

public interface IMediaPlayerAction {

    float MEDIA_VOLUME_DEFAULT = 1.0f;
    float MEDIA_VOLUME_DUCK = 0.2f;

    void play();

    boolean isPlaying();

    void onPlay();

    void stop();

    void onStop();

    void pause();

    void onPause();

    void seekTo(long position);

    void setVolume(float volume);

    void release();

}
