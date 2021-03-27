package com.wj.lib.multimedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.wj.lib.multimedia.notification.MediaNotificationManager;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    private static final String MY_MEDIA_ROOT_ID = "0x0001";
    private static final String MY_EMPTY_MEDIA_ROOT_ID = "0x0001";

    private static final String TAG = BuildConfig.LIBRARY_PACKAGE_NAME + MusicService.class.getSimpleName();

    private MediaNotificationManager mNotificationManager;

    private MediaPlayerAdapter mMediaPlayer;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mPlaybackStateBuilder;
    private MediaSessionCallback mMediaCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        // init player adapter
        mMediaPlayer = new SimpleMediaPlayerAdapter(this);
        // init media session
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaCallback = new MediaSessionCallback(mMediaSession, mMediaPlayer);
        mMediaSession.setCallback(mMediaCallback);
//        mMediaSession.setFlags();
        setSessionToken(mMediaSession.getSessionToken());
        mPlaybackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build());
        // init notification manager
        mNotificationManager = new MediaNotificationManager(this);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.release();
        mMediaSession.release();
        mMediaPlayer.release();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    // 控制客户端对服务的访问，如果该方法返回 null，则会拒绝连接。
    // 要允许客户端连接到您的服务并浏览其媒体内容，onGetRoot() 必须返回非 null 的 BrowserRoot，这是代表您的内容层次结构的根 ID。
    //  要允许客户端连接到MediaSession 而不进行浏览，onGetRoot() 仍然必须返回非 null 的 BrowserRoot，但此根 ID 应代表一个空的内容层次结构
    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        if (allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            return new BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null);
        }
    }

    private boolean allowBrowsing(String clientPkg, int clientUid) {
        return true;
    }

    // 客户端连接后，可以通过重复调用 MediaBrowserCompat.subscribe() 来遍历内容层次结构，以构建界面的本地表示方式。
    // subscribe() 方法将回调 onLoadChildren() 发送给服务，该服务会返回 MediaBrowser.MediaItem 对象的列表。
    // 每个 MediaItem 都有一个唯一的 ID 字符串，这是一个不透明令牌。
    // 当客户端想要打开子菜单或播放某项内容时，它就会传递此 ID。您的服务负责将此 ID 与相应的菜单节点或内容项关联起来
    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        //  禁止浏览服务
        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID, parentId)) {
            result.sendResult(null);
            return;
        }
        List<MediaBrowserCompat.MediaItem> mediaItems = MusicLibrary.getMediaItems();
        if (MY_MEDIA_ROOT_ID.equals(parentId)) {

        } else {

        }
        result.sendResult(mediaItems);
    }
}
