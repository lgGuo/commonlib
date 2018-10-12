package com.glg.baselib.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.githang.statusbar.StatusBarCompat;
import com.glg.baselib.util.AppManager;
import com.glg.baselib.util.LoadingDialogUtil;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseSwipeActivity extends AppCompatActivity {

    /**
     * 获取布局资源文件
     */
    public abstract int getLayoutID();

    /**
     * 初始化控件
     */

    public abstract void initViews();


    /**
     * 是否需要用EventBus,默认为false,继承此activit
     * 如果需要用酒复写此方法，返回true
     *
     */
    public boolean isUseEventBus() {
        return false;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwipeBackHelper.onCreate(this);
        //是否与下一级activity联动(微信效果)。默认关;
        SwipeBackHelper.getCurrentPage(this) .setSwipeEdge(300).setSwipeRelateEnable(true);

        //保持竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutID());
        initViews();

        AppManager.getAppManager().addActivity(this);
        if (isUseEventBus()) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        SwipeBackHelper.onDestroy(this);
        AppManager.getAppManager().finishActivity(this);
    }


    /**
     * 显示loading
     * @param target 由哪个view而触发的loading,防止同一控件快速点击
     */
    public void showLoading(View target){
        if (target!=null){
            target.setClickable(false);
            target.setEnabled(false);
        }
        LoadingDialogUtil.showProgressDialog(this);

    }



    /**
     * 关闭loading
     * @param target 由哪个view而触发的loading
     */
    public void hideLoading( View target){
        if (target!=null){
            target.setClickable(true);
            target.setEnabled(true);
        }

        LoadingDialogUtil.closeProgressDialog();
    }


    /**
     * 浅色状态栏
     */
    protected void setLightStatuBar(){
        if(Build.VERSION.SDK_INT <=22){
            StatusBarCompat.setStatusBarColor(this, Color.parseColor("#9f999999"), false);
        }else {
            StatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT, true);
        }
    }


    /**
     * 透明状态栏
     * @param offsetView 需要偏移的view,防止view顶到状态栏上
     */
    protected void setTansparentStatuBar(View offsetView){
        if(Build.VERSION.SDK_INT <=22){
            StatusBarUtil.setTranslucentForImageViewInFragment(this,0,offsetView);

        }else {
            StatusBarUtil.setTranslucentForImageView(this,0,offsetView);

        }
    }



}
