package com.wujia.jetpack.arch.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.wujia.jetpack.arch.BaseFragment;

/**
 * @author wujia0916.
 * @date 20-3-10
 * @desc Base fragment with dataBinding
 */
public abstract class BaseBindingFragment<V extends ViewDataBinding>
        extends BaseFragment {

    protected V mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (getLayout() == 0) {
            throw new IllegalArgumentException("getLayout() must not null");
        }
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        mBinding.setLifecycleOwner(this);
        initView();
        mRootView = mBinding.getRoot();
        return mRootView;
    }

    protected V getBinding() {
        return mBinding;
    }
}
