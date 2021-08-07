package com.wujia.player.client;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.wujia.player.IPlayerClientListener;
import com.wujia.player.bean.MusicEntity;

import java.util.ArrayList;
import java.util.List;

public class MediaClientHelper {

    public static final String TAG = BuildConfig.LIBRARY_PACKAGE_NAME + MediaClientHelper.class.getSimpleName();

    private static volatile MediaClientHelper sMediaClientHelper;

    private final Context mContext;
    private final List<IPlayerClientListener> mPlayStateListeners = new ArrayList<>();
    private final List<MusicEntity> mMusicList = new ArrayList<>();

    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mMediaController;

    private MediaClientHelper(Context context) {
        mContext = context.getApplicationContext();
        if (mMediaBrowser == null) {
            mMediaBrowser = new MediaBrowserCompat(
                    mContext,
                    new ComponentName(mContext, MusicService.class),
                    mConnectionCallback,
                    null
            );
        }
        mMediaBrowser.connect();
    }

    public static MediaClientHelper getInstance() {
        if (sMediaClientHelper == null) {
            synchronized (MediaClientHelper.class) {
                if (sMediaClientHelper == null) {
                    sMediaClientHelper = new MediaClientHelper(AppGlobal.getApplication());
                }
            }
        }
        return sMediaClientHelper;
    }

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            super.onConnected();
            mMediaController = new MediaControllerCompat(mContext, mMediaBrowser.getSessionToken());
            mMediaController.registerCallback(mMediaControllerCallback);
            mMediaControllerCallback.onMetadataChanged(mMediaController.getMetadata());
            mMediaControllerCallback.onPlaybackStateChanged(mMediaController.getPlaybackState());
            mMediaBrowser.subscribe(mMediaBrowser.getRoot(), new Bundle(), new MediaBrowserCompat.SubscriptionCallback() {

                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children,
                                             @NonNull Bundle options) {
                    super.onChildrenLoaded(parentId, children, options);
                    for (MediaBrowserCompat.MediaItem mediaItem : children) {
                        mMediaController.addQueueItem(mediaItem.getDescription());
                    }
                    mMusicList.clear();
                    List<MusicEntity> list = options.getParcelableArrayList(LocalMediaUtils.class.getSimpleName());
                    if (list != null) {
                        mMusicList.addAll(list);
                    }else {
                        Log.i(TAG, "onChildrenLoaded: list is null");
                    }
                    mMediaController.getTransportControls().prepare();
                }
            });
        }
    };

    private final MediaControllerCompat.Callback mMediaControllerCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);

        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            for (IPlayerClientListener listener : mPlayStateListeners) {
                listener.onPlayStateChanged(state.getState());
            }
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

    };

    public void play(String mediaId) {
        if (mMediaController != null) {
            mMediaController.getTransportControls().playFromMediaId(mediaId, null);
        }
    }

    public void play() {
        if (mMediaController != null) {
            mMediaController.getTransportControls().play();
        }
    }

    public void stop() {
        if (mMediaController != null) {
            mMediaController.getTransportControls().stop();
        }
    }

    public void pause() {
        if (mMediaController != null) {
            mMediaController.getTransportControls().pause();
        }
    }

    public void seekTo(long position) {
        if (mMediaController != null) {
            mMediaController.getTransportControls().seekTo(position);
        }
    }

    public void skipToNext() {
        if (mMediaController != null) {
            mMediaController.getTransportControls().skipToNext();
        }
    }

    public void skipToPrevious() {
        if (mMediaController != null) {
            mMediaController.getTransportControls().skipToPrevious();
        }
    }

    public void setVolume(int volume) {
        if (mMediaController != null) {
            mMediaController.setVolumeTo(volume, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void setPlayStateListener(@NonNull IPlayerClientListener listener) {
        mPlayStateListeners.add(listener);
        if (mMediaController != null) {
            listener.onPlayStateChanged(mMediaController.getPlaybackState().getState());
            listener.onPlayDataChanged(LocalMediaUtils.Metadata2Music(mMediaController.getMetadata()));
            listener.onMusicListChanged(mMusicList);
        }
    }

    public final class AppLifecycle implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onAppResume() {
            if (mMediaBrowser != null && !mMediaBrowser.isConnected()) {
                mMediaBrowser.connect();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onAppPause() {
            if (mMediaController != null) {
                mMediaController.unregisterCallback(mMediaControllerCallback);
                mMediaController = null;
            }
            if (mMediaBrowser != null) {
                mMediaBrowser.disconnect();
                mMediaBrowser = null;
            }
        }
    }
}
