package com.wujia.ui.indicator;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wujia.ui.R;

/**
 * desc
 *
 * @author WuJia.
 * @version 1.0
 * @date 2/17/21
 */
public class SmartTabLayout extends FrameLayout implements IPagerNavigator{

    private HorizontalScrollView mScrollView;
    private LinearLayout mTitleContainer;
    private LinearLayout mIndicatorContainer;

    private AbsSmartNavigatorAdapter mAdapter;
    private int mTotalCount;

    /**
     * 提供给外部的参数配置
     */
    /****************************************************/
    private boolean mAdjustMode;   // 自适应模式，适用于数目固定的、少量的title
    private boolean mEnablePivotScroll; // 启动中心点滚动
    private float mScrollPivotX = 0.5f; // 滚动中心点 0.0f - 1.0f
    private boolean mSmoothScroll = true;   // 是否平滑滚动，适用于 !mAdjustMode && !mFollowTouch
    private boolean mFollowTouch = true;    // 是否手指跟随滚动
    private int mRightPadding;
    private int mLeftPadding;
    private boolean mIndicatorOnTop;    // 指示器是否在title上层，默认为下层
    private boolean mSkimOver;  // 跨多页切换时，中间页是否显示 "掠过" 效果
    private boolean mReselectWhenLayout = true; // PositionData准备好时，是否重新选中当前页，为true可保证在极端情况下指示器状态正确
    /****************************************************/

    private final DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            mTotalCount = mAdapter.getCount();    // 如果使用helper，应始终保证helper中的totalCount为最新
            initView();
        }

        @Override
        public void onInvalidated() {
            // 没什么用，暂不做处理
        }
    };

    public SmartTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public SmartTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(AbsSmartNavigatorAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter!=null){
            mAdapter.registerDataSetObserver(mObserver);
            mTotalCount = mAdapter.getCount();
            if (mTitleContainer!=null){
                mAdapter.notifyDataSetChanged();
            }
        }else {
            mTotalCount = 0;
            initView();
        }
    }

    /************ 内部方法 **************/

    private void initView() {
        removeAllViews();
        View root;
        if (mAdjustMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.smart_indicator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.smart_indicator_layout, this);
        }
        mScrollView = root.findViewById(R.id.scroll_view);
        mTitleContainer = root.findViewById(R.id.title_container);
        mTitleContainer.setPadding(mLeftPadding, 0, mRightPadding, 0);
        mIndicatorContainer = root.findViewById(R.id.indicator_container);
        if (mIndicatorOnTop) {
            mIndicatorContainer.getParent().bringChildToFront(mIndicatorContainer);
        }
        initTitlesAndIndicator();
    }

    private void initTitlesAndIndicator() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttachToMagicIndicator() {

    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {

    }
}
