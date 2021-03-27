package com.wj.lib.multimedia;

import android.content.Context;

public class SimpleExoPlayerAdapter extends MediaPlayerAdapter{

    public SimpleExoPlayerAdapter(Context context) {
        super(context);
    }

    @Override
    public void release() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public void setVolume(float volume) {

    }
}
