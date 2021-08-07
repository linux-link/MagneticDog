package com.wujia.jetpack.pic;

import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public interface ILoadStrategy {

    void load(String url);

    void skipMemoryCache(boolean skip);

    void placeholder(@DrawableRes int drawableId);

    void centerCrop(boolean center);

    void animate(ViewPropertyAnimator animator);

    void into(ImageView view);

}
