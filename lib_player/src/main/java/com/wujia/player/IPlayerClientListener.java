package com.wujia.player;

import android.support.v4.media.session.PlaybackStateCompat;

import com.wj.lib.multimedia.bean.MusicEntity;

import java.util.List;

public interface IPlayerClientListener {

    void onPlayStateChanged(@PlaybackStateCompat.State int state);

    void onPlayDataChanged(MusicEntity musicInfo);

    void onMusicListChanged(List<MusicEntity> list);
}
