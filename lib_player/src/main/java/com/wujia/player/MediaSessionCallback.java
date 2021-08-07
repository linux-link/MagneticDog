package com.wujia.player;

import android.support.v4.media.session.MediaSessionCompat;

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private final MediaSessionCompat mMediaSession;
    private final MediaPlayerAdapter mMediaPlayerAdapter;

    public MediaSessionCallback(MediaSessionCompat mediaSession, MediaPlayerAdapter mediaPlayerAdapter) {
        mMediaSession = mediaSession;
        mMediaPlayerAdapter = mediaPlayerAdapter;
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
        mMediaPlayerAdapter.seekTo(pos);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayerAdapter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayerAdapter.stop();
        mMediaSession.setActive(false);
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();

    }

    @Override
    public void onSkipToPrevious() {
        super.onSkipToPrevious();

    }

    @Override
    public void onPlay() {
        super.onPlay();
    }
}
