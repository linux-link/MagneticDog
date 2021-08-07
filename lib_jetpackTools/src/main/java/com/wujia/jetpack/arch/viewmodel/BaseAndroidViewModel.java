package com.wujia.jetpack.arch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.wujia.jetpack.arch.model.BaseRepository;

import org.jetbrains.annotations.NotNull;

/**
 * Base viewModel.
 *
 * @author WuJia.
 * @version 1.0
 * @date 2020/9/8
 */
public abstract class BaseAndroidViewModel<M extends BaseRepository> extends AndroidViewModel {

    protected final M mRepository;

    public BaseAndroidViewModel(@NonNull @NotNull Application application) {
        super(application);
        mRepository = initRepository();
    }

    protected abstract M initRepository();

    public M getRepository() {
        return mRepository;
    }
}
