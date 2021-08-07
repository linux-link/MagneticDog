package com.wujia.jetpack.arch.view;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.wujia.jetpack.arch.BaseActivity;

/**
 * @author wujia0916.
 * @date 20-3-10
 * @desc Base activity with dataBinding
 */
public abstract class BaseBindingActivity<V extends ViewDataBinding>
        extends BaseActivity {

    protected V mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayout() == 0) {
            throw new IllegalArgumentException("getLayout() must not null");
        }
        mBinding = DataBindingUtil.setContentView(this, getLayout());
        initView(mBinding);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void initView(V binding);

    public V getBinding() {
        return mBinding;
    }
}
