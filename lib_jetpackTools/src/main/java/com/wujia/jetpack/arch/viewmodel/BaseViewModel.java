package com.wujia.jetpack.arch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.wujia.jetpack.arch.model.BaseRepository;

/**
 * Base viewModel.
 *
 * @author WuJia.
 * @version 1.0
 * @date 2020/9/8
 */
public abstract class BaseViewModel<M extends BaseRepository> extends ViewModel {

    public final String TAG = getClass().getSimpleName();

    protected final M mRepository;

    public BaseViewModel() {
        mRepository = initRepository();
    }

    protected abstract M initRepository();

    public M getRepository() {
        return mRepository;
    }
}
