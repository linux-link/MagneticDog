package com.wujia.jetpack.arch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * @author wujia0916.
 * @date 20-3-10
 * @desc
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void startActivity(Class<FragmentActivity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void startActivity(Class<FragmentActivity> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * Start container activity.
     *
     * @param canonicalName canonical name
     * @param bundle        bundle
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    /**
     * Start activity with animation.
     *
     * @param sharedElement     shared element
     * @param sharedElementName shared element name
     * @param activityClass     activity
     * @param bundle            bundle
     */
    public void startSceneTransitionActivity(@NonNull View sharedElement,
                                             @NonNull String sharedElementName,
                                             Class<FragmentActivity> activityClass,
                                             Bundle bundle) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, sharedElement, sharedElementName
        );
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent, compat.toBundle());
    }

    protected void fullScreen() {
        fullScreen(false);
    }

    protected void fullScreen(boolean showTitleBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View decorView = window.getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags = attributes.flags | flagTranslucentStatus;
                attributes.flags = attributes.flags | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

}
