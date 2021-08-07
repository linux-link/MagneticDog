package com.wujia.jetpack.arch.view;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.wujia.jetpack.arch.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author wujia0916.
 * @date 20-3-10
 * @desc Base activity with mvvm and databinding
 */
public abstract class BaseMvvmActivity<M extends BaseViewModel, V extends ViewDataBinding>
        extends BaseBindingActivity<V> {

    protected M mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initViewModel();
        super.onCreate(savedInstanceState);
        initViewObservable(mViewModel);
        loadData(mViewModel);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected void initViewModel() {
        Class<M> modelClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            modelClass = (Class<M>) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            modelClass = (Class<M>) BaseViewModel.class;
        }
        mViewModel = createViewModel(this, modelClass);
        if (mViewModel == null) {
            mViewModel = new ViewModelProvider(this,
                    new ViewModelProvider.NewInstanceFactory()).get(modelClass);
        }
    }

    /**
     * 创建自定义的ViewModel.
     *
     * @param storeOwner
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T extends ViewModel> T createViewModel(ViewModelStoreOwner storeOwner,
                                                      Class<T> clazz) {
        if (createViewModelFactory() == null) {
            return null;
        }
        return new ViewModelProvider(storeOwner, createViewModelFactory()).get(clazz);
    }

    protected ViewModelProvider.Factory createViewModelFactory() {
        return null;
    }

    /**
     * 监听LiveData变化的模块.
     *
     * @param viewModel
     */
    protected abstract void initViewObservable(M viewModel);

    /**
     * 访问ViewModel的加载数据的方法，多数时候可能不需要实现.
     *
     * @param viewModel
     */
    protected abstract void loadData(M viewModel);

    public M getViewModel() {
        return mViewModel;
    }
}
