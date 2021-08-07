package com.wujia.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.wj.lib.multimedia.R;
import com.wj.lib.multimedia.databinding.LayoutMusicActionControlBarBinding;

public class MusicActionControlLayout extends ConstraintLayout {

    private final LayoutMusicActionControlBarBinding mBinding;
    private IMusicClickListener mClickListener;

    public MusicActionControlLayout(@NonNull Context context) {
        this(context, null);
    }

    public MusicActionControlLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicActionControlLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_music_action_control_bar, this);
        mBinding = LayoutMusicActionControlBarBinding.bind(rootView);
        initListener();
    }

    private void initListener() {
        mBinding.ivPrevious.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onPreviousClickListener();
            }
        });
        mBinding.ivNext.setOnClickListener(v->{
            if (mClickListener != null) {
                mClickListener.onNextClickListener();
            }
        });
        mBinding.ivPlay.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onPlayClickListener();
            }
        });
        mBinding.ivPlayMenu.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onPlayMenuClickListener();
            }
        });
        mBinding.ivPlayType.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onPlayTypeClickListener();
            }
        });
    }


    public void setClickListener(IMusicClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface IMusicClickListener {

        void onNextClickListener();

        void onPreviousClickListener();

        void onPlayClickListener();

        void onPlayMenuClickListener();

        void onPlayTypeClickListener();
    }

}
