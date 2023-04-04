package com.pikachu.utils.base;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.pikachu.utils.utils.LogsUtils;
import com.pikachu.utils.utils.ToastUtils;
import com.pikachu.utils.utils.ViewBindingUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pkpk.run
 * @project 通用工程
 * @package com.pikachu.utils.base
 * @date 2021/9/8
 * @description 用于懒加载 数据等
 */
public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    public final static String START_STR = "PIKACHU";
    protected View mRootView;
    protected boolean isVisible = false;   //是否可见
    private boolean isPrepared = false;    //是否初始化完成
    private boolean isFirst = true;        //是否第一次加载
    protected T binding;
    protected FragmentActivity context;


    public BaseFragment() {
        // Required empty public constructor
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mRootView == null) {
            return;
        }
        if (getUserVisibleHint()) {
            isVisible = true;
            initLoad();
        } else {
            isVisible = false;
            onHidden();
        }
    }






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ViewBindingUtils.create(getClass(), inflater, container);
        if (mRootView == null) {
            mRootView = binding.getRoot();
            //mRootView = initView(inflater, container, savedInstanceState);
            context = getActivity();
            onInitView(savedInstanceState, binding, context);
        }
        context = getActivity();
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        initLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint())
            setUserVisibleHint(true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = null;
        if (mRootView != null)
             parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
            parent.removeView(mRootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isVisible = false;
        isPrepared = false;
        isFirst = true;
        mRootView = null;
    }

    private void initLoad() {
        if (isPrepared && isVisible && !isFirst)
            onShow();
        if (!isPrepared || !isVisible || !isFirst)
            return;
        lazyLoad();
        isFirst = false;
    }


    /**
     * 初始化布局
     */
    protected abstract void onInitView(Bundle savedInstanceState,@NonNull T binding, @NonNull FragmentActivity activity);

    /**
     * 懒加载
     */
    protected void lazyLoad() {

    }


    /**
     * 不可见时调用
     */
    protected void onHidden() { }


    /**
     * 可见时调用
     *
     * (初始时不调用)
     */
    protected void onShow() { }



    // 发送包
    public static void setBundle(Fragment fragment, Serializable clz) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(START_STR, clz);
        fragment.setArguments(bundle);
    }
    protected <t extends Serializable> t getBundle(Class<t> cls) {
        if (this.getArguments() == null)
            return null;
        return cls.cast(this.getArguments().getSerializable(START_STR));
    }

    protected <t extends Serializable> t getBundle(Bundle savedInstanceState, Class<t> cls) {
        if (this.getArguments() == null)
            return null;
        return cls.cast(savedInstanceState.getSerializable(START_STR));
    }




    /**
     * 页面跳转
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }


    /**
     * 带数据页面跳转 序列化
     */
    protected void startActivity(Class<?> clz, Serializable cls) {
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转  int
     */
    protected void startActivity(Class<?> clz, int cls) {
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 string
     */
    protected void startActivity(Class<?> clz, String cls) {
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 float
     */
    protected void startActivity(Class<?> clz, float cls) {
        Intent intent = new Intent(getActivity(), clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }
    
    
    
    public void showToast(String... msg) {
        ToastUtils.showToast(context, LogsUtils.forStr(msg));
    }
    public void showToast(Object... msg) {
        ToastUtils.showToast(context, LogsUtils.forStr(Arrays.toString(msg)));
    }

    public void showLog(String... msg) {
        LogsUtils.showLog(context, LogsUtils.forStr(msg));
    }
    public void showLog(Object... msg) {
        List<String> strings = new ArrayList<>();
        for (Object f : msg)
            strings.add(f + "");
        LogsUtils.showLog(context, LogsUtils.forStr(strings.toArray(new String[]{})));
    }




    public int getColor(@ColorRes int id){
        return getResources().getColor(id);
    }



    /////////////////////////////////////// 状态栏 /////////////////////////////////////////////////

    /**
     * 重写此处 更改状态栏颜色
     * <!-- 导入包 -->
     * implementation 'com.gyf.immersionbar:immersionbar:3.0.0'
     * implementation 'com.gyf.immersionbar:immersionbar-components:3.0.0' // fragment快速实现（可选）
     *
     * <!-- 地址 -->
     * https://github.com/gyf-dev/ImmersionBar
     *
     * <!-- 全面屏 -->
     * 在manifest的Application节点中加入
     * android:resizeableActivity="true"
     *
     * <!--适配华为（huawei）刘海屏-->
     * <meta-data
     * android:name="android.notch_support"
     * android:value="true"/>
     *
     * <!--适配小米（xiaomi）刘海屏-->
     * <meta-data
     * android:name="notch.config"
     * android:value="portrait|landscape" />
     *
     * <!--androidx-->
     * android.enableJetifier=true
     *
     * @return ImmersionBar
     */
    protected ImmersionBar setImmersionBar() {
        return ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true); // 解决布局与状态栏重叠问题
    }

    /**
     * 设置状态栏为白色  字体为黑色
     */
    protected void setWindowBlack() {
        ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
                .init();

    }

    /**
     * 自定义颜色  并且设置字体为黑色
     */
    protected void setWindowBlack(@ColorRes int barColor, @ColorRes int navColor ) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
                .init();

    }




    /**
     * 设置状态栏为黑色 字体为白色
     */
    protected void setWindowWhite() {
        ImmersionBar.with(this)
                .barColor(android.R.color.black)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(true)
                .init();
    }


    /**
     * 自定义颜色  并且设置字体为白色
     */
    protected void setWindowWhite(@ColorRes int barColor, @ColorRes int navColor ) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(true)
                .init();
    }



    //设置 状态栏字体 导航栏图标 为黑色
    protected void setWindowTextBlack() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true) //  true 深色
                .fitsSystemWindows(true)
                .navigationBarDarkIcon(true)
                .init();
    }

    //设置 状态栏字体 导航栏图标 为白色
    protected void setWindowTextWhite() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(false)
                .fitsSystemWindows(true)
                .init();
    }




    //设置 状态栏字体 导航栏图标 为黑色
    protected void setWindowTextBlackNow() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true) //  true 深色
                .navigationBarDarkIcon(true)
                .init();
    }

    //设置 状态栏字体 导航栏图标 为白色
    protected void setWindowTextWhiteNow() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(false)
                .init();
    }




    //设置 全屏
    protected void setWindowFullScreen() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }


    //设置 显示状态栏
    protected void setWindowNoFullScreen() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .hideBar(BarHide.FLAG_SHOW_BAR).init();
    }


    //设置 显示状态栏并且布局延伸至状态栏
    protected void setNoFullViewToWindow() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .hideBar(BarHide.FLAG_SHOW_BAR)
                .init();
    }

    //设置 显示状态栏并且布局延伸至状态栏
    protected void setNoFullViewToWindow(@ColorRes int navColor, boolean barDark) {
        ImmersionBar.with(this)
                .navigationBarColor(navColor)
                .statusBarDarkFont(barDark) //  true 深色
                .fitsSystemWindows(false)
                .init();
    }
    //设置 显示状态栏并且布局不延伸至状态栏
    protected void setNoFullViewToWindow(@ColorRes int barColor, @ColorRes int navColor, boolean barDark, boolean fits) {
        ImmersionBar.with(this)
                .barColor(barColor)
                .navigationBarColor(navColor)
                .statusBarDarkFont(barDark) //  true 深色
                .fitsSystemWindows(fits)
                .init();
    }





    //设置 布局占用状态栏
    protected void setViewToWindow() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .init();
    }


/////////////////////////////////////// 状态栏 /////////////////////////////////////////////////


}